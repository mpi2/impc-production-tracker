package org.gentar.biology.targ_rep.es_cell;

import static org.gentar.framework.db.DBSetupFilesPaths.MULTIPLE_TARGREP;
import static org.gentar.framework.targRep.constant.ParamConstant.ES_CELL_TEST_ID;
import static org.gentar.framework.targRep.constant.ParamConstant.ES_CELL_TEST_NAME;
import static org.gentar.framework.targRep.constant.ParamConstant.ES_CELL_TEST_SYMBOL;
import static org.gentar.framework.targRep.constant.UrlConstant.*;
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


class TargRepEsCellControllerTest extends ControllerTestTemplate {

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
    @DisplayName("Find TargRep ES Cell By Gene Symbol")
    void findEsCellByGene() throws Exception {
        String contentAsString =
            restCaller.executeGetAndDocument(API_TARG_REP_ES_CELL_BY_SYMBOL + "/" +
                ES_CELL_TEST_SYMBOL, documentTargRepEsCell( "_embedded.targrep_es_cells[].","es_cell_by_symbol"));

        String expectedOutputAsString =
            loadExpectedResponseFromResource(EXPECTED_TARG_REP_ES_CELL_BY_GENE_SYMBOL_JSON);

        JSONAssert.assertEquals(expectedOutputAsString, contentAsString, JSONCompareMode.STRICT);
    }

    @Test
    @DatabaseSetup(MULTIPLE_TARGREP)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = MULTIPLE_TARGREP)
    @DisplayName("Find TargRep ES Cell By Name")
    void findEsCellByName() throws Exception {
        String contentAsString =
            restCaller.executeGetAndDocument(API_TARG_REP_ES_CELL_BY_NAME + "/" + ES_CELL_TEST_NAME,
                    documentTargRepEsCell( "","es_cell_by_name"));

        String expectedOutputAsString =
            loadExpectedResponseFromResource(EXPECTED_TARG_REP_ES_CELL_BY_ID_JSON);

        JSONAssert.assertEquals(expectedOutputAsString, contentAsString, JSONCompareMode.STRICT);
    }

    @Test
    @DatabaseSetup(MULTIPLE_TARGREP)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = MULTIPLE_TARGREP)
    @DisplayName("Fetch All TargRep EsCells")
    void testFindAllTargRepAEsCells() throws Exception {

        String contentAsString =
            restCaller.executeGetAndDocument(API_TARG_REP_ES_CELL_URL,
                documentTargRepEsCell("_embedded.targrep_es_cells[].","allEsCells", true));

        String expectedOutputAsString =
            loadExpectedResponseFromResource(EXPECTED_ALL_TARG_REP_ES_CELLS_JSON);

        JSONAssert.assertEquals(expectedOutputAsString, contentAsString, JSONCompareMode.STRICT);
    }

    @Test
    @DatabaseSetup(MULTIPLE_TARGREP)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = MULTIPLE_TARGREP)
    @DisplayName("Fetch TargRep EsCell By ID")
    void testFindTargRepEsCellById() throws Exception {
        String contentAsString =
            restCaller
                .executeGetAndDocument(API_TARG_REP_ES_CELL_URL + "/" + ES_CELL_TEST_ID,
                    documentTargRepEsCell("","EsCellById"));

        String expectedOutputAsString =
            loadExpectedResponseFromResource(EXPECTED_TARG_REP_ES_CELL_BY_ID_JSON);

        JSONAssert.assertEquals(expectedOutputAsString, contentAsString, JSONCompareMode.STRICT);

    }

    private ResultHandler documentTargRepEsCell(String prefix, String endpoint) {
        return documentTargRepEsCell(prefix, endpoint, false);
    }

    private ResultHandler documentTargRepEsCell(String prefix, String endpoint, boolean includePagination) {
        List<FieldDescriptor> esCellFieldsDescriptions =
            EsCellFieldsDescriptors.getEsCellFieldsDescriptions(prefix, includePagination);
        return document("targrep/esCell/" + endpoint, responseFields(esCellFieldsDescriptions));
    }

    private String loadExpectedResponseFromResource(String resourceName)
        throws IOException {
        String completeResourcePath = TEST_RESOURCES_FOLDER + resourceName;
        return TestResourceLoader.loadJsonFromResource(completeResourcePath);
    }
}