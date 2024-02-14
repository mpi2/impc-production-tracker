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

/**
 * Class to format Json related exceptions.
 */
public class JsonPayloadExceptionFormatter extends BaseExceptionFormatter
{

    private static final String UNRECOGNIZED_TOKEN = "Unrecognized token";
    private static final String UNEXPECTED_CHARACTER = "Unexpected character";
    private static final String PARSE_ERROR_END = ": was expecting";
    private static final String MESSAGE = "The Json could not be parsed.";
    private static final String DEBUG_MESSAGE = "Please verify that the values that are string are not lacking " +
        "quotation marks, and that the numeric fields have valid values.";

    public JsonPayloadExceptionFormatter(Throwable exception)
    {
        super(exception);
        init();
    }

    private void init()
    {
        String rootCauseMessage = exception.getMessage();
        if (exception instanceof JsonParseException || exception instanceof JsonMappingException)
        {
            message = MESSAGE;
            if (rootCauseMessage.contains(UNRECOGNIZED_TOKEN))
            {
                processJsonParseExceptionByUnrecognizedToken(rootCauseMessage);
            }
            else if (rootCauseMessage.contains(UNEXPECTED_CHARACTER))
            {
                processJsonParseExceptionByUnexpectedCharacter(rootCauseMessage);
            }
        }
    }

    /**
     * Formats a message that looks like:
     * com.fasterxml.jackson.core.JsonParseException: Unrecognized token 'sa': was expecting ('true', 'false' or 'null')
     *  at [Source: (org.apache.catalina.connector.CoyoteInputStream);
     * @param completeMessage The exception message.
     */
    private void processJsonParseExceptionByUnrecognizedToken(String completeMessage)
    {
        int indexInit = completeMessage.indexOf(UNRECOGNIZED_TOKEN);
        int indexEnd = completeMessage.indexOf(PARSE_ERROR_END);
        debugMessage = completeMessage.substring(indexInit, indexEnd) + ". " + DEBUG_MESSAGE;
    }

    /**
     * Formats a message that looks like:
     * com.fasterxml.jackson.core.JsonParseException:Unexpected character ('5' (code 53)): was expecting
     *  double-quote to start field name
     *  at [Source: (org.apache.catalina.connector.CoyoteInputStream); line: 4, column: 13]
     * @param completeMessage The exception message.
     */
    private void processJsonParseExceptionByUnexpectedCharacter(String completeMessage)
    {
        int indexInit = completeMessage.indexOf(UNEXPECTED_CHARACTER);
        int indexEnd = completeMessage.indexOf(PARSE_ERROR_END);
        debugMessage = completeMessage.substring(indexInit, indexEnd) + ". " + DEBUG_MESSAGE;
    }
}
