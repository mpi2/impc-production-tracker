package org.gentar.biology.plan;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.gentar.framework.ControllerTestTemplate;
import org.gentar.framework.db.DBSetupFilesPaths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
            .andExpect(status().isOk());
        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        System.out.println(contentAsString);
    }
}