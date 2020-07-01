package org.gentar.biology.plan.attempt.phenotyping.stage;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.gentar.framework.*;
import org.gentar.framework.db.DBSetupFilesPaths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.ResultHandler;

import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

class PhenotypingStageControllerTest  extends ControllerTestTemplate
{
    private static final String TEST_RESOURCES_FOLDER = INTEGRATION_TESTS_RESOURCE_PATH + "phenotypingStages/";

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
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_PHENOTYPING_STAGES)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_PHENOTYPING_STAGES)
    void testGetAllPhenotypingStagesInPlan() throws Exception
    {
        String url = "/api/plans/PIN:0000000001/phenotypingStages";
        String expectedJson = getCompleteResourcePath("expectedAllPhenotypingStages.json");
        String obtainedJson = restCaller.executeGet(url);
        resultValidator.validateObtainedMatchesJson(obtainedJson, expectedJson);
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_PHENOTYPING_STAGES)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_PHENOTYPING_STAGES)
    void testGetPhenotypingStageInPlan() throws Exception
    {
        String url = "/api/plans/PIN:0000000001/phenotypingStages/PSN:000000000001";
        String expectedJson = getCompleteResourcePath("expectedPhenotypingStagePSN:000000000001.json");
        String obtainedJson = restCaller.executeGetAndDocument(url, documentPhenotypingStage());
        resultValidator.validateObtainedMatchesJson(obtainedJson, expectedJson);
    }

    private String getCompleteResourcePath(String resourceJsonName)
    {
        return TEST_RESOURCES_FOLDER + resourceJsonName;
    }

    private ResultHandler documentPhenotypingStage()
    {
        List<FieldDescriptor> phenotypingStageFieldsDescriptions = PhenotypingStageFieldsDescriptors.getPhenotypingStageFieldsDescriptions();
        return document("phenotypingStage", responseFields(phenotypingStageFieldsDescriptions));
    }

}