package org.gentar.organization.person;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.gentar.framework.*;
import org.gentar.framework.asserts.json.PersonCustomizations;
import org.gentar.framework.db.DBSetupFilesPaths;
import org.gentar.security.authentication.AAPService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.ResultHandler;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;


class PersonControllerTest extends ControllerTestTemplate
{
    private static final String TEST_RESOURCES_FOLDER = INTEGRATION_TESTS_RESOURCE_PATH + "people/";

    private ResultValidator resultValidator;
    private RestCaller restCaller;

    @Autowired
    private SequenceResetter sequenceResetter;

    @SpyBean
    @Autowired
    private AAPService aapService;

    @BeforeEach
    void setUp()
    {
        resultValidator = new ResultValidator();
        restCaller = new RestCaller(mvc(), accessToken);
    }

    private void setupAuthentication() throws Exception
    {
        setTestUserSecurityContext();
        restCaller = new RestCaller(mvc(), accessToken);
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.GENERAL_USER)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.GENERAL_USER)
    void testGetCurrentUser() throws Exception
    {
        setupAuthentication();

        String obtainedJson =
                restCaller.executeGetAndDocument("/api/people/currentPerson", documentCurrentUser());
        validateGetResponse(obtainedJson);
    }

    private void validateGetResponse(String obtainedJson) throws Exception
    {
        String completePathExpectedJson = getCompleteResourcePath("expectedGeneralUser.json");
        resultValidator.validateObtainedMatchesJson(obtainedJson, completePathExpectedJson);
    }

    private ResultHandler documentCurrentUser()
    {
        List<FieldDescriptor> personFieldDescriptions =
                PersonFieldsDescriptors.getPersonFieldDescriptions();
        return document("people/getPerson", responseFields(personFieldDescriptions));
    }

    private ResultHandler documentPersonCreation()
    {
        List<FieldDescriptor> personFieldDescriptions =
                PersonFieldsDescriptors.getPersonFieldDescriptions();
        return document("people/postPerson", responseFields(personFieldDescriptions));
    }

    private void validateCreationResponse(String obtainedJson, String expectedJsonPath) throws Exception
    {
        String completePathExpectedJson = getCompleteResourcePath(expectedJsonPath);
        resultValidator.validateObtainedMatchesJson(
                obtainedJson, completePathExpectedJson, PersonCustomizations.ignoreIds());
    }

    @Test
    public void testRequestPasswordReset() throws Exception
    {
        String email = "gentar_test_user1@gentar.org";
        String url = "/api/people/requestPasswordReset";
        doNothing().when(aapService).requestPasswordReset(email);
        restCaller.executePostAndDocumentNoAuthentication(url, email, documentRequestPasswordReset());
    }

    private ResultHandler documentRequestPasswordReset()
    {
        return document("people/requestPasswordReset");
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.ADMIN_USER)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.ADMIN_USER)
    public void testUpdateManagedUser() throws Exception
    {
        setupAuthentication();
        sequenceResetter.syncSequence("PERSON_SEQ", "PERSON");
        sequenceResetter.syncSequence("PERSON_ROLE_CONSORTIUM_SEQ", "PERSON_ROLE_CONSORTIUM");
        sequenceResetter.syncSequence("PERSON_ROLE_WORK_UNIT_SEQ", "PERSON_ROLE_WORK_UNIT");

        String payload = loadFromResource("updateUserPayload.json");
        String url = "/api/people/gentar_test_user2@gentar.org";
        String obtainedJson =
                restCaller.executePutAndDocument(url, payload, documentUpdateManagedUser());
        validateCreationResponse(obtainedJson, "expectedUpdatedManagedUser.json");
    }

    private ResultHandler documentUpdateManagedUser()
    {
        List<FieldDescriptor> personFieldDescriptions =
                PersonFieldsDescriptors.getPersonFieldDescriptions();
        return document("people/updateManagedUser", responseFields(personFieldDescriptions));
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