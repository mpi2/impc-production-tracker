package org.gentar.biology.targ_rep.targeting_vector;

import static org.gentar.framework.db.DBSetupFilesPaths.MULTIPLE_TARGREP;
import static org.gentar.framework.targRep.constant.ParamConstant.TARGETING_VECTOR_TEST_ID;
import static org.gentar.framework.targRep.constant.UrlConstant.API_TARG_REP_TARGETING_VECTOR;
import static org.gentar.framework.targRep.constant.UrlConstant.EXPECTED_ALL_TARG_REP_TARGETING_VECTOR_JSON;
import static org.gentar.framework.targRep.constant.UrlConstant.EXPECTED_TARG_REP_TARGETING_VECTOR_BY_ID_JSON;
import static org.gentar.framework.targRep.constant.UrlConstant.TARG_REP;
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

class TargRepTargetingVectorControllerTest extends ControllerTestTemplate {

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
    @DisplayName("Find All TargRep Targeting Vectors")
    void findAllTargRepTargetingVector() throws Exception {
        String contentAsString =
            restCaller.executeGetAndDocument(API_TARG_REP_TARGETING_VECTOR,
                documentTargRepTargetingVector("_embedded.targrep_targeting_vectors[].","AllTargetingVector"));

        String expectedOutputAsString =
            loadExpectedResponseFromResource(EXPECTED_ALL_TARG_REP_TARGETING_VECTOR_JSON);

        JSONAssert.assertEquals(expectedOutputAsString, contentAsString, JSONCompareMode.STRICT);
    }


    @Test
    @DatabaseSetup(MULTIPLE_TARGREP)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = MULTIPLE_TARGREP)
    @DisplayName("Find TargRepTargetingVector By ID")
    void findTargRepTargetingVectorById() throws Exception {
        String contentAsString =
            restCaller.executeGetAndDocument(
                API_TARG_REP_TARGETING_VECTOR + "/" + TARGETING_VECTOR_TEST_ID,
                documentTargRepTargetingVector("","targetingVectorById"));

        String expectedOutputAsString =
            loadExpectedResponseFromResource(EXPECTED_TARG_REP_TARGETING_VECTOR_BY_ID_JSON);

        JSONAssert.assertEquals(expectedOutputAsString, contentAsString, JSONCompareMode.STRICT);

    }

    private ResultHandler documentTargRepTargetingVector(String prefix,String endPoint) {
        List<FieldDescriptor> targetingVectorFieldsDescriptions =
            TargetingVectorFieldsDescriptors.getTargetingVectorFieldsDescriptions(prefix);
        return document("targrep/targetingVector/"+endPoint,
            responseFields(targetingVectorFieldsDescriptions));
    }

    private String loadExpectedResponseFromResource(String resourceName)
        throws IOException {
        String completeResourcePath = TEST_RESOURCES_FOLDER + resourceName;
        return TestResourceLoader.loadJsonFromResource(completeResourcePath);
    }
}