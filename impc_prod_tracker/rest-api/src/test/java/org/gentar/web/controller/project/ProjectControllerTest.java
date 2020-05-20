package org.gentar.web.controller.project;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.gentar.biology.plan.PlanCommonDataDTO;
import org.gentar.biology.plan.PlanDTO;
import org.gentar.biology.plan.PlanMinimumCreationDTO;
import org.gentar.biology.project.ProjectCommonDataDTO;
import org.gentar.biology.project.ProjectCreationDTO;
import org.gentar.biology.project.ProjectDTO;
import org.gentar.framework.ControllerTestTemplate;
import org.gentar.framework.db.Paths;
import org.gentar.util.JsonHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.gentar.util.JsonHelper.toJson;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
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
        return document(
            "projects/getProject",
            responseFields(
                fieldWithPath("tpn").description("Public identifier for the project. Read only."),
                fieldWithPath("assignmentStatusName")
                    .description("Assignment Status for the project. It would be a conflict if " +
                        "existing projects are working in the same gene. Read only."),
                fieldWithPath("summaryStatusName")
                    .description("A status summarising the global status based on the statuses of " +
                        "the plans in the project. Read only."),
                fieldWithPath("reactivationDate")
                    .description("Date on which the project was activated again" +
                        "(assignment Status changed from inactive). Read only."),
                fieldWithPath("recovery").description("To be validated"),
                fieldWithPath("comment").description("Comment on this project."),
                fieldWithPath("relatedWorkUnitNames")
                    .description("Work units associated with the project."),
                fieldWithPath("relatedWorkGroupNames")
                    .description("Work groups associated with the project."),
                fieldWithPath("assignmentStatusStamps")
                    .description("Stamps for the changes of Assignment Status. Read only."),
                fieldWithPath("externalReference")
                    .description("External reference for the project. Read only."),
                fieldWithPath("projectIntentions")
                    .description("Intentions for the project"),
                fieldWithPath("privacyName")
                    .description("Privacy level for the project (public, protected or restricted)"),
                fieldWithPath("speciesNames")
                    .description("Species associated with the project."),
                fieldWithPath("consortia")
                    .description("Consortia associated with the project."),
                fieldWithPath("_links")
                    .description("Links for project"),
                fieldWithPath("_links.productionPlans")
                    .description("Links to production plans"),
                fieldWithPath("_links.productionPlans.href")
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
