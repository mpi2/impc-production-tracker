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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.gentar.exceptions.OperationFailedException;
import org.gentar.exceptions.SystemOperationFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * This class controls exceptions that occur before even calling controllers, that is, catches exceptions that
 * are not managed by {@link RestExceptionHandler}.
 */

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter
{
    private ObjectMapper mapper = new ObjectMapper();
    public ExceptionHandlerFilter()
    {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void doFilterInternal(
        HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
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
                apiError = ApiError.of(new SystemOperationFailedException(e));
            }
            else
            {
                if (cause instanceof OperationFailedException)
                {
                    OperationFailedException ofe = (OperationFailedException) cause;
                    apiError = ApiError.of(ofe);
                }
                else
                {
                    apiError = ApiError.of(new SystemOperationFailedException(cause));
                }
            }
            addExceptionInfoToResponse(response, apiError);
        }
    }

    private void addExceptionInfoToResponse(HttpServletResponse response, ApiError apiError)
    throws IOException
    {
        response.setStatus(apiError.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        PrintWriter printWriter = response.getWriter();
        printWriter.write(convertObjectToJson(apiError));
        if (printWriter != null) {
            printWriter.flush();
            printWriter.close();
        }
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
