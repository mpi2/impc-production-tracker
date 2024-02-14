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
package org.gentar.security.authentication;

import org.gentar.exceptions.CommonErrorMessages;
import org.gentar.exceptions.InvalidRequestException;
import org.springframework.stereotype.Service;

/**
 * Service for authenticating a user against a authentication provider.
 *
 * @author Mauricio Martinez
 */
@Service
public class AuthService
{
    private final AAPService aapService;

    public AuthService(AAPService aapService)
    {
        this.aapService = aapService;
    }

    /**
     * Return an authentication token if the user can be successfully logged into the local account at EBI AAP.
     * @param userName: User name.
     * @param password: Password.
     * @return Token (jwt) if authenticated.
     */
    public String getAuthenticationToken(String userName, String password)
    {
        validateNotNullCredentials(userName, password);
        return aapService.getToken(userName, password);
    }

    private void validateNotNullCredentials(String userName, String password)
    {
        if (userName == null)
        {
            String errorMessage = String.format(CommonErrorMessages.NULL_FIELD_ERROR, "userName");
            errorMessage += " Make sure the sent parameter is called 'userName' (case sensitive).";
            throw new InvalidRequestException(errorMessage);
        }
        if (password == null)
        {
            String errorMessage = String.format(CommonErrorMessages.NULL_FIELD_ERROR, "password");
            throw new InvalidRequestException(errorMessage);
        }
    }
}
