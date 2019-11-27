/******************************************************************************
 Copyright 2019 EMBL - European Bioinformatics Institute

 Licensed under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License. You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 either express or implied. See the License for the specific
 language governing permissions and limitations under the
 License.
 */
package org.gentar.biology.gene;

import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

/**
 * Checks if the identifier of a gene used in a search is valid.
 */
@Component
public class GeneIdentifierValidator
{
    private static final String ACCESSION_ID_IDENTIFIER = ":";

    /**
     * Checks if an identifier for a gene to be searched is valid and its significant characters
     * have a length greater than minimumLength.
     * @param input The symbol or the accession id in the gene.
     * @param minimumLength The minimum length for the significant characters in the symbol or
     *                      accession id.
     */
    public void validateIdentifier(String input, int minimumLength)
    {
        if (!hasValidLength(input, minimumLength))
        {
             throw new UserOperationFailedException("The input is too short.");
        }
    }

    private boolean hasValidLength(String input, int minimumLength)
    {
        String significantCharacters = getSignificantCharacters(input);
        return significantCharacters.length() >=  minimumLength;
    }

    private String getSignificantCharacters(String input)
    {
        String significantCharacters;
        if (isAccessionId(input))
        {
            significantCharacters = input.substring(input.indexOf(ACCESSION_ID_IDENTIFIER) + 1);
        }
        else
        {
            significantCharacters = input;
        }
        if (significantCharacters.endsWith("%"))
        {
            significantCharacters =
                significantCharacters.substring(0, significantCharacters.length() - 1);
        }
        return significantCharacters;
    }

    private boolean isAccessionId(String input)
    {
        return input.contains(ACCESSION_ID_IDENTIFIER);
    }
}
