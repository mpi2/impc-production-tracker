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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.NonNull;
import org.gentar.exceptions.ForbiddenAccessException;
import org.gentar.exceptions.InvalidRequestException;
import org.gentar.exceptions.NotFoundException;
import org.gentar.exceptions.OperationFailedException;
import org.gentar.exceptions.SystemOperationFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * This class controls exceptions that occur before even calling controllers, that is, catches exceptions that
 * are not managed by {@link RestExceptionHandler}.
 */

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter
{
    private final ObjectMapper mapper = new ObjectMapper();
    public ExceptionHandlerFilter()
    {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void doFilterInternal(
            @NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
    throws IOException
    {
        ApiError apiError;
        try
        {
            filterChain.doFilter(request, response);
        }
        catch (OperationFailedException ofe)
        {
            apiError = ApiError.of(ofe);
            addExceptionInfoToResponse(response, apiError);
        }
        catch (NotFoundException nfe)
        {
            apiError = new ApiError(HttpStatus.NOT_FOUND, nfe.getMessage(), "Please check the resource identifier.");
            addExceptionInfoToResponse(response, apiError);
        }
        catch (ForbiddenAccessException fae)
        {
            apiError = new ApiError(HttpStatus.FORBIDDEN, fae.getMessage(), "Please check your permissions with the administrator.");
            addExceptionInfoToResponse(response, apiError);
        }
        catch (InvalidRequestException ire)
        {
            apiError = new ApiError(HttpStatus.BAD_REQUEST, ire.getMessage(), "Please fix the request format and try again.");
            addExceptionInfoToResponse(response, apiError);
        }
        catch (RuntimeException e)
        {
            apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
            addExceptionInfoToResponse(response, apiError);
        }
        catch (Exception e)
        {
            Throwable cause = e.getCause();
            if (cause == null)
            {
                // Check if the exception itself is one we should handle
                apiError = ApiError.of(new SystemOperationFailedException(e));
            }
            else
            {
                apiError = switch (cause) {
                    case OperationFailedException ofe -> ApiError.of(ofe);
                    case NotFoundException nfe ->
                            new ApiError(HttpStatus.NOT_FOUND, nfe.getMessage(), "Please check the resource identifier.");
                    case ForbiddenAccessException fae ->
                            new ApiError(HttpStatus.FORBIDDEN, fae.getMessage(), "Please check your permissions with the administrator.");
                    case InvalidRequestException ire ->
                            new ApiError(HttpStatus.BAD_REQUEST, ire.getMessage(), "Please fix the request format and try again.");
                    default -> ApiError.of(new SystemOperationFailedException(cause));
                };
            }
            addExceptionInfoToResponse(response, apiError);
        }
    }

    private void addExceptionInfoToResponse(HttpServletResponse response, ApiError apiError)
    throws IOException
    {
        response.setStatus(apiError.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        ApiErrorResponse errorResponse = new ApiErrorResponse(apiError);
        PrintWriter printWriter = response.getWriter();
        printWriter.write(convertObjectToJson(errorResponse));
        printWriter.flush();
        printWriter.close();
    }

    public String convertObjectToJson(Object object)
    throws JsonProcessingException
    {
        if (object == null)
        {
            return null;
        }
        return mapper.writeValueAsString(object);
    }
}
