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
package uk.ac.ebi.impc_prod_tracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.impc_prod_tracker.conf.exeption_management.OperationFailedException;
import uk.ac.ebi.impc_prod_tracker.domain.login.AuthenticationRequest;
import uk.ac.ebi.impc_prod_tracker.service.AuthService;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

/**
 * Controller for end points related with the authentication process.
 *
 * @author Mauricio Martinez
 */
@RestController
@RequestMapping("/auth")
public class AuthController
{
    private static final String AUTHENTICATION_ERROR = "Invalid User/Password provided.";
    private AuthService authService;

    public AuthController( AuthService authService)
    {
        this.authService = authService;
    }

    /**
     * @api {post} /signin Signin a user to obtain a token.
     * @apiName Signin
     * @apiGroup User
     */
    @PostMapping(value = {"/signin"})
    public ResponseEntity signinReturningCookie(@RequestBody AuthenticationRequest authenticationRequest)
    {
        try
        {
            String username = authenticationRequest.getUsername();
            String token = authService.getAuthenticationToken(authenticationRequest);

            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("access_token", token);

            return ok(model);
        }
        catch (AuthenticationException e)
        {
            throw new OperationFailedException(AUTHENTICATION_ERROR);
        }
    }
}
