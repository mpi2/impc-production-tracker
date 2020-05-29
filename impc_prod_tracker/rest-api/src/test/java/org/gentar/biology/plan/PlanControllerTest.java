package org.gentar.biology.plan;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.gentar.framework.ControllerTestTemplate;
import org.gentar.framework.TestResourceLoader;
import org.gentar.framework.db.DBSetupFilesPaths;
import org.gentar.util.JsonHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PlanControllerTest extends ControllerTestTemplate
{
    private static final String TEST_RESOURCES_FOLDER = INTEGRATION_TESTS_RESOURCE_PATH + "plans/";

    @BeforeEach
    public void setup() throws Exception
    {
        setTestUserSecurityContext();
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_PLANS)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_PLANS)
    void testGetOneCrisprPlan() throws Exception
    {
        ResultActions resultActions = mvc().perform(MockMvcRequestBuilders
            .get("/api/plans/PIN:0000000001")
            .header("Authorization", accessToken))
            .andExpect(status().isOk())
            .andDo(documentCrisprPlan());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        String expectedOutputAsString =
            loadExpectedResponseFromResource("expectedPlanGetPIN_0000000001.json");

        assertThat(
            JsonHelper.getJsonStringAsObject(contentAsString),
            is(JsonHelper.getJsonStringAsObject(expectedOutputAsString)));
    }

    private ResultHandler documentCrisprPlan()
    {
        List<FieldDescriptor> allFieldDescriptors = PlanFieldsDescriptors.getSharedFieldDescriptions();
        allFieldDescriptors.addAll(PlanFieldsDescriptors.getCrisprFieldDescriptors());
        return document("plans/getCrisprPlan", responseFields(allFieldDescriptors));
    }

    private String loadExpectedResponseFromResource(String resourceName)
        throws IOException
    {
        String completeResourcePath = TEST_RESOURCES_FOLDER + resourceName;
        return TestResourceLoader.loadJsonFromResource(completeResourcePath);
    }
}
