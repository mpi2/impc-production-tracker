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

import lombok.Data;
import lombok.NonNull;

/**
 * Class to format an exception and get a suitable message and debugMessage values.
 */
@Data
public abstract class BaseExceptionFormatter implements ExceptionFormatter
{
    protected String message;
    protected String debugMessage;
    @NonNull
    protected Throwable exception;

    public BaseExceptionFormatter(Throwable exception)
    {
        this.exception = exception;
        message = exception.getMessage();
        debugMessage = null;
    }

    protected String formatString(String text)
    {
        String result;
        if (text == null)
        {
            result = null;
        }
        else
        {
            result = text.replaceAll("\n", "").trim();
            if (!result.endsWith("."))
            {
                result += ".";
            }
        }
        return result;
    }

    public String getMessage()
    {
        return formatString(message);
    }

    public String getDebugMessage()
    {
        return formatString(debugMessage);
    }
}
