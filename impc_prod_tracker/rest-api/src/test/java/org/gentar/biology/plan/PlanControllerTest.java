package org.gentar.biology.plan;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.gentar.audit.history.HistoryFieldsDescriptors;
import org.gentar.biology.ChangeResponse;
import org.gentar.common.history.HistoryDTO;
import org.gentar.common.history.HistoryDetailDTO;
import org.gentar.framework.*;
import org.gentar.framework.asserts.json.PlanCustomizations;
import org.gentar.framework.db.DBSetupFilesPaths;
import org.gentar.helpers.LinkUtil;
import org.gentar.util.JsonHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.ResultHandler;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

class PlanControllerTest extends ControllerTestTemplate
{
    private static final String TEST_RESOURCES_FOLDER = INTEGRATION_TESTS_RESOURCE_PATH + "plans/";

    private ResultValidator resultValidator;
    private RestCaller restCaller;

    @Autowired
    private SequenceResetter sequenceResetter;

    @BeforeEach
    public void setup() throws Exception
    {
        setTestUserSecurityContext();
        resultValidator = new ResultValidator();
        restCaller = new RestCaller(mvc(), accessToken);
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_PLANS)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_PLANS)
    void testGetOneCrisprPlan() throws Exception
    {
        String contentAsString =
            restCaller.executeGetAndDocument("/api/plans/PIN:0000000001", documentCrisprPlan());
        String expectedOutputAsString =
            loadExpectedResponseFromResource("expectedCrisprPlanGetPIN_0000000001.json");

        JSONAssert.assertEquals(expectedOutputAsString, contentAsString, JSONCompareMode.STRICT);
    }

    private ResultHandler documentCrisprPlan()
    {
        List<FieldDescriptor> allFieldDescriptors = PlanFieldsDescriptors.getSharedFieldDescriptions();
        allFieldDescriptors.addAll(PlanFieldsDescriptors.getCrisprFieldDescriptors());
        return document("plans/getCrisprPlan", responseFields(allFieldDescriptors));
    }

    private String loadExpectedResponseFromResource(String resourceName)
            throws IOException
    {
        String completeResourcePath = TEST_RESOURCES_FOLDER + resourceName;
        return TestResourceLoader.loadJsonFromResource(completeResourcePath);
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_PLANS)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_PLANS)
    void testGetAllPlans() throws Exception
    {
        String contentAsString =
            restCaller.executeGetAndDocument("/api/plans", document("plans/allPlans"));

        String expectedOutputAsString =
            loadExpectedResponseFromResource("expectedAllPlans.json");

        JSONAssert.assertEquals(expectedOutputAsString, contentAsString, JSONCompareMode.STRICT);
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_PLANS)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_PLANS)
    void testGetFilteredPlans() throws Exception
    {
        String contentAsString = restCaller.executeGetAndDocument(
            "/api/plans?statusName=Founder Obtained&attemptTypeName=crispr",
            document("plans/filteredPlans"));
        String expectedOutputAsString =
            loadExpectedResponseFromResource("expectedFilteredPlans.json");
        JSONAssert.assertEquals(expectedOutputAsString, contentAsString, JSONCompareMode.STRICT);
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_PLANS)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_PLANS)
    void testUpdateCrisprPlan() throws Exception
    {
        sequenceResetter.syncSequence("PLAN_STATUS_STAMP_SEQ", "PLAN_STATUS_STAMP");
        sequenceResetter.syncSequence("PLAN_SUMMARY_STATUS_STAMP_SEQ", "PLAN_SUMMARY_STATUS_STAMP");
        sequenceResetter.syncSequence("HISTORY_SEQ", "HISTORY");
        sequenceResetter.syncSequence("HISTORY_DETAIL_SEQ", "HISTORY_DETAIL");

        String payload = loadFromResource("crisprPlanUpdatePayload.json");
        String url = "/api/plans/PIN:0000000001";

        String obtainedJson =
                restCaller.executePutAndDocument(url, payload, document("plans/putCrisprPlan"));
        ChangeResponse changeResponse = JsonHelper.fromJson(obtainedJson, ChangeResponse.class);
        verifyChangeResponseCrisprUpdate(changeResponse);

        String cripsrPlanUrl = LinkUtil.getSelfHrefLinkStringFromJson(obtainedJson);
        verifyGetPlantEqualsJsonIgnoringIdsAndDates(cripsrPlanUrl, "expectedUpdatedPlanGetPIN_0000000001.json");
    }

    private void verifyChangeResponseCrisprUpdate(ChangeResponse changeResponse)
    {
        List<HistoryDTO> historyDTOS = changeResponse.getHistoryDTOs();
        assertThat(historyDTOS.size(), is(1));

        HistoryDTO historyDTO = historyDTOS.get(0);
        assertThat(historyDTO.getComment(), is("Plan updated"));

        List<HistoryDetailDTO> historyDetailDTOS = historyDTO.getDetails();
        assertThat(historyDetailDTOS.size(), is(4));

        HistoryDetailDTO historyDetailDTO1 =
                getHistoryDetailByField(historyDetailDTOS, "comment");
        assertThat(historyDetailDTO1.getOldValue(), is(nullValue()));
        assertThat(historyDetailDTO1.getNewValue(), is("New Plan comment"));

        HistoryDetailDTO historyDetailDTO2 =
            getHistoryDetailByField(historyDetailDTOS, "crisprAttempt.comment");
        assertThat(historyDetailDTO2.getOldValue(), is("crispr plan comment"));
        assertThat(historyDetailDTO2.getNewValue(), is("New Crispr Comment"));

        HistoryDetailDTO historyDetailDTO3 =
            getHistoryDetailByField(historyDetailDTOS, "crisprAttempt.totalEmbryosInjected");
        assertThat(historyDetailDTO3.getOldValue(), is("72"));
        assertThat(historyDetailDTO3.getNewValue(), is("10"));

        HistoryDetailDTO historyDetailDTO4 =
            getHistoryDetailByField(historyDetailDTOS, "crisprAttempt.totalEmbryosSurvived");
        assertThat(historyDetailDTO4.getOldValue(), is("70"));
        assertThat(historyDetailDTO4.getNewValue(), is("5"));
    }

    private HistoryDetailDTO getHistoryDetailByField(
            List<HistoryDetailDTO> historyDetailDTOS, String field)
    {
        HistoryDetailDTO historyDetailDTO = null;
        if (historyDetailDTOS != null)
        {
            historyDetailDTO = historyDetailDTOS.stream()
                .filter(x -> x.getField().equals(field))
                .findFirst().orElse(null);
        }
        return historyDetailDTO;
    }

    private void verifyGetPlantEqualsJsonIgnoringIdsAndDates(String planLink, String jsonFileName)
        throws Exception
    {
        String obtainedPlanAsString = restCaller.executeGet(planLink);
        String expectedOutputAsString = loadExpectedResponseFromResource(jsonFileName);

        JSONAssert.assertEquals(
            expectedOutputAsString,
            obtainedPlanAsString,
            new CustomComparator(JSONCompareMode.STRICT, PlanCustomizations.ignoreIdsAndDates()));
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_PLANS)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_PLANS)
    void testCreateCrisprPlan() throws Exception
    {
        sequenceResetter.syncSequence("PLAN_SEQ", "PLAN");
        sequenceResetter.syncSequence("CRISPR_ATTEMPT_REAGENT_SEQ", "CRISPR_ATTEMPT_REAGENT");
        sequenceResetter.syncSequence("GUIDE_SEQ", "GUIDE");
        sequenceResetter.syncSequence("MUTAGENESIS_DONOR_SEQ", "MUTAGENESIS_DONOR");
        sequenceResetter.syncSequence("NUCLEASE_SEQ", "NUCLEASE");
        sequenceResetter.syncSequence("GENOTYPE_PRIMER_SEQ", "GENOTYPE_PRIMER");
        sequenceResetter.syncSequence("PLAN_STATUS_STAMP_SEQ", "PLAN_STATUS_STAMP");
        sequenceResetter.syncSequence("PLAN_SUMMARY_STATUS_STAMP_SEQ", "PLAN_SUMMARY_STATUS_STAMP");
        sequenceResetter.syncSequence("HISTORY_SEQ", "HISTORY");
        sequenceResetter.syncSequence("HISTORY_DETAIL_SEQ", "HISTORY_DETAIL");

        String payload = loadExpectedResponseFromResource("crisrpPlanCreationPayload.json");

        String contentAsString =
            restCaller.executePostAndDocument("/api/plans", payload, document("plans/postCrisprPlan"));
        String planLink = LinkUtil.getSelfHrefLinkStringFromJson(contentAsString);
        verifyGetPlantEqualsJsonIgnoringIdsAndPinAndDates(planLink, "expectedCreatedCrisprPlan.json");
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_PLANS)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_PLANS)
    void testCreateEsCellPlan() throws Exception
    {
        sequenceResetter.syncSequence("PLAN_SEQ", "PLAN");
        sequenceResetter.syncSequence("PLAN_STATUS_STAMP_SEQ", "PLAN_STATUS_STAMP");
        sequenceResetter.syncSequence("PLAN_SUMMARY_STATUS_STAMP_SEQ", "PLAN_SUMMARY_STATUS_STAMP");
        sequenceResetter.syncSequence("HISTORY_SEQ", "HISTORY");
        sequenceResetter.syncSequence("HISTORY_DETAIL_SEQ", "HISTORY_DETAIL");

        String payload = loadExpectedResponseFromResource("esCellPlanCreationPayload.json");

        String contentAsString =
                restCaller.executePostAndDocument("/api/plans", payload, document("plans/postEsCellPlan"));
        String planLink = LinkUtil.getSelfHrefLinkStringFromJson(contentAsString);
        verifyGetPlantEqualsJsonIgnoringIdsAndPinAndDates(planLink, "expectedCreatedEsCellPlan.json");
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_PLANS)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_PLANS)
    void testCreateEsCellAlleleModificationPlan() throws Exception
    {
        sequenceResetter.syncSequence("PLAN_SEQ", "PLAN");
        sequenceResetter.syncSequence("PLAN_STATUS_STAMP_SEQ", "PLAN_STATUS_STAMP");
        sequenceResetter.syncSequence("PLAN_SUMMARY_STATUS_STAMP_SEQ", "PLAN_SUMMARY_STATUS_STAMP");
        sequenceResetter.syncSequence("HISTORY_SEQ", "HISTORY");
        sequenceResetter.syncSequence("HISTORY_DETAIL_SEQ", "HISTORY_DETAIL");

        String payload = loadExpectedResponseFromResource("esCellAlleleModificationPlanCreationPayload.json");
        String contentAsString =
                restCaller.executePostAndDocument("/api/plans", payload, document("plans/postEsCellAlleleModificationPlan"));
        String planLink = LinkUtil.getSelfHrefLinkStringFromJson(contentAsString);
        verifyGetPlantEqualsJsonIgnoringIdsAndPinAndDates(planLink, "expectedCreatedEsCellAlleleModificationPlan.json");
    }

    private void verifyGetPlantEqualsJsonIgnoringIdsAndPinAndDates(
        String planLink, String jsonFileName) throws Exception
    {
        String obtainedPlanAsString = restCaller.executeGet(planLink);
        String expectedOutputAsString = loadExpectedResponseFromResource(jsonFileName);

        JSONAssert.assertEquals(
            expectedOutputAsString,
            obtainedPlanAsString,
            new CustomComparator(JSONCompareMode.STRICT, PlanCustomizations.ignoreIdsAndPinAndDates()));
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_PLANS)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_PLANS)
    void testGetPlanHistory() throws Exception
    {
        String url = "/api/plans/PIN:0000000222/history";
        String expectedJson =
            getCompleteResourcePath("expectedAttemptHistoryPIN_0000000222.json");
        String obtainedJson = restCaller.executeGetAndDocument(url, documentPhenotypingStageHistory());
        resultValidator.validateObtainedMatchesJson(obtainedJson, expectedJson);
    }

    private String getCompleteResourcePath(String resourceJsonName)
    {
        return TEST_RESOURCES_FOLDER + resourceJsonName;
    }

    private ResultHandler documentPhenotypingStageHistory()
    {
        List<FieldDescriptor> historyFieldDescriptions =
            HistoryFieldsDescriptors.getHistoryFieldDescriptions();
        return document("plans/history", responseFields(historyFieldDescriptions));
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_PLANS)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_PLANS)
    void testUpdatePhenotypingAttemptPlan() throws Exception
    {
        sequenceResetter.syncSequence("PLAN_STATUS_STAMP_SEQ", "PLAN_STATUS_STAMP");
        sequenceResetter.syncSequence("PLAN_SUMMARY_STATUS_STAMP_SEQ", "PLAN_SUMMARY_STATUS_STAMP");
        sequenceResetter.syncSequence("HISTORY_SEQ", "HISTORY");
        sequenceResetter.syncSequence("HISTORY_DETAIL_SEQ", "HISTORY_DETAIL");

        String payload = loadFromResource("phenotypingPlanUpdatePayload.json");
        String url = "/api/plans/PIN:0000000003";
        String expectedJson =
            getCompleteResourcePath("expectedUpdatedPhenotypingPlanGetPIN_0000000003.json");

        String obtainedJson =
            restCaller.executePutAndDocument(url, payload, document("plans/putPhenotypingPlan"));
        ChangeResponse changeResponse = JsonHelper.fromJson(obtainedJson, ChangeResponse.class);
        verifyChangeResponsePhenotyping(changeResponse);

        String phenotypingPlanUrl = LinkUtil.getSelfHrefLinkStringFromJson(obtainedJson);
        verifyUpdatedPhenotypingPlan(phenotypingPlanUrl, expectedJson);
    }

    private void verifyChangeResponsePhenotyping(ChangeResponse changeResponse)
    {
        List<HistoryDTO> historyDTOS = changeResponse.getHistoryDTOs();
        assertThat(historyDTOS.size(), is(1));

        HistoryDTO historyDTO = historyDTOS.get(0);
        assertThat(historyDTO.getComment(), is("Plan updated"));

        List<HistoryDetailDTO> historyDetailDTOS = historyDTO.getDetails();
        assertThat(historyDetailDTOS.size(), is(1));

        HistoryDetailDTO historyDetailDTO1 =
            getHistoryDetailByField(historyDetailDTOS, "comment");
        assertThat(historyDetailDTO1.getOldValue(), is(nullValue()));
        assertThat(historyDetailDTO1.getNewValue(), is("New Plan comment"));
    }

    private void verifyUpdatedPhenotypingPlan(
            String phenotypingPlanUrl, String expectedJson) throws Exception
    {
        String obtainedPhenotypingPlan = restCaller.executeGet(phenotypingPlanUrl);
        resultValidator.validateObtainedMatchesJson(
            obtainedPhenotypingPlan, expectedJson, PlanCustomizations.ignoreIdsAndPinAndDates());
    }

    private String loadFromResource(String resourceName)
        throws IOException
    {
        String completeResourcePath = getCompleteResourcePath(resourceName);
        return TestResourceLoader.loadJsonFromResource(completeResourcePath);
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_PLANS)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_PLANS)
    void testCreatePhenotypingPlan() throws Exception
    {
        sequenceResetter.syncSequence("PLAN_SEQ", "PLAN");
        sequenceResetter.syncSequence("PLAN_STARTING_POINT_SEQ", "PLAN_STARTING_POINT");
        sequenceResetter.syncSequence("PLAN_STATUS_STAMP_SEQ", "PLAN_STATUS_STAMP");
        sequenceResetter.syncSequence("PLAN_SUMMARY_STATUS_STAMP_SEQ", "PLAN_SUMMARY_STATUS_STAMP");
        sequenceResetter.syncSequence("HISTORY_SEQ", "HISTORY");
        sequenceResetter.syncSequence("HISTORY_DETAIL_SEQ", "HISTORY_DETAIL");

        String payload = loadFromResource("phenotypingPlanCreationPayload.json");

        String url = "/api/plans/";
        String expectedJson = getCompleteResourcePath("expectedCreatedPhenotypingPlan.json");
        String obtainedJson =
            restCaller.executePostAndDocument(url, payload, document("plans/postPhenotypingPlan"));
        String phenotypingPlanUrl = LinkUtil.getSelfHrefLinkStringFromJson(obtainedJson);
        verifyCreatedPhenotypingPlan(phenotypingPlanUrl, expectedJson);
    }

    private void verifyCreatedPhenotypingPlan(
        String phenotypingPlanUrl, String expectedJson) throws Exception
    {
        String obtainedPhenotypingStage = restCaller.executeGet(phenotypingPlanUrl);
        resultValidator.validateObtainedMatchesJson(
            obtainedPhenotypingStage, expectedJson, PlanCustomizations.ignoreIdsAndPinAndDates());
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_PLANS)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_PLANS)
    void testGetOnePhenotypingPlan() throws Exception
    {
        String url = "/api/plans/PIN:0000000003";
        String expectedJson = getCompleteResourcePath("expectedPhenotypingPlanGetPIN_0000000003.json");
        String obtainedJson = restCaller.executeGetAndDocument(url, documentPhenotypingPlan());
        resultValidator.validateObtainedMatchesJson(obtainedJson, expectedJson);
    }

    private ResultHandler documentPhenotypingPlan()
    {
        List<FieldDescriptor> phenotypingPlanFieldsDescriptions = PlanFieldsDescriptors.getSharedFieldDescriptions();
        phenotypingPlanFieldsDescriptions.addAll(PlanFieldsDescriptors.getPhenotypingFieldDescriptors());
        return document("plans/getPhenotypingPlan", responseFields(phenotypingPlanFieldsDescriptions));
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_PLANS)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_PLANS)
    void testGetOneEsCellAlleleModificationPlan() throws Exception
    {
        String url = "/api/plans/PIN:0000000009";
        String expectedJson = getCompleteResourcePath("expectedEsCellAlleleModificationPlanGetPIN_0000000009.json");
        String obtainedJson = restCaller.executeGetAndDocument(url, documentEsCellAlleleModificationPlan());
        resultValidator.validateObtainedMatchesJson(obtainedJson, expectedJson);
    }

    private ResultHandler documentEsCellAlleleModificationPlan()
    {
        List<FieldDescriptor> esCellAlleleModificationPlanFieldsDescriptions =
                PlanFieldsDescriptors.getSharedFieldDescriptions();

        esCellAlleleModificationPlanFieldsDescriptions.
                addAll(PlanFieldsDescriptors.getEsCellAlleleModificationFieldDescriptors());

        return document("plans/getEsCellAlleleModificationPlan",
                responseFields(esCellAlleleModificationPlanFieldsDescriptions));
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_PLANS)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_PLANS)
    void testGetOneEsCellPlan() throws Exception
    {
        String url = "/api/plans/PIN:0000000010";
        String expectedJson = getCompleteResourcePath("expectedEsCellPlanGetPIN_0000000010.json");
        String obtainedJson = restCaller.executeGetAndDocument(url, documentEsCellPlan());
        resultValidator.validateObtainedMatchesJson(obtainedJson, expectedJson);
    }

    private ResultHandler documentEsCellPlan()
    {
        List<FieldDescriptor> esCellPlanFieldsDescriptions = PlanFieldsDescriptors.getSharedFieldDescriptions();
        esCellPlanFieldsDescriptions.addAll(PlanFieldsDescriptors.getEsCellFieldDescriptors());
        return document("plans/getEsCellPlan", responseFields(esCellPlanFieldsDescriptions));
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_PLANS)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_PLANS)
    void testUpdateEsCellAttemptPlan() throws Exception
    {
        sequenceResetter.syncSequence("PLAN_STATUS_STAMP_SEQ", "PLAN_STATUS_STAMP");
        sequenceResetter.syncSequence("PLAN_SUMMARY_STATUS_STAMP_SEQ", "PLAN_SUMMARY_STATUS_STAMP");
        sequenceResetter.syncSequence("HISTORY_SEQ", "HISTORY");
        sequenceResetter.syncSequence("HISTORY_DETAIL_SEQ", "HISTORY_DETAIL");

        String payload = loadFromResource("esCellPlanUpdatePayload.json");
        String url = "/api/plans/PIN:0000000010";
        String expectedJson =
                getCompleteResourcePath("expectedUpdatedEsCellPlanGetPIN_0000000010.json");
        String obtainedJson =
                restCaller.executePutAndDocument(url, payload, document("plans/putEsCellPlan"));

        ChangeResponse changeResponse = JsonHelper.fromJson(obtainedJson, ChangeResponse.class);
        verifyChangeResponseEsCell(changeResponse);

        String attemptLink = LinkUtil.getSelfHrefLinkStringFromJson(obtainedJson);
        verifyUpdatedEsCellPlan(attemptLink, expectedJson);
    }

    private void verifyChangeResponseEsCell(ChangeResponse changeResponse)
    {
        List<HistoryDTO> historyDTOS = changeResponse.getHistoryDTOs();
        assertThat(historyDTOS.size(), is(1));

        HistoryDTO historyDTO = historyDTOS.get(0);
        assertThat(historyDTO.getComment(), is("Plan updated"));

        List<HistoryDetailDTO> historyDetailDTOS = historyDTO.getDetails();
        assertThat(historyDetailDTOS.size(), is(4));

        HistoryDetailDTO historyDetailDTO1 =
                getHistoryDetailByField(historyDetailDTOS, "comment");
        assertThat(historyDetailDTO1.getOldValue(), is(nullValue()));
        assertThat(historyDetailDTO1.getNewValue(), is("New Plan comment"));

        HistoryDetailDTO historyDetailDTO2 =
                getHistoryDetailByField(historyDetailDTOS, "esCellAttempt.cassetteTransmissionVerifiedAutoComplete");
        assertThat(historyDetailDTO2.getOldValue(), is("false"));
        assertThat(historyDetailDTO2.getNewValue(), is("true"));

        HistoryDetailDTO historyDetailDTO3 =
                getHistoryDetailByField(historyDetailDTOS, "esCellAttempt.totalF1MiceFromMatings");
        assertThat(historyDetailDTO3.getOldValue(), is("52"));
        assertThat(historyDetailDTO3.getNewValue(), is("32"));

        HistoryDetailDTO historyDetailDTO4 =
                getHistoryDetailByField(historyDetailDTOS, "esCellAttempt.totalTransferred");
        assertThat(historyDetailDTO4.getOldValue(), is("44"));
        assertThat(historyDetailDTO4.getNewValue(), is("40"));
    }

    private void verifyUpdatedEsCellPlan(
            String esCellPlanUrl, String expectedJson) throws Exception
    {
        String obtainedEsCellPlan = restCaller.executeGet(esCellPlanUrl);
        resultValidator.validateObtainedMatchesJson(
                obtainedEsCellPlan, expectedJson, PlanCustomizations.ignoreIdsAndPinAndDates());
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_PLANS)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_PLANS)
    void testUpdateEsCellAlleleModificationAttemptPlan() throws Exception
    {
        sequenceResetter.syncSequence("PLAN_STATUS_STAMP_SEQ", "PLAN_STATUS_STAMP");
        sequenceResetter.syncSequence("PLAN_SUMMARY_STATUS_STAMP_SEQ", "PLAN_SUMMARY_STATUS_STAMP");
        sequenceResetter.syncSequence("HISTORY_SEQ", "HISTORY");
        sequenceResetter.syncSequence("HISTORY_DETAIL_SEQ", "HISTORY_DETAIL");

        String payload = loadFromResource("esCellAlleleModificationPlanUpdatePayload.json");
        String url = "/api/plans/PIN:0000000009";
        String expectedJson =
                getCompleteResourcePath("expectedUpdatedEsCellAlleleModificationPlanGetPIN_0000000009.json");
        String obtainedJson =
                restCaller.executePutAndDocument(url, payload, document("plans/putEsCellAlleleModificationPlan"));

        ChangeResponse changeResponse = JsonHelper.fromJson(obtainedJson, ChangeResponse.class);
        verifyChangeResponseEsCellAlleleModification(changeResponse);

        String attemptLink = LinkUtil.getSelfHrefLinkStringFromJson(obtainedJson);
        verifyUpdatedEsCellAlleleModificationPlan(attemptLink, expectedJson);
    }

    private void verifyChangeResponseEsCellAlleleModification(ChangeResponse changeResponse)
    {
        List<HistoryDTO> historyDTOS = changeResponse.getHistoryDTOs();
        assertThat(historyDTOS.size(), is(1));

        HistoryDTO historyDTO = historyDTOS.get(0);
        assertThat(historyDTO.getComment(), is("Plan updated"));

        List<HistoryDetailDTO> historyDetailDTOS = historyDTO.getDetails();
        assertThat(historyDetailDTOS.size(), is(2));

        HistoryDetailDTO historyDetailDTO1 =
                getHistoryDetailByField(historyDetailDTOS, "esCellAlleleModificationAttempt.numberOfCreMatingsSuccessful");
        assertThat(historyDetailDTO1.getOldValue(), is("1"));
        assertThat(historyDetailDTO1.getNewValue(), is("3"));

        HistoryDetailDTO historyDetailDTO2 =
                getHistoryDetailByField(historyDetailDTOS, "esCellAlleleModificationAttempt.tatCre");
        assertThat(historyDetailDTO2.getOldValue(), is("false"));
        assertThat(historyDetailDTO2.getNewValue(), is("true"));
    }

    private void verifyUpdatedEsCellAlleleModificationPlan(
            String esCellPlanUrl, String expectedJson) throws Exception
    {
        String obtainedEsCellPlan = restCaller.executeGet(esCellPlanUrl);
        resultValidator.validateObtainedMatchesJson(
                obtainedEsCellPlan, expectedJson, PlanCustomizations.ignoreIdsAndPinAndDates());
    }

}
