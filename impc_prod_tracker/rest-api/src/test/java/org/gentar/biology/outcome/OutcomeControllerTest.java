package org.gentar.biology.outcome;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.gentar.audit.history.HistoryFieldsDescriptors;
import org.gentar.audit.history.HistoryValidator;
import org.gentar.biology.ChangeResponse;
import org.gentar.common.history.HistoryDTO;
import org.gentar.common.history.HistoryDetailDTO;
import org.gentar.framework.ControllerTestTemplate;
import org.gentar.framework.RestCaller;
import org.gentar.framework.ResultValidator;
import org.gentar.framework.SequenceResetter;
import org.gentar.framework.TestResourceLoader;
import org.gentar.framework.asserts.json.MutationCustomizations;
import org.gentar.framework.asserts.json.OutcomeCustomizations;
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

class OutcomeControllerTest extends ControllerTestTemplate
{
    private static final String TEST_RESOURCES_FOLDER = INTEGRATION_TESTS_RESOURCE_PATH + "outcomes/";

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
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_OUTCOMES)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_OUTCOMES)
    void testGetAllOutcomesInPlan() throws Exception
    {
        String url = "/api/plans/PIN:0000000001/outcomes";
        String expectedJson = getCompleteResourcePath("expectedAllOutcomes.json");
        String obtainedJson = restCaller.executeGetAndDocument(url, document("outcomes/allOutcomes"));
        resultValidator.validateObtainedMatchesJson(obtainedJson, expectedJson);
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_OUTCOMES)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_OUTCOMES)
    void testGetColonyOutcomeInPlan() throws Exception
    {
        String url = "/api/plans/PIN:0000000001/outcomes/TPO:000000000001";
        String expectedJson = getCompleteResourcePath("expectedColonyOutcomeTPO_000000000001.json");
        String obtainedJson = restCaller.executeGetAndDocument(url, documentColonyOutcome());
        resultValidator.validateObtainedMatchesJson(obtainedJson, expectedJson);
    }

    private ResultHandler documentColonyOutcome()
    {
        List<FieldDescriptor> outcomeFieldDescriptions =
            OutcomeFieldsDescriptors.getOutcomeFieldDescriptions();
        outcomeFieldDescriptions.addAll(OutcomeFieldsDescriptors.getColonyFieldDescriptions());
        return document("outcomes/colonyOutcome", responseFields(outcomeFieldDescriptions));
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_OUTCOMES)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_OUTCOMES)
    void testGetColonyOutcomeInPlanHistory() throws Exception
    {
        String url = "/api/plans/PIN:0000000001/outcomes/TPO:000000000002/history";
        String expectedJson =
            getCompleteResourcePath("expectedColonyOutcomeHistoryTPO_000000000002.json");
        String obtainedJson = restCaller.executeGetAndDocument(url, documentColonyOutcomeHistory());
        resultValidator.validateObtainedMatchesJson(obtainedJson, expectedJson);
    }

    private ResultHandler documentColonyOutcomeHistory()
    {
        List<FieldDescriptor> historyFieldDescriptions =
            HistoryFieldsDescriptors.getHistoryFieldDescriptions();
        return document("outcomes/colonyOutcome/history", responseFields(historyFieldDescriptions));
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_OUTCOMES)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_OUTCOMES)
    void testGetSpecimenOutcomeInPlan() throws Exception
    {
        String url = "/api/plans/PIN:0000000001/outcomes/TPO:000000000004";
        String expectedJson = getCompleteResourcePath("expectedSpecimenOutcomeTPO_000000000004.json");
        String obtainedJson = restCaller.executeGetAndDocument(url, documentSpecimenOutcome());
        resultValidator.validateObtainedMatchesJson(obtainedJson, expectedJson);
    }

    private ResultHandler documentSpecimenOutcome()
    {
        List<FieldDescriptor> outcomeFieldDescriptions =
            OutcomeFieldsDescriptors.getOutcomeFieldDescriptions();
        outcomeFieldDescriptions.addAll(OutcomeFieldsDescriptors.getSpecimenFieldDescriptions());
        return document("outcomes/specimenOutcome", responseFields(outcomeFieldDescriptions));
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_OUTCOMES)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_OUTCOMES)
    void testCreateColonyOutcomeInPlan() throws Exception
    {
        sequenceResetter.syncSequence("OUTCOME_SEQ", "OUTCOME");
        sequenceResetter.syncSequence("COLONY_STATUS_STAMP_SEQ", "COLONY_STATUS_STAMP");
        sequenceResetter.syncSequence("DISTRIBUTION_PRODUCT_SEQ", "DISTRIBUTION_PRODUCT");
        sequenceResetter.syncSequence("MUTATION_SEQ", "MUTATION");
        sequenceResetter.syncSequence("SEQUENCE_SEQ", "SEQUENCE");
        sequenceResetter.syncSequence("MUTATION_SEQUENCE_SEQ", "MUTATION_SEQUENCE");
        sequenceResetter.syncSequence("HISTORY_SEQ", "HISTORY");
        sequenceResetter.syncSequence("HISTORY_DETAIL_SEQ", "HISTORY_DETAIL");

        String payload = loadFromResource("colonyOutcomeCreationPayload.json");

        String url = "/api/plans/PIN:0000000001/outcomes";
        String expectedJson = getCompleteResourcePath("expectedCreatedColonyOutcome.json");
        String obtainedJson =
            restCaller.executePostAndDocument(url, payload, document("outcomes/postColonyOutcome"));
        String outcomeUrl = LinkUtil.getSelfHrefLinkStringFromJson(obtainedJson);
        verifyCreatedOutcome(outcomeUrl, expectedJson);
    }

    private void verifyCreatedOutcome(
        String outcomeUrl, String expectedJson) throws Exception
    {
        String obtainedOutcome = restCaller.executeGet(outcomeUrl);
        resultValidator.validateObtainedMatchesJson(
            obtainedOutcome, expectedJson, OutcomeCustomizations.ignoreIdsAndTpoAndDates());

        String mutationLink =
            LinkUtil.getCustomHrefLinkStringFromJsonArray(obtainedOutcome, "mutations", 0);
        verifyObtainedMutation(
            mutationLink, getCompleteResourcePath("expectedNestedCreatedMutation.json"));
    }

    private void verifyObtainedMutation(String mutationLink, String jsonFileName)
    throws Exception
    {
        String obtainedMutation = restCaller.executeGet(mutationLink);
        resultValidator.validateObtainedMatchesJson(
            obtainedMutation, jsonFileName, MutationCustomizations.ignoreIdsAndMin());
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_OUTCOMES)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_OUTCOMES)
    void testUpdateColonyOutcomeInPlan() throws Exception
    {
        sequenceResetter.syncSequence("HISTORY_SEQ", "HISTORY");
        sequenceResetter.syncSequence("HISTORY_DETAIL_SEQ", "HISTORY_DETAIL");

        String payload = loadFromResource("colonyOutcomeUpdatePayload.json");
        String url = "/api/plans/PIN:0000000001/outcomes/TPO:000000000001";
        String expectedJson = getCompleteResourcePath("expectedUpdatedColonyOutcome.json");
        String obtainedJson =
            restCaller.executePutAndDocument(url, payload, document("outcomes/putColonyOutcome"));
        ChangeResponse changeResponse = JsonHelper.fromJson(obtainedJson, ChangeResponse.class);
        verifyChangeResponse(changeResponse);
        String outcomeUrl = LinkUtil.getSelfHrefLinkStringFromJson(obtainedJson);
        verifyUpdatedOutcome(outcomeUrl, expectedJson);
    }

    private void verifyChangeResponse(ChangeResponse changeResponse)
    {
        List<HistoryDTO> historyDTOS = changeResponse.getHistoryDTOs();
        assertThat(historyDTOS.size(), is(1));

        HistoryDTO historyDTO = historyDTOS.get(0);
        assertThat(historyDTO.getComment(), is("Outcome updated"));

        List<HistoryDetailDTO> historyDetailDTOS = historyDTO.getDetails();
        assertThat(historyDetailDTOS.size(), is(1));

        HistoryDetailDTO historyDetailDTO1 =
            HistoryValidator.getHistoryDetailByField(historyDetailDTOS, "colony.genotypingComment");
        assertThat(historyDetailDTO1.getOldValue(), is("An example comment for the genotyping."));
        assertThat(historyDetailDTO1.getNewValue(), is("Genotyping comment changed."));
    }

    private void verifyUpdatedOutcome(
        String outcomeUrl, String expectedJson) throws Exception
    {
        String obtainedOutcome = restCaller.executeGet(outcomeUrl);
        resultValidator.validateObtainedMatchesJson(
            obtainedOutcome, expectedJson, OutcomeCustomizations.ignoreIdsAndTpoAndDates());
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