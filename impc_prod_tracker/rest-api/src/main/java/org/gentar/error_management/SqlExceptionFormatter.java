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

import org.springframework.dao.DataIntegrityViolationException;

public class SqlExceptionFormatter extends BaseExceptionFormatter
{
    private static final String UNIQUE_CONSTRAINT_MESSAGE = "duplicate key value violates unique constraint";

    public SqlExceptionFormatter(Throwable exception)
    {
        super(exception);
        init();
    }

    private void init()
    {
        String rootCauseMessage = exception.getMessage();
        if (exception instanceof DataIntegrityViolationException)
        {
            Throwable rootCause = ((DataIntegrityViolationException) exception).getRootCause();
            if (rootCause != null)
            {
                rootCauseMessage = rootCause.getMessage();
            }
            if (rootCauseMessage.contains(UNIQUE_CONSTRAINT_MESSAGE))
            {
                processDuplicateKeyException(rootCauseMessage);
            }
        }
        message = formatString(message);
        debugMessage = formatString(debugMessage);
    }

    /**
     * DeliveryMethodType expecting an error like this:
     * ERROR: duplicate key value violates unique constraint "uk_2r45e34u6qbbksaobuopx32kh"
     *   Detail: Key (code)=(Code1) already exists.
     *   The message is of course jpa implementation (hibernate) specific, so this method will change if
     *   another implementation is chosen (or if hibernate changes the error message).
     * @param rootCauseMessage The complete exception message that contains all needed details.
     */
   private void processDuplicateKeyException(String rootCauseMessage)
    {
        int initIndex = rootCauseMessage.indexOf("Detail:");
        message = rootCauseMessage.substring(initIndex);
        debugMessage = rootCauseMessage.substring(0, initIndex - 1);
    }
}
