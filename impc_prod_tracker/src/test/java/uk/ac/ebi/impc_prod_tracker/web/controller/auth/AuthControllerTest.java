package uk.ac.ebi.impc_prod_tracker.web.controller.auth;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultHandler;
import uk.ac.ebi.impc_prod_tracker.data.organization.person.Person;
import uk.ac.ebi.impc_prod_tracker.data.organization.person.PersonRepository;
import uk.ac.ebi.impc_prod_tracker.domain.login.AuthenticationRequest;
import uk.ac.ebi.impc_prod_tracker.framework.ControllerTestTemplate;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.ac.ebi.impc_prod_tracker.framework.JsonHelper.toJson;

public class AuthControllerTest extends ControllerTestTemplate
{
    private static final String USER_NOT_IN_AUTH_SYSTEM_MJE =
        "Invalid User/Password provided.";
    private static final String USER_NOT_IN_DB =
        "There is not associated information in the system for the user";
    @Autowired
    private PersonRepository personRepository;

    @Test
    @DatabaseSetup("/dbunit/auth/createUser.xml")
    public void testSignIn() throws Exception
    {
        AuthenticationRequest authenticationRequest =
            new AuthenticationRequest("imits2test", "imits2test");
        List<Person> people = personRepository.findAll();

        mvc().perform(post("/auth/signin")
            .content(toJson(authenticationRequest))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(containsString("\"access_token\":")))
            .andExpect(status().isOk())
            .andDo(documentSignIn());
    }

    private ResultHandler documentSignIn() {
        ConstrainedFields fields = fields(AuthenticationRequest.class);
        return document(
            "auth/signin",
            requestFields(
                fields
                    .withPath("username").description("The user name"),
                fields
                    .withPath("password").description("The password")),
            responseFields(
                fields
                    .withPath("access_token")
                    .description("The token to access the protected end points"),
                fields
                    .withPath("workUnitName")
                    .description("Work unit the user belongs to"),
                fields
                    .withPath("role")
                    .description("Role the user has assigned"),
                fields
                    .withPath("username")
                    .description("User name in the system (usually the email)")
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
    @DatabaseSetup("/dbunit/auth/emptyUser.xml")
    public void testSignInWhenUserNotInDataBase()
        throws Exception
    {
        AuthenticationRequest authenticationRequest =
            new AuthenticationRequest("imits2test", "imits2test");
        mvc().perform(post("/auth/signin")
            .content(toJson(authenticationRequest))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(containsString(USER_NOT_IN_DB)))
            .andExpect(status().is5xxServerError())
            .andDo(document("auth/signin/no-info-for-user"));
    }

    @Test
    public void testSignUp()
    {
    }
}