package org.gentar.biology.outcome;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.gentar.framework.ControllerTestTemplate;
import org.gentar.framework.SequenceResetter;
import org.gentar.framework.TestResourceLoader;
import org.gentar.framework.db.DBSetupFilesPaths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OutcomeControllerTest extends ControllerTestTemplate
{
    private static final String TEST_RESOURCES_FOLDER = INTEGRATION_TESTS_RESOURCE_PATH + "outcomes/";

    @Autowired
    private SequenceResetter sequenceResetter;

    @BeforeEach
    void setUp() throws Exception
    {
        setTestUserSecurityContext();
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_OUTCOMES)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_OUTCOMES)
    void testGetAllOutcomesInPlan() throws Exception
    {
        ResultActions resultActions = mvc().perform(MockMvcRequestBuilders
            .get("/api/plans/PIN:0000000001/outcomes")
            .header("Authorization", accessToken))
            .andExpect(status().isOk())
            .andDo(document("outcomes/allOutcomes"));

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        System.out.println(contentAsString);
        String expectedOutputAsString =
            loadExpectedResponseFromResource("expectedAllOutcomes.json");

        JSONAssert.assertEquals(expectedOutputAsString, contentAsString, JSONCompareMode.STRICT);
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_OUTCOMES)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_OUTCOMES)
    void testGetColonyOutcomeInPlan() throws Exception
    {
        ResultActions resultActions = mvc().perform(MockMvcRequestBuilders
            .get("/api/plans/PIN:0000000001/outcomes/TPO:000000000001")
            .header("Authorization", accessToken))
            .andExpect(status().isOk())
            .andDo(document("outcomes/allOutcomes"));

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        System.out.println(contentAsString);
        String expectedOutputAsString =
            loadExpectedResponseFromResource("expectedColonyOutcomeTPO_000000000001.json");

        JSONAssert.assertEquals(expectedOutputAsString, contentAsString, JSONCompareMode.STRICT);
    }

    private String loadExpectedResponseFromResource(String resourceName)
        throws IOException
    {
        String completeResourcePath = TEST_RESOURCES_FOLDER + resourceName;
        return TestResourceLoader.loadJsonFromResource(completeResourcePath);
    }
}