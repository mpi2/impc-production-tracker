package org.gentar.biology.targ_rep.pipeline;

import static org.gentar.framework.targRep.constant.ParamConstant.PIPELINE_TEST_ID;
import static org.gentar.framework.targRep.constant.UrlConstant.*;
import static org.gentar.framework.db.DBSetupFilesPaths.MULTIPLE_TARGREP;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import java.io.IOException;
import java.util.List;
import org.gentar.framework.ControllerTestTemplate;
import org.gentar.framework.RestCaller;
import org.gentar.framework.ResultValidator;
import org.gentar.framework.TestResourceLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.ResultHandler;

class TargRepPipelineControllerTest extends ControllerTestTemplate {

    private static final String TEST_RESOURCES_FOLDER =
        INTEGRATION_TESTS_RESOURCE_PATH + TARG_REP;


    private ResultValidator resultValidator;
    private RestCaller restCaller;


    @BeforeEach
    public void setup() throws Exception {
        setTestUserSecurityContext();
        resultValidator = new ResultValidator();
        restCaller = new RestCaller(mvc(), accessToken);
    }


    @Test
    @DatabaseSetup(MULTIPLE_TARGREP)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = MULTIPLE_TARGREP)
    @DisplayName("Find All TargRep Pipelines")
    void findAllTargRepPipelines() throws Exception {
        String contentAsString =
            restCaller.executeGetAndDocument(API_TARG_REP_PIPELINES,
                documentTargRepPipeline("_embedded.targrep_pipelines[].","allPipelines"));

        String expectedOutputAsString =
            loadExpectedResponseFromResource(EXPECTED_ALL_TARG_REP_PIPELINES_JSON);

        JSONAssert.assertEquals(expectedOutputAsString, contentAsString, JSONCompareMode.STRICT);

    }

    @Test
    @DatabaseSetup(MULTIPLE_TARGREP)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = MULTIPLE_TARGREP)
    @DisplayName("Find TargRep By Pipeline ID")
    void findTargRepPipelineById() throws Exception {
        String contentAsString =
            restCaller.executeGetAndDocument(API_TARG_REP_PIPELINES + "/" + PIPELINE_TEST_ID,
                documentTargRepPipeline("","pipelineById"));

        String expectedOutputAsString =
            loadExpectedResponseFromResource(EXPECTED_TARG_REP_PIPELINE_BY_ID_JSON);

        JSONAssert.assertEquals(expectedOutputAsString, contentAsString, JSONCompareMode.STRICT);

    }

    private ResultHandler documentTargRepPipeline(String prefix, String endpoint)  {
        List<FieldDescriptor> pipelineFieldsDescriptions =
            PipelineFieldsDescriptors.getPipelineFieldsDescriptions(prefix);
        return document("targrep/pipeline/"+endpoint, responseFields(pipelineFieldsDescriptions));
    }

    private String loadExpectedResponseFromResource(String resourceName)
        throws IOException {
        String completeResourcePath = TEST_RESOURCES_FOLDER + resourceName;
        return TestResourceLoader.loadJsonFromResource(completeResourcePath);
    }
}