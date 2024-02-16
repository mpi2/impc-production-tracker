/*******************************************************************************
 * Copyright 2019 EMBL - European Bioinformatics Institute
 * <p>
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.
 *******************************************************************************/
package org.gentar.error_management;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.gentar.exceptions.ForbiddenAccessException;
import org.gentar.exceptions.InvalidRequestException;
import org.gentar.exceptions.NotFoundException;
import org.gentar.exceptions.OperationFailedException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * Central Exception handling. Taken from <a href="https://github.com/brunocleite/spring-boot-exception-handling">...</a>.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler
{
    @ExceptionHandler(OperationFailedException.class)
    public final ResponseEntity<Object> handleAccessDeniedException(
        OperationFailedException ex, WebRequest request)
    {
        return buildResponseEntity(ApiError.of(ex));
    }

    @ExceptionHandler(InvalidRequestException.class)
    public final ResponseEntity<Object> handleAccessDeniedException(
        InvalidRequestException ex, WebRequest request)
    {
        return buildResponseEntity(
            new ApiError(BAD_REQUEST, ex.getMessage(), "Please fix the request format and try again."));
    }

    @ExceptionHandler(AuthenticationException.class)
    public final ResponseEntity<Object> handleAccessDeniedException(
        AuthenticationException ex, WebRequest request)
    {
        return buildResponseEntity(
            new ApiError(UNAUTHORIZED, ex.getMessage(), "Please provide valid credentials."));
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<Object> handleAccessDeniedException(
        NotFoundException ex, WebRequest request)
    {
        return buildResponseEntity(
            new ApiError(NOT_FOUND, ex.getMessage(), "Please check the resource identifier."));
    }

    @ExceptionHandler(ForbiddenAccessException.class)
    public final ResponseEntity<Object> handleAccessDeniedException(
        ForbiddenAccessException ex, WebRequest request)
    {
        return buildResponseEntity(
            new ApiError(FORBIDDEN, ex.getMessage(), "Please check your permissions with the administrator."));
    }

    /**
     * Handle MissingServletRequestParameterException. Triggered when a 'required' request parameter is missing.
     *
     * @param ex      MissingServletRequestParameterException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
        MissingServletRequestParameterException ex,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request)
    {
        String error = ex.getParameterName() + " parameter is missing";
        return buildResponseEntity(new ApiError(BAD_REQUEST, error, ex));
    }


    /**
     * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid as well.
     *
     * @param ex      HttpMediaTypeNotSupportedException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
        HttpMediaTypeNotSupportedException ex,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        return buildResponseEntity(
            new ApiError(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), ex));
    }

    /**
     * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
     *
     * @param ex      the MethodArgumentNotValidException that is thrown when @Valid validation fails
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request)
    {
        ApiError apiError = new ApiError(BAD_REQUEST, ex);
        apiError.setMessage("Validation error");
        apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
        apiError.addValidationError(ex.getBindingResult().getGlobalErrors());

        return buildResponseEntity(apiError);
    }

    /**
     * Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails.
     *
     * @param ex the ConstraintViolationException
     * @return the ApiError object
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(
        ConstraintViolationException ex)
    {
        ApiError apiError = new ApiError(BAD_REQUEST, ex);
        apiError.setMessage("Validation error");
        apiError.addValidationErrors(ex.getConstraintViolations());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(TransactionSystemException.class)
    protected ResponseEntity<Object> handleConstraintViolation(
    TransactionSystemException ex)
    {
        ApiError apiError = new ApiError(BAD_REQUEST, ex);
        apiError.setMessage("Validation error");

        Throwable originalException = ex.getOriginalException();
        if (originalException != null)
        {
            Throwable cause = originalException.getCause();
            if (cause instanceof ConstraintViolationException constraintViolationException)
            {
                apiError.addValidationErrors(
                    constraintViolationException.getConstraintViolations());
            }
            else if (cause instanceof OperationFailedException)
            {
                return buildResponseEntity(ApiError.of((OperationFailedException)cause));
            }
        }
        return buildResponseEntity(apiError);
    }

    /**
     * Handles EntityNotFoundException. Created to encapsulate errors with more detail than
     * javax.persistence.EntityNotFoundException.
     *
     * @param ex the EntityNotFoundException
     * @return the ApiError object
     */
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(
        EntityNotFoundException ex) {
        ApiError apiError = new ApiError(NOT_FOUND, ex);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
     *
     * @param ex      HttpMessageNotReadableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
        HttpMessageNotReadableException ex,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request)
    {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        log.info("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());
        ApiError apiError;
        String error = "Malformed JSON request";
        if (ex.getCause() instanceof JsonParseException
            || ex.getCause() instanceof JsonMappingException)
        {
            apiError = new ApiError(BAD_REQUEST, new JsonPayloadExceptionFormatter(ex.getCause()));
        }
        else
        {
            apiError = new ApiError(BAD_REQUEST, error, ex);
        }
        return buildResponseEntity(apiError);
    }

    /**
     * Handle HttpMessageNotWritableException.
     *
     * @param ex      HttpMessageNotWritableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    protected ResponseEntity<Object> handleHttpMessageNotWritable(
        HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        String error = "Error writing JSON output";
        return buildResponseEntity(new ApiError(INTERNAL_SERVER_ERROR, error, ex));
    }

    /**
     * Handle NoHandlerFoundException.
     *
     * @param ex      HttpMessageNotWritableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    protected ResponseEntity<Object> handleNoHandlerFoundException(
        NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        ApiError apiError = new ApiError(BAD_REQUEST, ex);
        apiError.setMessage(
            String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));
        apiError.setDebugMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * Handle DataIntegrityViolationException, inspects the cause for different DB causes.
     *
     * @param ex the DataIntegrityViolationException
     * @return the ApiError object
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(
        DataIntegrityViolationException ex, WebRequest request)
    {
        ResponseEntity<Object> responseEntity = null;
        if (ex.getCause() instanceof ConstraintViolationException)
        {
            responseEntity =
                buildResponseEntity(new ApiError(HttpStatus.CONFLICT, "Database error", ex.getCause()));
        }
        else if(ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException)
        {
            responseEntity =
                buildResponseEntity(
                    new ApiError(HttpStatus.CONFLICT, new SqlExceptionFormatter(ex)));
        }
        return responseEntity;
    }

    /**
     * Handle Exception, handle generic Exception.class.
     *
     * @param ex the Exception.
     * @return the ApiError object.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(
        MethodArgumentTypeMismatchException ex,WebRequest request)
    {
        ApiError apiError = new ApiError(BAD_REQUEST, ex);
        String typeName = null;
        Class requiredType = ex.getRequiredType();
        if (requiredType != null)
        {
            typeName = requiredType.getSimpleName();
        }
        apiError.setMessage(
            String.format(
                "The parameter '%s' of value '%s' could not be converted to type '%s'",
                ex.getName(),
                ex.getValue(),
                typeName));
        apiError.setDebugMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError)
    {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
