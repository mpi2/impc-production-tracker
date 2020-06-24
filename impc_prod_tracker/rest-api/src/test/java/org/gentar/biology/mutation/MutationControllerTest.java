package org.gentar.biology.mutation;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.gentar.framework.ControllerTestTemplate;
import org.gentar.framework.RestCaller;
import org.gentar.framework.ResultValidator;
import org.gentar.framework.SequenceResetter;
import org.gentar.framework.db.DBSetupFilesPaths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MutationControllerTest extends ControllerTestTemplate
{
    private static final String TEST_RESOURCES_FOLDER = INTEGRATION_TESTS_RESOURCE_PATH + "mutations/";

    private ResultValidator resultValidator;
    private RestCaller restCaller;

    @Autowired
    private SequenceResetter sequenceResetter;

    @BeforeEach
    void setUp() throws Exception
    {
        setTestUserSecurityContext();
        resultValidator = new ResultValidator();
        restCaller = new RestCaller(mvc(), accessToken);
    }


    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_MUTATIONS)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_MUTATIONS)
    void testGetMutationInOutcome() throws Exception
    {
        String url = "/api/plans/PIN:0000000001/outcomes/TPO:000000000002/mutations/MIN:000000000002";
        String expectedJson = getCompleteResourcePath("expectedMutationMIN_000000000002.json");
        String obtainedJson = restCaller.executeGet(url);
        System.out.println(obtainedJson);
        resultValidator.validateObtainedMatchesJson(obtainedJson, expectedJson);
     }

    private String getCompleteResourcePath(String resourceJsonName)
    {
        return TEST_RESOURCES_FOLDER + resourceJsonName;
    }

}
