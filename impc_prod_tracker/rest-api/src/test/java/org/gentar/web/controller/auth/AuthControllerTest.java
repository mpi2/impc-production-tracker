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
package org.gentar.web.controller.auth;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.gentar.framework.ControllerTestTemplate;
import org.gentar.framework.db.DBSetupFilesPaths;
import org.gentar.security.auth.AuthenticationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultHandler;

import static org.gentar.util.JsonHelper.toJson;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends ControllerTestTemplate
{
    private static final String USER_NOT_IN_AUTH_SYSTEM_MJE =
        "Invalid userName/password provided.";

    @Test
    @DatabaseSetup(DBSetupFilesPaths.SIGN_IN)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.SIGN_IN)
    public void testSignIn() throws Exception
    {
        AuthenticationRequest authenticationRequest =
            new AuthenticationRequest("gentar_test_user1@gentar.org", "gentar_test_user1");

        mvc().perform(post("/auth/signin")
            .content(toJson(authenticationRequest))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(containsString("\"accessToken\":")))
            .andExpect(status().isOk())
            .andDo(documentSignIn());
    }
    
    private ResultHandler documentSignIn()
    {
        ConstrainedFields fields = fields(AuthenticationRequest.class);
        return document(
            "auth/signin",
            requestFields(
                fields
                    .withPath("userName").description("The user name."),
                fields
                    .withPath("password").description("The password.")),
            responseFields(
                fields
                    .withPath("userName").description("User name in the system (usually the email)."),
                fields
                    .withPath("accessToken")
                    .description("The token to access the protected end points.")
                ));
    }

    @Test
    public void testSignInWhenUserNotInAuthenticationSystem()
    throws Exception
    {
        AuthenticationRequest authenticationRequest =
            new AuthenticationRequest("not-existing-user-auth-system", "password");
        mvc().perform(post("/auth/signin")
            .content(toJson(authenticationRequest))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(containsString(USER_NOT_IN_AUTH_SYSTEM_MJE)))
            .andExpect(status().is4xxClientError())
            .andDo(document("auth/signin/no-valid-user-password"));
    }
}
