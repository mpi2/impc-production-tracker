package org.gentar.framework;

import org.gentar.security.auth.AuthenticationRequest;
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
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
    public static final String TAG = "ControllerTest";

    private MockMvc mvc;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private WebApplicationContext applicationContext;

    //@Autowired private SequenceResetter sequenceResetter;

    @BeforeEach
    public void setup(RestDocumentationContextProvider restDocumentation)
    {
        MockitoAnnotations.initMocks(this);
        mvc =
            MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
                .build();
    }

//    @AfterEach
//    public void reset() {
//        sequenceResetter.resetSequences(
//            "seq_proj_id",
//            "seq_comm_id",
//            "seq_acon_id",
//            "seq_acof_id",
//            "seq_ajob_id",
//            "seq_fiid_id",
//            "seq_file_id",
//            "seq_fpat_id",
//            "seq_modu_id",
//            "seq_glen_id",
//            "seq_job_id",
//            "seq_qpme_id",
//            "seq_user_id",
//            "seq_reto_id");
//    }

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

    /*protected JsonPathResponseFieldsSnippet responseFieldsInPath(
        String jsonPath, FieldDescriptor... fieldDescriptors) {
        return new JsonPathResponseFieldsSnippet(jsonPath, fieldDescriptors);
    }

    protected LinksSnippet linksInPath(String jsonPath, LinkDescriptor... linkDescriptors) {
        return new JsonPathLinksSnippet(jsonPath, linkDescriptors);
    }*/

    protected String getAccessTokenForTestsUser() throws Exception
    {

        AuthenticationRequest authenticationRequest =
            new AuthenticationRequest("imits2test", "imits2test");
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
