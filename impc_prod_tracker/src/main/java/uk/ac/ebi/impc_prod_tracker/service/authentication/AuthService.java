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
package uk.ac.ebi.impc_prod_tracker.service.authentication;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.impc_prod_tracker.conf.security.constants.PersonManagementConstants;
import uk.ac.ebi.impc_prod_tracker.domain.login.AuthenticationRequest;

/**
 * Service for authenticating a user against a authentication provider.
 *
 * @author Mauricio Martinez
 */
@Service
public class AuthService
{
    private RestTemplate restTemplate;

    public AuthService(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }

    /**
     * Return an authentication token if the user can be successfully logged into the local account at EBI AAP.
     * @param authenticationRequest Information to authenticate the user.
     * @return Token (jwt) if authenticated.
     */
    public String getAuthenticationToken(AuthenticationRequest authenticationRequest)
    {
        ResponseEntity<String> response;
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        try
        {
            response = restTemplate.exchange(
                PersonManagementConstants.LOCAL_AUTHENTICATION_URL, HttpMethod.GET, entity, String.class);
        }
        catch (HttpClientErrorException e)
        {
            throw new BadCredentialsException(e.getMessage());
        }
        return response.getBody();
    }
}
