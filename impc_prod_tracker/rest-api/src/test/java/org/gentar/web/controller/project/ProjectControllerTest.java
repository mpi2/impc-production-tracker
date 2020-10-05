package org.gentar.web.controller.project;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.gentar.audit.history.HistoryFieldsDescriptors;
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
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.io.IOException;
import java.util.List;

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
            responseFields(ProjectFieldsDescriptors.getResponseFieldDescriptions()));
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_PROJECTS)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_PROJECTS)
    void testGetOneProjectNotExisting() throws Exception
    {
        mvc().perform(MockMvcRequestBuilders
            .get("/api/projects/TPN:01X")
            .header("Authorization", accessToken))
            .andExpect(status().is4xxClientError());
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

    private void verifyUpdatedProject(String projectUrl, String expectedJsonPath) throws Exception
    {
        String expectedJsonCompletePath = getCompleteResourcePath(expectedJsonPath);
        String obtainedMProject = restCaller.executeGet(projectUrl);
        resultValidator.validateObtainedMatchesJson(obtainedMProject, expectedJsonCompletePath);
    }

    private ResultHandler documentUpdateOfProject()
    {
        List<FieldDescriptor> descriptors = ProjectFieldsDescriptors.getSharedFieldDescriptions();
        descriptors.add(ProjectFieldsDescriptors.getTpnDescriptor());
        return document(
            "projects/putProject",
            requestFields(descriptors),
            responseFields(HistoryFieldsDescriptors.getHistoryFieldDescriptions("history")));
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_PROJECTS)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_PROJECTS)
    void testCreateProject() throws Exception
    {
        sequenceResetter.syncSequence("PROJECT_CONSORTIUM_SEQ", "PROJECT_CONSORTIUM");
        sequenceResetter.syncSequence("PROJECT_INTENTION_SEQ", "PROJECT_INTENTION");
        sequenceResetter.syncSequence("PROJECT_INTENTION_SEQUENCE_SEQ", "PROJECT_INTENTION_SEQUENCE");
        sequenceResetter.syncSequence("PROJECT_SEQ", "PROJECT");
        sequenceResetter.syncSequence("PLAN_SEQ", "PLAN");
        sequenceResetter.syncSequence("SEQUENCE_SEQ", "SEQUENCE");
        sequenceResetter.syncSequence("LOCATION_SEQ", "LOCATION");
        sequenceResetter.syncSequence("SEQUENCE_LOCATION_SEQ", "SEQUENCE_LOCATION");

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
            requestFields(ProjectFieldsDescriptors.getCreationFieldDescriptions()),
            responseFields(
                HistoryFieldsDescriptors.getHistoryFieldDescriptions("history")));
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
