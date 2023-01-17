/*******************************************************************************
 * Copyright 2019 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.
 *******************************************************************************/
package org.gentar.error_management;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import jakarta.validation.ConstraintViolation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.Data;
import org.gentar.exceptions.OperationFailedException;
import org.gentar.exceptions.SystemOperationFailedException;
import org.gentar.exceptions.UserOperationFailedException;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

/**
 * Class to keep detailed information of exceptions raised in the application.
 * Taken from https://github.com/brunocleite/spring-boot-exception-handling.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(
    include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.CUSTOM, property = "error", visible = true)
@JsonTypeIdResolver(LowerCaseClassNameResolver.class)
public class ApiError
{
    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;
    private List<ApiSubError> subErrors;

    @JsonIgnore
    private Throwable exception;

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiError.class);
    private static final String ERROR_MESSAGE_TO_LOG =
        "Exception occurred. Message:[%s]. DebugMessage:[%s]. SubErrors:[subErrors]";
    private static final String ERROR_MESSAGE =
        "An unexpected error has occurred in the system: %s. Please contact the administrator " +
            "to check the errors in the logs.";

    private ApiError()
    {
        timestamp = LocalDateTime.now();
    }

    public static ApiError of(OperationFailedException exception)
    {
        ApiError apiError = new ApiError();
        if (exception instanceof UserOperationFailedException)
        {
            UserOperationFailedException uofe = (UserOperationFailedException) exception;
            apiError = buildFromUserOperationFailedException(uofe);
        }
        else if (exception instanceof SystemOperationFailedException)
        {
            SystemOperationFailedException sofe = (SystemOperationFailedException) exception;
            apiError = buildFromSystemOperationFailedException(sofe);
        }
        return apiError;
    }

    private static ApiError buildFromUserOperationFailedException(UserOperationFailedException exception)
    {
        ApiError apiError = new ApiError();
        apiError.exception = exception;
        apiError.status = HttpStatus.INTERNAL_SERVER_ERROR;
        apiError.message = getExceptionMessage(exception);
        apiError.debugMessage = exception.getDebugMessage();
        logError(apiError.exception, apiError.debugMessage);
        return apiError;
    }

    private static ApiError buildFromSystemOperationFailedException(SystemOperationFailedException exception)
    {
        ApiError apiError = new ApiError();
        apiError.exception = exception;
        apiError.status = HttpStatus.INTERNAL_SERVER_ERROR;
        apiError.message = exception.getMessage();
        apiError.debugMessage = exception.getDebugMessage();
        logErrorWithExceptionDetail(apiError.message, buildDetailedError(exception));
        return apiError;
    }

    private static String buildDetailedError(SystemOperationFailedException exception)
    {
        int numberElementsStackToDebug = 4;
        Throwable cause = exception.getCause();
        StackTraceElement[] stackTraceElements;
        String causeError = "";
        if (cause == null)
        {
            stackTraceElements = exception.getStackTrace();
        }
        else
        {
            stackTraceElements = cause.getStackTrace();
            causeError = "[" + cause.toString() + "]";
        }

        List<String> stackMessages = new ArrayList<>();
        for (int i = 0; i < stackTraceElements.length && i < numberElementsStackToDebug; i++)
        {
            stackMessages.add(stackTraceElements[i].toString());
        }

        return
            causeError + exception.getExceptionDetail()
                + ". Truncated Stack: " + stackMessages.toString();
    }

    private static String getExceptionMessage(Throwable exception)
    {
        String message = exception.getMessage();
        if (message == null)
        {
            Throwable cause = exception.getCause();
            if (cause == null)
            {
                cause = exception;
            }
            message = String.format(ERROR_MESSAGE, cause.toString());
        }
        return message;
    }

    public ApiError(HttpStatus status, Throwable ex)
    {
        this(status, "Unexpected error.", ex);
    }

    public ApiError(HttpStatus status, String message, Throwable ex)
    {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
        logError(ex, ex.getLocalizedMessage());
    }

    public ApiError(HttpStatus status, String message, String debugMessage)
    {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = debugMessage;
        logErrorWithExceptionDetail(message, debugMessage);
    }

    public ApiError(HttpStatus status, ExceptionFormatter exceptionFormatter)
    {
        this();
        this.status = status;
        this.message = exceptionFormatter.getMessage();
        this.debugMessage = exceptionFormatter.getDebugMessage();
        logErrorWithExceptionDetail(this.message, this.debugMessage);
    }

    private void addSubError(ApiSubError subError)
    {
        if (subErrors == null)
        {
            subErrors = new ArrayList<>();
        }
        subErrors.add(subError);
    }

    private void addValidationError(String object, String field, Object rejectedValue, String message)
    {
        addSubError(new ApiValidationError(object, field, rejectedValue, message));
    }

    private void addValidationError(String object, String message)
    {
        addSubError(new ApiValidationError(object, message));
    }

    private void addValidationError(FieldError fieldError)
    {
        this.addValidationError(
            fieldError.getObjectName(),
            fieldError.getField(),
            fieldError.getRejectedValue(),
            fieldError.getDefaultMessage());
    }

    void addValidationErrors(List<FieldError> fieldErrors)
    {
        fieldErrors.forEach(this::addValidationError);
    }

    private void addValidationError(ObjectError objectError)
    {
        this.addValidationError(
            objectError.getObjectName(),
            objectError.getDefaultMessage());
    }

    void addValidationError(List<ObjectError> globalErrors)
    {
        globalErrors.forEach(this::addValidationError);
    }

    /**
     * Utility method for adding error of ConstraintViolation. Usually when a @Validated validation fails.
     *
     * @param cv the ConstraintViolation
     */
    private void addValidationError(ConstraintViolation<?> cv)
    {
        this.addValidationError(
            cv.getRootBeanClass().getSimpleName(),
            ((PathImpl) cv.getPropertyPath()).getLeafNode().asString(),
            cv.getInvalidValue(),
            cv.getMessage());
    }

    void addValidationErrors(Set<ConstraintViolation<?>> constraintViolations)
    {
        constraintViolations.forEach(this::addValidationError);
    }

    private static void logError(Throwable exception, String debugMessage)
    {
        String extendedDebugMessage = debugMessage + "|";
        Throwable cause = exception.getCause();
        if (cause != null)
        {
            extendedDebugMessage += "["+ cause.toString() + "]";
            StackTraceElement[] stackTraceElements = cause.getStackTrace();
            if (stackTraceElements != null && stackTraceElements.length > 0)
            {
                extendedDebugMessage += stackTraceElements[0];
            }
        }
        logErrorWithExceptionDetail(exception.getMessage(), extendedDebugMessage);
    }

    private static void logErrorWithExceptionDetail(String message, String exceptionDetail)
    {
        LOGGER.error(String.format(ERROR_MESSAGE_TO_LOG, message, exceptionDetail));
    }
}

class LowerCaseClassNameResolver extends TypeIdResolverBase
{
    @Override
    public String idFromValue(Object value)
    {
        return value.getClass().getSimpleName().toLowerCase();
    }

    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType)
    {
        return idFromValue(value);
    }

    @Override
    public JsonTypeInfo.Id getMechanism()
    {
        return JsonTypeInfo.Id.CUSTOM;
    }
}
