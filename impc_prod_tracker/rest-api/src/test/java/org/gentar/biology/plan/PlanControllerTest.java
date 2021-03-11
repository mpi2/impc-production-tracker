package org.gentar.biology.plan;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.gentar.audit.history.HistoryFieldsDescriptors;
import org.gentar.biology.ChangeResponse;
import org.gentar.biology.plan.attempt.crispr.CrisprAttemptDTO;
import org.gentar.common.history.HistoryDTO;
import org.gentar.common.history.HistoryDetailDTO;
import org.gentar.framework.*;
import org.gentar.framework.asserts.json.ChangeResponseCustomizations;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.io.IOException;
import java.util.List;

import static org.gentar.util.JsonHelper.toJson;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        PlanUpdateDTO planUpdateDTO = getPlanToUpdate();
        editCrisprPlanWithNewValues(planUpdateDTO);

        String contentAsString =
            restCaller.executePutAndDocument("/api/plans/PIN:0000000001", toJson(planUpdateDTO),
                    document("plans/putCrisprPlan"));
        ChangeResponse changeResponse = JsonHelper.fromJson(contentAsString, ChangeResponse.class);
        verifyChangeResponse(changeResponse);

        String projectLink = LinkUtil.getSelfHrefLinkStringFromJson(contentAsString);

        verifyGetPlanEqualsJson(projectLink, "expectedUpdatedPlanGetPIN_0000000001.json");
    }

    private void verifyGetPlanEqualsJson(String planLink, String jsonFileName) throws Exception
    {
        ResultActions callGetWithObtainedUrl = mvc().perform(MockMvcRequestBuilders
            .get(planLink)
            .header("Authorization", accessToken))
            .andExpect(status().isOk());
        MvcResult obtainedProject = callGetWithObtainedUrl.andReturn();
        String obtainedPlanAsString = obtainedProject.getResponse().getContentAsString();
        String expectedOutputAsString = loadExpectedResponseFromResource(jsonFileName);

        JSONAssert.assertEquals(expectedOutputAsString, obtainedPlanAsString,
                new CustomComparator(JSONCompareMode.STRICT, PlanCustomizations.ignoreIdsAndDates()));
    }

    private void editCrisprPlanWithNewValues(PlanUpdateDTO planUpdateDTO)
    {
        PlanBasicDataDTO planBasicDataDTO = planUpdateDTO.getPlanBasicDataDTO();
        CrisprAttemptDTO crisprAttemptDTO = planBasicDataDTO.getCrisprAttemptDTO();
        crisprAttemptDTO.setComment("New Crispr Comment");
        crisprAttemptDTO.setExperimental(false);
        crisprAttemptDTO.setMiExternalRef("New external reference");
        crisprAttemptDTO.setTotalEmbryosInjected(10);
        crisprAttemptDTO.setTotalEmbryosSurvived(5);
        PlanCommonDataDTO planCommonDataDTO = planBasicDataDTO.getPlanCommonDataDTO();
        planCommonDataDTO.setComment("New Plan comment");
    }

    private PlanUpdateDTO getPlanToUpdate() throws IOException
    {
        String originalPlan =
            loadExpectedResponseFromResource("expectedUpdatedPlanGetPIN_0000000001.json");
        return JsonHelper.fromJson(originalPlan, PlanUpdateDTO.class);
    }

    private void verifyChangeResponse(ChangeResponse changeResponse)
    {
        List<HistoryDTO> historyDTOS = changeResponse.getHistoryDTOs();
        assertThat(historyDTOS.size(), is(1));

        HistoryDTO historyDTO = historyDTOS.get(0);
        assertThat(historyDTO.getComment(), is("Plan updated"));

        List<HistoryDetailDTO> historyDetailDTOS = historyDTO.getDetails();
        assertThat(historyDetailDTOS.size(), is(12));

        HistoryDetailDTO historyDetailDTO1 =
            getHistoryDetailByField(historyDetailDTOS, "crisprAttempt.comment");
        assertThat(historyDetailDTO1.getOldValue(), is("crispr plan comment"));
        assertThat(historyDetailDTO1.getNewValue(), is("New Crispr Comment"));

        HistoryDetailDTO historyDetailDTO2 =
            getHistoryDetailByField(historyDetailDTOS, "crisprAttempt.totalEmbryosInjected");
        assertThat(historyDetailDTO2.getOldValue(), is("72"));
        assertThat(historyDetailDTO2.getNewValue(), is("10"));

        HistoryDetailDTO historyDetailDTO3 =
            getHistoryDetailByField(historyDetailDTOS, "crisprAttempt.miExternalRef");
        assertThat(historyDetailDTO3.getOldValue(), is(nullValue()));
        assertThat(historyDetailDTO3.getNewValue(), is("New external reference"));

        HistoryDetailDTO historyDetailDTO4 =
            getHistoryDetailByField(historyDetailDTOS, "crisprAttempt.totalEmbryosSurvived");
        assertThat(historyDetailDTO4.getOldValue(), is("70"));
        assertThat(historyDetailDTO4.getNewValue(), is("5"));

        HistoryDetailDTO historyDetailDTO5 =
            getHistoryDetailByField(historyDetailDTOS, "crisprAttempt.experimental");
        assertThat(historyDetailDTO5.getOldValue(), is("true"));
        assertThat(historyDetailDTO5.getNewValue(), is("false"));

        HistoryDetailDTO historyDetailDTO6 =
            getHistoryDetailByField(historyDetailDTOS, "comment");
        assertThat(historyDetailDTO6.getOldValue(), is(nullValue()));
        assertThat(historyDetailDTO6.getNewValue(), is("New Plan comment"));
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

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_PLANS)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_PLANS)
    void testUpdateStateMachineCrisprPlan() throws Exception
    {
        sequenceResetter.syncSequence("PLAN_STATUS_STAMP_SEQ", "PLAN_STATUS_STAMP");
        sequenceResetter.syncSequence("PLAN_SUMMARY_STATUS_STAMP_SEQ", "PLAN_SUMMARY_STATUS_STAMP");
        sequenceResetter.syncSequence("HISTORY_SEQ", "HISTORY");
        sequenceResetter.syncSequence("HISTORY_DETAIL_SEQ", "HISTORY_DETAIL");

        PlanUpdateDTO planUpdateDTO = getPlanToUpdate();
        setAbortAction(planUpdateDTO);

        String contentAsString =
                restCaller.executePutAndDocument("/api/plans/PIN:0000000001", toJson(planUpdateDTO),
                        document("plans/putCrisprPlan"));
        ChangeResponse changeResponse = JsonHelper.fromJson(contentAsString, ChangeResponse.class);

        List<HistoryDTO> historyDTOS = changeResponse.getHistoryDTOs();
        assertThat(historyDTOS.size(), is(1));

        HistoryDTO historyDTO = historyDTOS.get(0);
        assertThat(historyDTO.getComment(), is("Plan updated"));

        List<HistoryDetailDTO> historyDetailDTOS = historyDTO.getDetails();
        assertThat(historyDetailDTOS.size(), is(3));

        HistoryDetailDTO historyDetailDTO1 =
            getHistoryDetailByField(historyDetailDTOS, "status.name");
        assertThat(historyDetailDTO1.getOldValue(), is("Founder Obtained"));
        assertThat(historyDetailDTO1.getNewValue(), is("Attempt Aborted"));

        HistoryDetailDTO historyDetailDTO2 =
            getHistoryDetailByField(historyDetailDTOS, "summaryStatus.name");
        assertThat(historyDetailDTO2.getOldValue(), is("Founder Obtained"));
        assertThat(historyDetailDTO2.getNewValue(), is("Attempt Aborted"));

        String planLink = LinkUtil.getSelfHrefLinkStringFromJson(contentAsString);

        verifyGetPlantEqualsJsonIgnoringIdsAndDates(
            planLink, "expectedAbortedPlanGetPIN_0000000001.json");
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

    private void setAbortAction(PlanUpdateDTO planUpdateDTO)
    {
        planUpdateDTO.getStatusTransitionDTO().setActionToExecute("abortWhenFounderObtained");
        PlanBasicDataDTO planBasicDataDTO = planUpdateDTO.getPlanBasicDataDTO();
        planBasicDataDTO.setCrisprAttemptDTO(null);
        planUpdateDTO.setPlanBasicDataDTO(planBasicDataDTO);
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
        String url = "/api/plans/PIN:0000000002/history";
        String expectedJson =
            getCompleteResourcePath("expectedAttemptHistoryPIN_0000000002.json");
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
}
