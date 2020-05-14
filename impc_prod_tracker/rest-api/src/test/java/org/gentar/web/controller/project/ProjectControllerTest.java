package org.gentar.web.controller.project;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.gentar.framework.ControllerTestTemplate;
import org.gentar.framework.db.Paths;
import org.gentar.security.auth.AuthenticationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProjectControllerTest extends ControllerTestTemplate
{
    @BeforeEach
    public void setup() throws Exception
    {
        setTestUserSecurityContext();
    }

    @Test
    @DatabaseSetup(Paths.MULTIPLE_PROJECTS)
    void testGetOneProject() throws Exception
    {
        mvc().perform(MockMvcRequestBuilders
            .get("/api/projects/TPN:01")
            .header("Authorization", accessToken))
            .andExpect(status().isOk())
            .andDo(documentSingleProject());
    }

    private ResultHandler documentSingleProject()
    {
        ConstrainedFields fields = fields(AuthenticationRequest.class);
        return document(
            "projects/getProject",
            responseFields(
                fields
                    .withPath("tpn").description("Public identifier for the project. Read only."),
                fields
                    .withPath("assignmentStatusName")
                    .description("Assignment Status for the project. It would be a conflict if " +
                        "existing projects are working in the same gene. Read only."),
                fields
                    .withPath("summaryStatusName")
                    .description("A status summarising the global status based on the statuses of " +
                        "the plans in the project. Read only."),
                fields
                    .withPath("reactivationDate")
                    .description("Date on which the project was activated again" +
                        "(assignment Status changed from inactive). Read only."),
                fields
                    .withPath("recovery").description("To be validated"),
                fields
                    .withPath("comment").description("Comment on this project."),
                fields
                    .withPath("relatedWorkUnitNames")
                    .description("Work units associated with the project."),
                fields
                    .withPath("relatedWorkGroupNames")
                    .description("Work groups associated with the project."),
                fields
                    .withPath("assignmentStatusStamps")
                    .description("Stamps for the changes of Assignment Status. Read only."),
                fields
                    .withPath("externalReference")
                    .description("External reference for the project. Read only."),
                fields
                    .withPath("projectIntentions")
                    .description("Intentions for the project"),
                fields
                    .withPath("privacyName")
                    .description("Privacy level for the project (public, protected or restricted)"),
                fields
                    .withPath("speciesNames")
                    .description("Species associated with the project."),
                fields
                    .withPath("consortia")
                    .description("Consortia associated with the project."),
                fields
                    .withPath("_links")
                    .description("Links for project"),
                fields
                    .withPath("_links.productionPlans")
                    .description("Links to production plans"),
                fields
                    .withPath("_links.productionPlans.href")
                    .description("Link to a specific production plan")
            ));
    }

    @Test
    @DatabaseSetup(Paths.MULTIPLE_PROJECTS)
    void testGetAllProjects() throws Exception
    {
        mvc().perform(MockMvcRequestBuilders
            .get("/api/projects")
            .header("Authorization", accessToken))
            .andExpect(status().isOk())
            .andDo(document("projects/allProjects"));
    }

    @Test
    @DatabaseSetup(Paths.MULTIPLE_PROJECTS)
    void testGetAllProjectsWithFilter() throws Exception
    {
        mvc().perform(MockMvcRequestBuilders
            .get("/api/projects?tpns=TPN:01,TPN:02")
            .header("Authorization", accessToken))
            .andExpect(status().isOk())
            .andDo(document("projects/allProjectsWithFilter"));
    }
}
