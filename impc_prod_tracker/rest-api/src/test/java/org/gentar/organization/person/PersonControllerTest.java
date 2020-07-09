package org.gentar.organization.person;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.gentar.framework.ControllerTestTemplate;
import org.gentar.framework.RestCaller;
import org.gentar.framework.ResultValidator;
import org.gentar.framework.SequenceResetter;
import org.gentar.framework.TestResourceLoader;
import org.gentar.framework.asserts.json.PersonCustomizations;
import org.gentar.framework.db.DBSetupFilesPaths;
import org.gentar.security.authentication.AAPService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
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
    @Mock
    ResultActions resultActions;

    @BeforeEach
    void setUp() throws Exception
    {
        setTestUserSecurityContext();
        resultValidator = new ResultValidator();
        restCaller = new RestCaller(mvc(), accessToken);
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.ADMIN_USER)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.ADMIN_USER)
    public void testCreatePerson() throws Exception
    {
        sequenceResetter.syncSequence("PERSON_SEQ", "PERSON");
        sequenceResetter.syncSequence("PERSON_ROLE_CONSORTIUM_SEQ", "PERSON_ROLE_CONSORTIUM");
        sequenceResetter.syncSequence("PERSON_ROLE_WORK_UNIT_SEQ", "PERSON_ROLE_WORK_UNIT");

        String payload = loadFromResource("createUserPayload.json");
        String url = "/api/people";
        doReturn("usr-dde67fbd-c3c2-42eb-aa81-f1a225f047c7").when(aapService).createUser(any(), any());
        String obtainedJson = restCaller.executePostAndDocument(url, payload, documentPersonCreation());
        System.out.println(obtainedJson);
        validateCreationResponse(obtainedJson, "expectedCreatedUser.json");
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
