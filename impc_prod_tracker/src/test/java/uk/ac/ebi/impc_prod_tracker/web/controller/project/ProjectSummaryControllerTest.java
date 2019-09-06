package uk.ac.ebi.impc_prod_tracker.web.controller.project;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Test;
import uk.ac.ebi.impc_prod_tracker.framework.ControllerTestTemplate;
import uk.ac.ebi.impc_prod_tracker.web.db.Paths;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProjectSummaryControllerTest extends ControllerTestTemplate
{
    @Test
    @DatabaseSetup(Paths.MULTIPLE_PROJECTS)
    public void testGetProjectSummaries()
    throws Exception
    {
        // TODO: Fix when new project structure finishes.
//        mvc().perform(get("/api/projectSummaries"))
//            .andExpect(status().isOk())
//            .andDo(document("projectSummaries"));
    }
}