/*****************************************************************************
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
package org.gentar.security.auth;

import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.security.authentication.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

/**
 * Controller for end points related with the auth process.
 *
 * @author Mauricio Martinez
 */
@RestController
@RequestMapping("/auth")
public class AuthController
{
    private static final String AUTHENTICATION_ERROR = "Invalid User/Password provided.";
    private final AuthService authService;

    public AuthController(AuthService authService)
    {
        this.authService = authService;
    }

    /**
     * Signin a user to obtain a token and some basic information about the user.
     * @param authenticationRequest Object with the user's credentials.
     * @return Token if authenticated, as well as basic information like role and workUnitName.
     */
    @PostMapping(value = {"/signin"})
    public ResponseEntity signIn(@RequestBody AuthenticationRequest authenticationRequest)
    {
        try
        {
            String username = authenticationRequest.getUserName();
            String token = authService.getAuthenticationToken(
                authenticationRequest.getUserName(), authenticationRequest.getPassword());
            AuthenticationResponseDTO authenticationResponseDTO = buildAuthenticationResponseDTO(
                username, token);

            return ok(authenticationResponseDTO);
        }
        catch (AuthenticationException e)
        {
            throw new UserOperationFailedException(AUTHENTICATION_ERROR);
        }
    }

    private AuthenticationResponseDTO buildAuthenticationResponseDTO(String userName, String token)
    {
        AuthenticationResponseDTO authenticationResponseDTO = new AuthenticationResponseDTO();
        authenticationResponseDTO.setUserName(userName);
        authenticationResponseDTO.setAccessToken(token);
        return authenticationResponseDTO;
    }

}
