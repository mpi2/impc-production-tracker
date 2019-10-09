package uk.ac.ebi.impc_prod_tracker.web.controller.auth;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultHandler;
import uk.ac.ebi.impc_prod_tracker.domain.login.AuthenticationRequest;
import uk.ac.ebi.impc_prod_tracker.framework.ControllerTestTemplate;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.ac.ebi.impc_prod_tracker.common.json.JsonHelper.toJson;

public class AuthControllerTest extends ControllerTestTemplate
{
    private static final String USER_NOT_IN_AUTH_SYSTEM_MJE =
        "Invalid User/Password provided.";
    private static final String USER_NOT_IN_DB =
        "There is not associated information in the system for the user";

    @Test
    @DatabaseSetup("/dbunit/auth/createUser.xml")
    public void testSignIn() throws Exception
    {
        AuthenticationRequest authenticationRequest =
            new AuthenticationRequest("imits2test", "imits2test");

        mvc().perform(post("/auth/signin")
            .content(toJson(authenticationRequest))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(containsString("\"accessToken\":")))
            .andExpect(status().isOk())
            .andDo(documentSignIn());
    }
    
    private ResultHandler documentSignIn() {
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
            .andExpect(status().is5xxServerError())
            .andDo(document("auth/signin/no-valid-user-password"));
    }

    @Test
    public void testSignUp()
    {
    }
}