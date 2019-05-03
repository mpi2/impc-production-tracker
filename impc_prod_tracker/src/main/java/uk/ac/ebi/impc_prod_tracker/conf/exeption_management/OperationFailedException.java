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

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Class to manage the Exceptions in the application
 *
 * @author Mauricio Martinez
 */
@Getter
public class OperationFailedException extends RuntimeException
{
    private String debugMessage;
    private Throwable cause;
    private HttpStatus httpStatus;

    public OperationFailedException(String message)
    {
        super(message);
    }

    public OperationFailedException(String message, HttpStatus httpStatus)
    {
        super(message);
        this.httpStatus = httpStatus;
    }

    public OperationFailedException(String message, Throwable cause)
    {
        this(message);
        this.cause = cause;
        this.debugMessage = cause.getLocalizedMessage();
    }

    public OperationFailedException(String message, String debugMessage)
    {
        this(message);
        this.debugMessage = debugMessage;
    }
}
