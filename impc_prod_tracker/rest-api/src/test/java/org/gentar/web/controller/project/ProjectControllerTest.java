package org.gentar.web.controller.project;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.gentar.biology.plan.PlanCommonDataDTO;
import org.gentar.biology.plan.PlanDTO;
import org.gentar.biology.plan.PlanMinimumCreationDTO;
import org.gentar.biology.project.ProjectCommonDataDTO;
import org.gentar.biology.project.ProjectCreationDTO;
import org.gentar.biology.project.ProjectDTO;
import org.gentar.biology.project.ProjectResponseDTO;
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
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = Paths.MULTIPLE_PROJECTS)
    void testGetOneProject() throws Exception
    {
        ResultActions resultActions = mvc().perform(MockMvcRequestBuilders
            .get("/api/projects/TPN:01")
            .header("Authorization", accessToken))
            .andExpect(status().isOk())
            .andDo(documentSingleProject());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        ProjectResponseDTO projectResponseDTO =
            JsonHelper.fromJson(contentAsString, ProjectResponseDTO.class);
        System.out.println(projectResponseDTO);
        assertThat(projectResponseDTO.getTpn(), is("TPN:01"));
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
                fieldWithPath("projectIntentions[].molecularMutationTypeName")
                    .description("Name of thr molecular mutation."),
                fieldWithPath("projectIntentions[].mutationCategorizations")
                    .description("Mutation categorizations linked to the project intention."),
                fieldWithPath("projectIntentions[].mutationCategorizations[].name")
                    .description("Name of the mutation categorization."),
                fieldWithPath("projectIntentions[].mutationCategorizations[].description")
                    .description("Description of the mutation categorization."),
                fieldWithPath("projectIntentions[].mutationCategorizations[].typeName")
                    .description("Name of type of the mutation categorization."),
                fieldWithPath("projectIntentions[].intentionByGene")
                    .description("Gene in the intention."),
                fieldWithPath("projectIntentions[].intentionByGene.bestOrthologs[]")
                    .description("A list of best orthologs for the gene (Support count > 4)."),
                fieldWithPath("projectIntentions[].intentionByGene.allOrthologs[]")
                    .description("A list of all orthologs for the gene."),
                fieldWithPath("projectIntentions[].intentionByGene.gene")
                    .description("Gene information."),
                fieldWithPath("projectIntentions[].intentionByGene.gene.id")
                    .description("Internal id of the gene in GenTaR."),
                fieldWithPath("projectIntentions[].intentionByGene.gene.name")
                    .description("Name of the gene."),
                fieldWithPath("projectIntentions[].intentionByGene.gene.symbol")
                    .description("Symbol of the gene."),
                fieldWithPath("projectIntentions[].intentionByGene.gene.externalLink")
                    .description("External link for the gene"),
                fieldWithPath("projectIntentions[].intentionByGene.gene.accessionId")
                    .description("Accession id for the gene, e.g MGI"),
                fieldWithPath("projectIntentions[].intentionByGene.gene.speciesName")
                    .description("Species associated with the gene"),
                fieldWithPath("projectIntentions[].intentionBySequence[]")
                    .description("Sequence information"),
                fieldWithPath("privacyName")
                    .description("Privacy level for the project (public, protected or restricted)"),
                fieldWithPath("speciesNames")
                    .description("Species associated with the project."),
                fieldWithPath("consortia")
                    .description("Consortia associated with the project."),
                fieldWithPath("consortia[].consortiumName")
                    .description("Name of the consortium."),
                fieldWithPath("consortia[].institutes")
                    .description("Institutes associated with the project - consortium"),
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
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = Paths.MULTIPLE_PROJECTS)
    void testGetOneProjectNotExisting() throws Exception
    {
        mvc().perform(MockMvcRequestBuilders
            .get("/api/projects/TPN:01X")
            .header("Authorization", accessToken))
            .andExpect(status().is5xxServerError());
    }

    @Test
    @DatabaseSetup(Paths.MULTIPLE_PROJECTS)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = Paths.MULTIPLE_PROJECTS)
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
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = Paths.MULTIPLE_PROJECTS)
    void testGetAllProjectsWithFilter() throws Exception
    {
        mvc().perform(MockMvcRequestBuilders
            .get("/api/projects?tpns=TPN:01,TPN:02")
            .header("Authorization", accessToken))
            .andExpect(status().isOk())
            .andDo(document("projects/allProjectsWithFilter"));
    }
}
