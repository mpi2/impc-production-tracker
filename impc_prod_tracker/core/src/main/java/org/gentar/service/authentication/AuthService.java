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
package org.gentar.service.authentication;

import org.gentar.service.conf.AAPService;
import org.springframework.stereotype.Service;

/**
 * Service for authenticating a user against a authentication provider.
 *
 * @author Mauricio Martinez
 */
@Service
public class AuthService
{
    private AAPService aapService;

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
        return aapService.getToken(userName, password);
    }
}
