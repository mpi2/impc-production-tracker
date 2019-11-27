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
package org.gentar.exceptions;

import lombok.Getter;
import lombok.Setter;

/**
 * Class to manage the Exceptions in the application
 *
 * @author Mauricio Martinez
 */
@Getter
@Setter
public abstract class OperationFailedException extends RuntimeException
{
    //protected HttpStatus httpStatus;

    public OperationFailedException(String message)
    {
        super(message);
    }
}
