package org.gentar.framework;

import org.gentar.security.abac.subject.SystemSubject;
import org.gentar.security.auth.AuthenticationRequest;
import org.gentar.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Collections;
import java.util.Properties;

import static org.gentar.util.JsonHelper.toJson;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@Tag(ControllerTestTemplate.TAG)
public class ControllerTestTemplate extends IntegrationTestTemplate
{
    @Autowired private JwtTokenProvider jwtTokenProvider;
    public static final String TAG = "ControllerTest";

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext applicationContext;

    protected Authentication authenticationForTestUser = null;
    protected String accessToken = null;
    protected static final String INTEGRATION_TESTS_RESOURCE_PATH = "/integration-tests/";

    @BeforeEach
    public void setup(RestDocumentationContextProvider restDocumentation) throws Exception
    {
        MockitoAnnotations.initMocks(this);
        mvc =
            MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
                .build();
    }

    protected void setTestUserSecurityContext() throws Exception
    {
        if (accessToken == null)
        {
            accessToken = getAccessTokenForTestsUser();
        }
        if (authenticationForTestUser == null)
        {
            SystemSubject systemSubject = jwtTokenProvider.getSystemSubject(accessToken);
            Authentication auth = new UsernamePasswordAuthenticationToken(systemSubject, "", null);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> ConstrainedFields<T> fields(Class<T> clazz) {
        return new ConstrainedFields(clazz);
    }

    public static class ConstrainedFields<T> {

        private final ConstraintDescriptions constraintDescriptions;
        private final Properties customValidationDescription = new Properties();

        ConstrainedFields(Class<T> input) {

            try {
                this.constraintDescriptions = new ConstraintDescriptions(input);
                this.customValidationDescription.load(
                    getClass().getResourceAsStream("CustomValidationDescription.properties"));
            } catch (IOException e) {
                throw new IllegalArgumentException(
                    "unable to load properties for custom validation description");
            }
        }

        public FieldDescriptor withPath(String path) {
            return fieldWithPath(path)
                .attributes(
                    key("constraints")
                        .value(
                            StringUtils.collectionToDelimitedString(
                                this.constraintDescriptions.descriptionsForProperty(path), ". ")));
        }

        /** Returns field descriptor for custom validators. */
        public FieldDescriptor withCustomPath(String path) {
            return fieldWithPath(path)
                .attributes(
                    key("constraints")
                        .value(
                            StringUtils.collectionToDelimitedString(
                                Collections.singletonList(customValidationDescription.getProperty(path)),
                                ". ")));
        }
    }

    /**
     * Wraps the static document() method of RestDocs and configures it to pretty print request and
     * response JSON structures.
     */
    protected RestDocumentationResultHandler document(String identifier, Snippet... snippets) {
        return MockMvcRestDocumentation.document(
            identifier, preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), snippets);
    }

    protected MockMvc mvc() {
        return mvc;
    }

    protected String getAccessTokenForTestsUser() throws Exception
    {

        AuthenticationRequest authenticationRequest =
            new AuthenticationRequest("gentar_test_user1@gentar.org", "gentar_test_user1");
        ResultActions result
            = mvc().perform(post("/auth/signin")
            .content(toJson(authenticationRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept("application/json;charset=UTF-8"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"));
        String resultString = result.andReturn().getResponse().getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("accessToken").toString();
    }
}
