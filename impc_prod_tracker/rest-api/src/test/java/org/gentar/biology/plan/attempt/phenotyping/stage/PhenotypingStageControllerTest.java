package org.gentar.biology.plan.attempt.phenotyping.stage;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.gentar.audit.history.HistoryFieldsDescriptors;
import org.gentar.audit.history.HistoryValidator;
import org.gentar.biology.ChangeResponse;
import org.gentar.common.history.HistoryDTO;
import org.gentar.common.history.HistoryDetailDTO;
import org.gentar.framework.*;
import org.gentar.framework.asserts.json.PhenotypingStageCustomizations;
import org.gentar.framework.db.DBSetupFilesPaths;
import org.gentar.helpers.LinkUtil;
import org.gentar.util.JsonHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.ResultHandler;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

class PhenotypingStageControllerTest extends ControllerTestTemplate
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
        String obtainedJson =
            restCaller.executeGetAndDocument(url, document("phenotypingStages/allPhenotypingStages"));
        resultValidator.validateObtainedMatchesJson(obtainedJson, expectedJson);
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_PHENOTYPING_STAGES)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_PHENOTYPING_STAGES)
    void testGetPhenotypingStageInPlan() throws Exception
    {
        String url = "/api/plans/PIN:0000000001/phenotypingStages/PSN:000000000001";
        String expectedJson = getCompleteResourcePath("expectedPhenotypingStagePSN_000000000001.json");
        String obtainedJson = restCaller.executeGetAndDocument(url, documentPhenotypingStage());
        resultValidator.validateObtainedMatchesJson(obtainedJson, expectedJson);
    }

    private ResultHandler documentPhenotypingStage()
    {
        List<FieldDescriptor> phenotypingStageFieldsDescriptions =
            PhenotypingStageFieldsDescriptors.getPhenotypingStageFieldsDescriptions();
        return document(
            "phenotypingStages/getPhenotypingStage",
            responseFields(phenotypingStageFieldsDescriptions));
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_PHENOTYPING_STAGES)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_PHENOTYPING_STAGES)
    void testGetPhenotypingStageInPlanHistory() throws Exception
    {
        String url = "/api/plans/PIN:0000000001/phenotypingStages/PSN:000000000002/history";
        String expectedJson =
            getCompleteResourcePath("expectedPhenotypingStageHistoryPSN_000000000002.json");
        String obtainedJson = restCaller.executeGetAndDocument(url, documentPhenotypingStageHistory());
        resultValidator.validateObtainedMatchesJson(obtainedJson, expectedJson);
    }

    private ResultHandler documentPhenotypingStageHistory()
    {
        List<FieldDescriptor> historyFieldDescriptions =
            HistoryFieldsDescriptors.getHistoryFieldDescriptions();
        return document("phenotypingStages/history", responseFields(historyFieldDescriptions));
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_PHENOTYPING_STAGES)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_PHENOTYPING_STAGES)
    void testCreatePhenotypingStageInPlan() throws Exception
    {
        sequenceResetter.syncSequence("PHENOTYPING_STAGE_SEQ", "PHENOTYPING_STAGE");
        sequenceResetter.syncSequence("PHENOTYPING_STAGE_STATUS_STAMP_SEQ", "PHENOTYPING_STAGE_STATUS_STAMP");
        sequenceResetter.syncSequence("TISSUE_DISTRIBUTION_SEQ", "TISSUE_DISTRIBUTION");
        sequenceResetter.syncSequence("HISTORY_SEQ", "HISTORY");
        sequenceResetter.syncSequence("HISTORY_DETAIL_SEQ", "HISTORY_DETAIL");

        String payload = loadFromResource("phenotypingStageCreationPayload.json");

        String url = "/api/plans/PIN:0000000002/phenotypingStages/";
        String expectedJson = getCompleteResourcePath("expectedCreatedPhenotypingStage.json");
        String obtainedJson =
            restCaller.executePostAndDocument(url, payload, document("phenotypingStages/postPhenotypingStage"));
        String phenotypingStageUrl = LinkUtil.getSelfHrefLinkStringFromJson(obtainedJson);

        verifyCreatedPhenotypingStage(phenotypingStageUrl, expectedJson);
    }

    private void verifyCreatedPhenotypingStage(
        String phenotypingStageUrl, String expectedJson) throws Exception
    {
        String obtained = restCaller.executeGet(phenotypingStageUrl);
        resultValidator.validateObtainedMatchesJson(
            obtained, expectedJson, PhenotypingStageCustomizations.ignoreIdsAndPsnAndDates());
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_PHENOTYPING_STAGES)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_PHENOTYPING_STAGES)
    void testUpdatePhenotypingStageInPlan() throws Exception
    {
        sequenceResetter.syncSequence("PHENOTYPING_STAGE_SEQ", "PHENOTYPING_STAGE");
        sequenceResetter.syncSequence(
            "PHENOTYPING_STAGE_STATUS_STAMP_SEQ", "PHENOTYPING_STAGE_STATUS_STAMP");
        sequenceResetter.syncSequence("TISSUE_DISTRIBUTION_SEQ", "TISSUE_DISTRIBUTION");
        sequenceResetter.syncSequence("HISTORY_SEQ", "HISTORY");
        sequenceResetter.syncSequence("HISTORY_DETAIL_SEQ", "HISTORY_DETAIL");

        String payload = loadFromResource("phenotypingStageUpdatePayload.json");
        String url = "/api/plans/PIN:0000000001/phenotypingStages/PSN:000000000001";
        String expectedJson =
            getCompleteResourcePath("expectedUpdatedPhenotypingStagePSN_000000000001.json");
        String obtainedJson =
            restCaller.executePutAndDocument(
                url, payload, document("phenotypingStages/putPhenotypingStage"));
        ChangeResponse changeResponse = JsonHelper.fromJson(obtainedJson, ChangeResponse.class);
        verifyChangeResponse(changeResponse);
        String phenotypingStageUrl = LinkUtil.getSelfHrefLinkStringFromJson(obtainedJson);
        verifyUpdatedPhenotypingStage(phenotypingStageUrl, expectedJson);
    }

    private void verifyChangeResponse(ChangeResponse changeResponse)
    {
        List<HistoryDTO> historyDTOS = changeResponse.getHistoryDTOs();
        assertThat(historyDTOS.size(), is(1));

        HistoryDTO historyDTO = historyDTOS.get(0);
        assertThat(historyDTO.getComment(), is("PhenotypingStage updated"));

        List<HistoryDetailDTO> historyDetailDTOS = historyDTO.getDetails();
        assertThat(historyDetailDTOS.size(), is(1));

        HistoryDetailDTO historyDetailDTO1 =
            HistoryValidator.getHistoryDetailByField(historyDetailDTOS, "doNotCountTowardsCompleteness");
        assertThat(historyDetailDTO1.getOldValue(), is("true"));
        assertThat(historyDetailDTO1.getNewValue(), is("false"));
    }

    private void verifyUpdatedPhenotypingStage(
        String phenotypingStageUrl, String expectedJson) throws Exception
    {
        String obtained = restCaller.executeGet(phenotypingStageUrl);
        resultValidator.validateObtainedMatchesJson(
            obtained, expectedJson, PhenotypingStageCustomizations.ignoreIdsAndPsnAndDates());
    }

    private String getCompleteResourcePath(String resourceJsonName)
    {
        return TEST_RESOURCES_FOLDER + resourceJsonName;
    }

    private String loadFromResource(String resourceName)
        throws IOException
    {
        String completeResourcePath = getCompleteResourcePath(resourceName);
        return TestResourceLoader.loadJsonFromResource(completeResourcePath);
    }
}