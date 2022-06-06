package org.gentar.biology.targ_rep.allele;

import static org.gentar.framework.targRep.constant.ParamConstant.ALLELE_TEST_ID;
import static org.gentar.framework.targRep.constant.UrlConstant.*;
import static org.gentar.framework.db.DBSetupFilesPaths.*;
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

class TargRepAlleleControllerTest extends ControllerTestTemplate {

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
    @DisplayName("Fetch All TargRep Alleles")
    void testFindAllTargRepAlleles() throws Exception {

        String contentAsString =
            restCaller.executeGetAndDocument( API_TARG_REP_ALLELES_URL,
                documentTargRepAllele("_embedded.targrep_alleles[].","allAlleles"));

        String expectedOutputAsString =
            loadExpectedResponseFromResource(EXPECTED_ALL_TARG_REP_ALLELES_JSON);

        JSONAssert.assertEquals(expectedOutputAsString, contentAsString, JSONCompareMode.STRICT);
    }

    @Test
    @DatabaseSetup(MULTIPLE_TARGREP)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = MULTIPLE_TARGREP)
    @DisplayName("Fetch TargRep Allele By ID")
    void testFindTargRepAlleleById() throws Exception {
        String contentAsString =
            restCaller
                .executeGetAndDocument(API_TARG_REP_ALLELES_URL + "/" + ALLELE_TEST_ID,
                    documentTargRepAllele("","alleleById"));

        String expectedOutputAsString =
            loadExpectedResponseFromResource(EXPECTED_TARG_REP_ALLELE_BY_ID_JSON);

        JSONAssert.assertEquals(expectedOutputAsString, contentAsString, JSONCompareMode.STRICT);

    }

    private ResultHandler documentTargRepAllele(String prefix,String endPoint) {
        List<FieldDescriptor> alleleFieldsDescriptions =
            AlleleFieldsDescriptors.getAlleleFieldsDescriptions(prefix);
        return document("targrep/allele/"+endPoint, responseFields(alleleFieldsDescriptions));
    }

    private String loadExpectedResponseFromResource(String resourceName)
        throws IOException {
        String completeResourcePath = TEST_RESOURCES_FOLDER + resourceName;
        return TestResourceLoader.loadJsonFromResource(completeResourcePath);
    }
}