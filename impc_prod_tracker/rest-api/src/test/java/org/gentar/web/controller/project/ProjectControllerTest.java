package org.gentar.web.controller.project;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.gentar.framework.ControllerTestTemplate;
import org.gentar.framework.RestCaller;
import org.gentar.framework.ResultValidator;
import org.gentar.framework.SequenceResetter;
import org.gentar.framework.TestResourceLoader;
import org.gentar.framework.asserts.json.ChangeResponseCustomizations;
import org.gentar.framework.asserts.json.ProjectCustomizations;
import org.gentar.framework.db.DBSetupFilesPaths;
import org.gentar.helpers.LinkUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.io.IOException;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProjectControllerTest extends ControllerTestTemplate
{
    private static final String TEST_RESOURCES_FOLDER = INTEGRATION_TESTS_RESOURCE_PATH + "projects/";

    @Autowired
    private SequenceResetter sequenceResetter;

    private ResultValidator resultValidator;
    private RestCaller restCaller;

    @BeforeEach
    public void setup() throws Exception
    {
        setTestUserSecurityContext();
        resultValidator = new ResultValidator();
        restCaller = new RestCaller(mvc(), accessToken);
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_PROJECTS)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_PROJECTS)
    void testGetOneProject() throws Exception
    {
        String url = "/api/projects/TPN:000000001";
        String expectedJsonPath = getCompleteResourcePath("expectedProjectTPN_000000001.json");
        String obtainedJson = restCaller.executeGetAndDocument(url, documentSingleProject());
        resultValidator.validateObtainedMatchesJson(obtainedJson, expectedJsonPath);
    }

    private ResultHandler documentSingleProject()
    {
        return document(
            "projects/getProject",
            responseFields(
                fieldWithPath("tpn").description("Public identifier for the project. Read only."),
                fieldWithPath("assignmentStatusName")
                    .description("Assignment Status for the project. It could be marked as in conflict if " +
                        "existing projects are working in the same gene. Read only."),
                fieldWithPath("summaryStatusName")
                    .description("A status summarising the global status based on the statuses of " +
                        "the plans in the project. Read only."),
                fieldWithPath("reactivationDate")
                    .description("Date on which the project was activated again" +
                        " (assignment Status changed from inactive). Read only."),
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
                    .description("Name of the molecular mutation type."),
                fieldWithPath("projectIntentions[].mutationCategorizations")
                    .description("Mutation categorizations linked to the project intention."),
                fieldWithPath("projectIntentions[].mutationCategorizations[].name")
                    .description("Name of the mutation categorization."),
                fieldWithPath("projectIntentions[].mutationCategorizations[].description")
                    .description("Description of the mutation categorization."),
                fieldWithPath("projectIntentions[].mutationCategorizations[].typeName")
                    .description("Name of the type of the mutation categorization."),
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
                fieldWithPath("projectIntentions[].intentionsBySequence[]")
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
                fieldWithPath("_links.productionPlans").description("Links to production plans"),
                fieldWithPath("_links.self.href").description("Link to the project"),
                fieldWithPath("_links.productionPlans[].href")
                    .description("Link to a specific production plan")
            ));
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_PROJECTS)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_PROJECTS)
    void testGetOneProjectNotExisting() throws Exception
    {
        mvc().perform(MockMvcRequestBuilders
            .get("/api/projects/TPN:01X")
            .header("Authorization", accessToken))
            .andExpect(status().is5xxServerError());
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_PROJECTS)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_PROJECTS)
    void testGetAllProjects() throws Exception
    {
        String url = "/api/projects";
        String expectedJsonPath = getCompleteResourcePath("expectedAllProjects.json");
        String obtainedJson = restCaller.executeGetAndDocument(url, document("projects/allProjects"));
        resultValidator.validateObtainedMatchesJson(obtainedJson, expectedJsonPath);
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_PROJECTS)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_PROJECTS)
    void testGetAllProjectsWithFilter() throws Exception
    {
        String url = "/api/projects?assignmentStatusName=Assigned";
        String expectedJsonPath = getCompleteResourcePath("expectedFilteredProjects.json");
        String obtainedJson = restCaller.executeGetAndDocument(url, document("projects/allProjectsWithFilter"));
        resultValidator.validateObtainedMatchesJson(obtainedJson, expectedJsonPath);
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_PROJECTS)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_PROJECTS)
    void testUpdateProject() throws Exception
    {
        String payload = loadFromResource("projectTPN_000000001UpdatePayload.json");
        String url = "/api/projects/TPN:000000001";
        String obtainedJson =
            restCaller.executePutAndDocument(url, payload, documentUpdateOfProject());
        verifyUpdateResponse(obtainedJson, "expectedResponseUpdateProjectTPN_000000001.json");
        String projectUrl = LinkUtil.getSelfHrefLinkStringFromJson(obtainedJson);
        verifyUpdatedProject(projectUrl, "expectedUpdatedProjectTPN_000000001.json");
    }

    private void verifyUpdateResponse(String obtainedJson, String expectedJsonPath) throws Exception
    {
        String expectedJsonCompletePath = getCompleteResourcePath(expectedJsonPath);
        resultValidator.validateObtainedMatchesJson(
            obtainedJson, expectedJsonCompletePath, ChangeResponseCustomizations.ignoreDates());
    }

    private void verifyUpdatedProject(String mutationUrl, String expectedJsonPath) throws Exception
    {
        String expectedJsonCompletePath = getCompleteResourcePath(expectedJsonPath);
        String obtainedMutation = restCaller.executeGet(mutationUrl);
        resultValidator.validateObtainedMatchesJson(obtainedMutation, expectedJsonCompletePath);
    }

    private ResultHandler documentUpdateOfProject()
    {
        return document(
            "projects/putProject",
            requestFields(
                fieldWithPath("tpn")
                    .description("Public identifier for the project. " +
                        "Not editable, just for reference in the payload"),
                fieldWithPath("recovery").description("To be validated"),
                fieldWithPath("comment").description("Comment on this project."),
                fieldWithPath("externalReference")
                    .description("External reference for the project. Read only."),
                fieldWithPath("privacyName")
                    .description("Privacy level for the project (public, protected or restricted)")),
            responseFields(
                fieldWithPath("history[]").description("Changes in the system."),
                fieldWithPath("history[].id").description("Id of the change."),
                fieldWithPath("history[].user").description("The user that made the change."),
                fieldWithPath("history[].date").description("Date of the change."),
                fieldWithPath("history[].comment").description("Comment describing the change."),
                fieldWithPath("history[].details[]").description("Additional details of the change."),
                fieldWithPath("history[].details[].field")
                    .description("Field that changed."),
                fieldWithPath("history[].details[].oldValue")
                    .description("Value in the field before the change"),
                fieldWithPath("history[].details[].newValue")
                    .description("Value in the field after the change"),
                fieldWithPath("history[].details[].note")
                    .description("One of these: Field changed, Element added, Element deleted."),
                fieldWithPath("_links.self.href").description("Links to the project just created.")
            ));
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_PROJECTS)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_PROJECTS)
    void testCreateProject() throws Exception
    {
        sequenceResetter.syncSequence("PROJECT_CONSORTIUM_SEQ", "PROJECT_CONSORTIUM");
        sequenceResetter.syncSequence("PROJECT_INTENTION_SEQ", "PROJECT_INTENTION");
        sequenceResetter.syncSequence("PROJECT_SEQ", "PROJECT");
        sequenceResetter.syncSequence("PLAN_SEQ", "PLAN");

        String payload = loadFromResource("projectCreationPayload.json");
        String url = "/api/projects";
        String obtainedJson =
            restCaller.executePostAndDocument(url, payload, documentCreationOfProject());
        verifyCreationResponse(obtainedJson, "expectedCreationResponse.json");
        String projectLink = LinkUtil.getSelfHrefLinkStringFromJson(obtainedJson);
        verifyProjectCreation(projectLink, "expectedCreatedProject.json");
    }

    private void verifyCreationResponse(String obtainedJson, String expectedJsonPath) throws Exception
    {
        String expectedJsonCompletePath = getCompleteResourcePath(expectedJsonPath);
        resultValidator.validateObtainedMatchesJson(
            obtainedJson, expectedJsonCompletePath, ChangeResponseCustomizations.ignoreDates());
    }

    private void verifyProjectCreation(String projectLink, String expectedJsonPath) throws Exception
    {
        String expectedJsonCompletePath = getCompleteResourcePath(expectedJsonPath);
        String obtained = restCaller.executeGet(projectLink);
        System.out.println(obtained);
        resultValidator.validateObtainedMatchesJson(
             obtained, expectedJsonCompletePath, ProjectCustomizations.ignoreIdsAndDates());
    }

    private ResultHandler documentCreationOfProject()
    {
        return document(
            "projects/postProject",
            requestFields(
                fieldWithPath("recovery").description("To be validated"),
                fieldWithPath("comment").description("Comment on this project."),
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
                fieldWithPath("privacyName")
                    .description("Privacy level for the project (public, protected or restricted)"),
                fieldWithPath("speciesNames")
                    .description("Species associated with the project."),
                fieldWithPath("planDetails").description("..."),
                fieldWithPath("planDetails.funderNames")
                    .description("Name of the funders in for the plan"),
                fieldWithPath("planDetails.workUnitName").description("Work unit of the plan."),
                fieldWithPath("planDetails.workGroupName").description("Work group of the plan."),
                fieldWithPath("planDetails.comment").description("Comment in the plan."),
                fieldWithPath("planDetails.productsAvailableForGeneralPublic")
                    .description("Whether the product is available to the public."),
                fieldWithPath("planDetails.attemptTypeName").description("Attempt type name"),
                fieldWithPath("planDetails.typeName").description("Plan type name"),
                fieldWithPath("consortia")
                    .description("Consortia associated with the project."),
                fieldWithPath("consortia[].consortiumName")
                    .description("Name of the consortium."),
                fieldWithPath("consortia[].institutes")
                    .description("Institutes associated with the project - consortium")),

            responseFields(
                fieldWithPath("history[]").description("Changes in the system."),
                fieldWithPath("history[].id").description("Id of the change."),
                fieldWithPath("history[].user").description("The user that made the change."),
                fieldWithPath("history[].date").description("Date of the change."),
                fieldWithPath("history[].comment").description("Comment describing the change."),
                fieldWithPath("history[].details[]").description("Additional details of the change."),
                fieldWithPath("_links.self.href").description("Links to the project just created.")
            ));
    }

    private String loadFromResource(String resourceName)
        throws IOException
    {
        String completeResourcePath = getCompleteResourcePath(resourceName);
        return TestResourceLoader.loadJsonFromResource(completeResourcePath);
    }

    private String getCompleteResourcePath(String resourceJsonName)
    {
        return TEST_RESOURCES_FOLDER + resourceJsonName;
    }
}
