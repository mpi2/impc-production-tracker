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
package uk.ac.ebi.impc_prod_tracker.conf.error_management;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
            apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ofe.getMessage(), ofe.getDebugMessage());
            addExceptionInfoToResponse(response, apiError);
        }
        catch (RuntimeException e)
        {
            apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
            addExceptionInfoToResponse(response, apiError);
        }
        catch (Exception e)
        {
            if (e.getCause() instanceof OperationFailedException)
            {
                OperationFailedException ofe = (OperationFailedException) e.getCause();
                if (ofe.getHttpStatus() == null)
                {
                    apiError =
                        new ApiError(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            ofe.getMessage(),
                            ofe.getDebugMessage());
                }
                else
                {
                    apiError =
                        new ApiError(
                            ofe.getHttpStatus(),
                            ofe.getMessage(),
                            ofe.getDebugMessage());
                }
            }
            else
            {
                apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "");
            }
            addExceptionInfoToResponse(response, apiError);
        }
    }

    private void addExceptionInfoToResponse(HttpServletResponse response, ApiError apiError)
    throws IOException
    {
        response.setStatus(apiError.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON.toString());
        response.getWriter().write(convertObjectToJson(apiError));
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
