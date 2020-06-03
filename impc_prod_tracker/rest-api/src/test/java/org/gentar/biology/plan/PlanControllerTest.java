package org.gentar.biology.plan;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.gentar.biology.ChangeResponse;
import org.gentar.biology.plan.attempt.crispr.CrisprAttemptDTO;
import org.gentar.common.history.HistoryDTO;
import org.gentar.common.history.HistoryDetailDTO;
import org.gentar.framework.ControllerTestTemplate;
import org.gentar.framework.SequenceResetter;
import org.gentar.framework.TestResourceLoader;
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
import org.springframework.http.MediaType;
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

    @Autowired
    private SequenceResetter sequenceResetter;

    PlanCustomizations planCustomizations = new PlanCustomizations();

    @BeforeEach
    public void setup() throws Exception
    {
        setTestUserSecurityContext();
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_PLANS)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_PLANS)
    void testGetOneCrisprPlan() throws Exception
    {
        ResultActions resultActions = mvc().perform(MockMvcRequestBuilders
            .get("/api/plans/PIN:0000000001")
            .header("Authorization", accessToken))
            .andExpect(status().isOk())
            .andDo(documentCrisprPlan());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        String expectedOutputAsString =
            loadExpectedResponseFromResource("expectedPlanGetPIN_0000000001.json");

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
        ResultActions resultActions = mvc().perform(MockMvcRequestBuilders
            .get("/api/plans")
            .header("Authorization", accessToken))
            .andExpect(status().isOk())
            .andDo(document("plans/allPlans"));

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        String expectedOutputAsString =
            loadExpectedResponseFromResource("expectedAllPlans.json");

        JSONAssert.assertEquals(expectedOutputAsString, contentAsString, JSONCompareMode.STRICT);
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_PLANS)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_PLANS)
    void testGetFilteredPlans() throws Exception
    {
        ResultActions resultActions = mvc().perform(MockMvcRequestBuilders
            .get("/api/plans?statusName=Founder Obtained&attemptTypeName=crispr")
            .header("Authorization", accessToken))
            .andExpect(status().isOk())
            .andDo(document("plans/filteredPlans"));

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        System.out.println(contentAsString);
        String expectedOutputAsString =
            loadExpectedResponseFromResource("expectedFilteredPlans.json");

        JSONAssert.assertEquals(expectedOutputAsString, contentAsString, JSONCompareMode.STRICT);
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_PLANS)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_PLANS)
    void testUpdateCrisprPlan() throws Exception
    {
        PlanUpdateDTO planUpdateDTO = getPlanToUpdate();
        editCrisprPlanWithNewValues(planUpdateDTO);

        ResultActions resultActions = mvc().perform(MockMvcRequestBuilders
            .put("/api/plans/PIN:0000000001")
            .header("Authorization", accessToken)
            .content(toJson(planUpdateDTO))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("plans/putCrisprPlan"));

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        System.out.println(contentAsString);
        ChangeResponse changeResponse = JsonHelper.fromJson(contentAsString, ChangeResponse.class);
        verifyChangeResponse(changeResponse);

        String projectLink = LinkUtil.getSelfHrefLinkStringFromJson(contentAsString);

        verifyGetPlantEqualsJson(projectLink, "expectedUpdatedPlanGetPIN_0000000001.json");
    }

    private void verifyGetPlantEqualsJson(String planLink, String jsonFileName) throws Exception
    {
        ResultActions callGetWithObtainedUrl = mvc().perform(MockMvcRequestBuilders
            .get(planLink)
            .header("Authorization", accessToken))
            .andExpect(status().isOk());
        MvcResult obtainedProject = callGetWithObtainedUrl.andReturn();
        String obtainedPlanAsString = obtainedProject.getResponse().getContentAsString();
        System.out.println(obtainedPlanAsString);
        String expectedOutputAsString =
            loadExpectedResponseFromResource(jsonFileName);

        JSONAssert.assertEquals(expectedOutputAsString, obtainedPlanAsString, JSONCompareMode.STRICT);
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
            loadExpectedResponseFromResource("expectedPlanGetPIN_0000000001.json");
        return JsonHelper.fromJson(originalPlan, PlanUpdateDTO.class);
    }

    private void verifyChangeResponse(ChangeResponse changeResponse)
    {
        List<HistoryDTO> historyDTOS = changeResponse.getHistoryDTOs();
        assertThat(historyDTOS.size(), is(1));

        HistoryDTO historyDTO = historyDTOS.get(0);
        assertThat(historyDTO.getComment(), is("Plan updated"));

        List<HistoryDetailDTO> historyDetailDTOS = historyDTO.getDetails();
        assertThat(historyDetailDTOS.size(), is(6));

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
        PlanUpdateDTO planUpdateDTO = getPlanToUpdate();
        setAbortAction(planUpdateDTO);

        ResultActions resultActions = mvc().perform(MockMvcRequestBuilders
            .put("/api/plans/PIN:0000000001")
            .header("Authorization", accessToken)
            .content(toJson(planUpdateDTO))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("plans/putCrisprPlanStateMachine"));

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        System.out.println(contentAsString);
        ChangeResponse changeResponse = JsonHelper.fromJson(contentAsString, ChangeResponse.class);

        List<HistoryDTO> historyDTOS = changeResponse.getHistoryDTOs();
        assertThat(historyDTOS.size(), is(1));

        HistoryDTO historyDTO = historyDTOS.get(0);
        assertThat(historyDTO.getComment(), is("Plan updated"));

        List<HistoryDetailDTO> historyDetailDTOS = historyDTO.getDetails();
        assertThat(historyDetailDTOS.size(), is(2));

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
        ResultActions callGetWithObtainedUrl = mvc().perform(MockMvcRequestBuilders
            .get(planLink)
            .header("Authorization", accessToken))
            .andExpect(status().isOk());
        MvcResult obtainedProject = callGetWithObtainedUrl.andReturn();
        String obtainedPlanAsString = obtainedProject.getResponse().getContentAsString();
        String expectedOutputAsString = loadExpectedResponseFromResource(jsonFileName);

        JSONAssert.assertEquals(
            expectedOutputAsString,
            obtainedPlanAsString,
            new CustomComparator(JSONCompareMode.STRICT, planCustomizations.ignoreIdsAndDates()));
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
    void testCreatCrisprPlan() throws Exception
    {
        sequenceResetter.syncSequence("PLAN_SEQ", "PLAN");
        sequenceResetter.syncSequence("CRISPR_ATTEMPT_REAGENT_SEQ", "CRISPR_ATTEMPT_REAGENT");
        sequenceResetter.syncSequence("GUIDE_SEQ", "GUIDE");
        sequenceResetter.syncSequence("MUTAGENESIS_DONOR_SEQ", "MUTAGENESIS_DONOR");
        sequenceResetter.syncSequence("NUCLEASE_SEQ", "NUCLEASE");
        sequenceResetter.syncSequence("GENOTYPE_PRIMER_SEQ", "GENOTYPE_PRIMER");

        String payload = loadExpectedResponseFromResource("crisrpPlanCreationPayload.json");

        ResultActions resultActions = mvc().perform(MockMvcRequestBuilders
            .post("/api/plans")
            .header("Authorization", accessToken)
            .content((payload))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("plans/postCrisprPlan"));

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        String planLink = LinkUtil.getSelfHrefLinkStringFromJson(contentAsString);
        verifyGetPlantEqualsJsonIgnoringIdsAndPinAndDates(planLink, "expectedCreatedCrisprPlan.json");
    }

    private void verifyGetPlantEqualsJsonIgnoringIdsAndPinAndDates(
        String planLink, String jsonFileName)
    throws Exception
    {
        ResultActions callGetWithObtainedUrl = mvc().perform(MockMvcRequestBuilders
            .get(planLink)
            .header("Authorization", accessToken))
            .andExpect(status().isOk());
        MvcResult obtainedProject = callGetWithObtainedUrl.andReturn();
        String obtainedPlanAsString = obtainedProject.getResponse().getContentAsString();
        String expectedOutputAsString = loadExpectedResponseFromResource(jsonFileName);

        JSONAssert.assertEquals(
            expectedOutputAsString,
            obtainedPlanAsString,
            new CustomComparator(JSONCompareMode.STRICT, planCustomizations.ignoreIdsAndPinAndDates()));
    }
}
