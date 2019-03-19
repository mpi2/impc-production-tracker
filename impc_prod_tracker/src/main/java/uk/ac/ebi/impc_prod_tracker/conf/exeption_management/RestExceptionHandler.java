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
package uk.ac.ebi.impc_prod_tracker.conf.exeption_management;

import com.fasterxml.jackson.core.JsonParseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
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
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Central Exception handling. Taken from https://github.com/brunocleite/spring-boot-exception-handling.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler
{
    /**
     * Handle MissingServletRequestParameterException. Triggered when a 'required' request parameter is missing.
     *
     * @param ex      MissingServletRequestParameterException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiException object
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
        MissingServletRequestParameterException ex,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request)
    {
        String error = ex.getParameterName() + " parameter is missing";
        return buildResponseEntity(new ApiException(BAD_REQUEST, error, ex));
    }


    /**
     * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid as well.
     *
     * @param ex      HttpMediaTypeNotSupportedException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiException object
     */
    @Override
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
            new ApiException(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), ex));
    }

    /**
     * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
     *
     * @param ex      the MethodArgumentNotValidException that is thrown when @Valid validation fails
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiException object
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request)
    {
        ApiException apiException = new ApiException(BAD_REQUEST);
        apiException.setMessage("Validation error");
        apiException.addValidationErrors(ex.getBindingResult().getFieldErrors());
        apiException.addValidationError(ex.getBindingResult().getGlobalErrors());

        return buildResponseEntity(apiException);
    }

    /**
     * Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails.
     *
     * @param ex the ConstraintViolationException
     * @return the ApiException object
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(
        ConstraintViolationException ex) {
        ApiException apiException = new ApiException(BAD_REQUEST);
        apiException.setMessage("Validation error");
        apiException.addValidationErrors(ex.getConstraintViolations());
        return buildResponseEntity(apiException);
    }

    @ExceptionHandler(TransactionSystemException.class)
    protected ResponseEntity<Object> handleConstraintViolation(
    TransactionSystemException ex)
    {
        ApiException apiException = new ApiException(BAD_REQUEST);
        apiException.setMessage("Validation error");

        if (ex.getOriginalException().getCause() instanceof ConstraintViolationException)
        {
            ConstraintViolationException constraintViolationException =
                (ConstraintViolationException)ex.getOriginalException().getCause();
            apiException.addValidationErrors(constraintViolationException.getConstraintViolations());
        }
        return buildResponseEntity(apiException);
    }

    /**
     * Handles EntityNotFoundException. Created to encapsulate errors with more detail than
     * javax.persistence.EntityNotFoundException.
     *
     * @param ex the EntityNotFoundException
     * @return the ApiException object
     */
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(
        EntityNotFoundException ex) {
        ApiException apiException = new ApiException(NOT_FOUND);
        apiException.setMessage(ex.getMessage());
        return buildResponseEntity(apiException);
    }

    /**
     * Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
     *
     * @param ex      HttpMessageNotReadableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiException object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
        HttpMessageNotReadableException ex,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request)
    {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        log.info("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());
        ApiException apiException = null;
        String error = "Malformed JSON request";
        if (ex.getCause() instanceof JsonParseException)
        {
            apiException = new ApiException(BAD_REQUEST, new JsonPayloadExceptionFormatter(ex.getCause()));
        }
        else
        {
            apiException = new ApiException(BAD_REQUEST, error, ex);
        }
        return buildResponseEntity(apiException);
    }

    /**
     * Handle HttpMessageNotWritableException.
     *
     * @param ex      HttpMessageNotWritableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiException object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(
        HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        String error = "Error writing JSON output";
        return buildResponseEntity(new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
    }

    /**
     * Handle NoHandlerFoundException.
     *
     * @param ex      HttpMessageNotWritableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiException object
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
        NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        ApiException apiException = new ApiException(BAD_REQUEST);
        apiException.setMessage(
            String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));
        apiException.setDebugMessage(ex.getMessage());
        return buildResponseEntity(apiException);
    }

    /**
     * Handle DataIntegrityViolationException, inspects the cause for different DB causes.
     *
     * @param ex the DataIntegrityViolationException
     * @return the ApiException object
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(
        DataIntegrityViolationException ex, WebRequest request)
    {
        ResponseEntity<Object> responseEntity = null;
        if (ex.getCause() instanceof ConstraintViolationException)
        {
            responseEntity =
                buildResponseEntity(new ApiException(HttpStatus.CONFLICT, "Database error", ex.getCause()));
        }
        else if(ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException)
        {
            responseEntity =
                buildResponseEntity(
                    new ApiException(HttpStatus.CONFLICT, new SqlExceptionFormatter(ex)));
        }
        return responseEntity;
    }

    /**
     * Handle Exception, handle generic Exception.class.
     *
     * @param ex the Exception.
     * @return the ApiException object.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(
        MethodArgumentTypeMismatchException ex,WebRequest request)
    {
        ApiException apiException = new ApiException(BAD_REQUEST);
        apiException.setMessage(
            String.format(
                "The parameter '%s' of value '%s' could not be converted to type '%s'",
                ex.getName(),
                ex.getValue(),
                ex.getRequiredType().getSimpleName()));
        apiException.setDebugMessage(ex.getMessage());
        return buildResponseEntity(apiException);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiException apiException)
    {
        return new ResponseEntity<>(apiException, apiException.getStatus());
    }
}
