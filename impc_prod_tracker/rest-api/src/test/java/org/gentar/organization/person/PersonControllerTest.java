package org.gentar.organization.person;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.gentar.framework.*;
import org.gentar.framework.db.DBSetupFilesPaths;
import org.gentar.security.authentication.AAPService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.ResultHandler;

import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;


class PersonControllerTest extends ControllerTestTemplate
{
    private static final String TEST_RESOURCES_FOLDER = INTEGRATION_TESTS_RESOURCE_PATH + "people/";

    private ResultValidator resultValidator;
    private RestCaller restCaller;

    @Autowired
    private SequenceResetter sequenceResetter;

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
        validateGetResponse(obtainedJson, "expectedGeneralUser.json");
    }

    private void validateGetResponse(String obtainedJson, String expectedJsonPath) throws Exception
    {
        String completePathExpectedJson = getCompleteResourcePath(expectedJsonPath);
        resultValidator.validateObtainedMatchesJson(obtainedJson, completePathExpectedJson);
    }

    private ResultHandler documentCurrentUser()
    {
        List<FieldDescriptor> personFieldDescriptions =
                PersonFieldsDescriptors.getPersonFieldDescriptions();
        return document("people/getPerson", responseFields(personFieldDescriptions));
    }




    private String getCompleteResourcePath(String resourceJsonName)
    {
        return TEST_RESOURCES_FOLDER + resourceJsonName;
    }
}
