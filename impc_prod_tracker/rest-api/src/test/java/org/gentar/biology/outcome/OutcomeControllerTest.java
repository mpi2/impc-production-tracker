package org.gentar.biology.outcome;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.gentar.framework.ControllerTestTemplate;
import org.gentar.framework.SequenceResetter;
import org.gentar.framework.TestResourceLoader;
import org.gentar.framework.asserts.json.OutcomeCustomizations;
import org.gentar.framework.db.DBSetupFilesPaths;
import org.gentar.helpers.LinkUtil;
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

import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OutcomeControllerTest extends ControllerTestTemplate
{
    private static final String TEST_RESOURCES_FOLDER = INTEGRATION_TESTS_RESOURCE_PATH + "outcomes/";

    @Autowired
    private SequenceResetter sequenceResetter;

    @BeforeEach
    void setUp() throws Exception
    {
        setTestUserSecurityContext();
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_OUTCOMES)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_OUTCOMES)
    void testGetAllOutcomesInPlan() throws Exception
    {
        ResultActions resultActions = mvc().perform(MockMvcRequestBuilders
            .get("/api/plans/PIN:0000000001/outcomes")
            .header("Authorization", accessToken))
            .andExpect(status().isOk())
            .andDo(document("outcomes/allOutcomes"));

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        System.out.println(contentAsString);
        String expectedOutputAsString =
            loadFromResource("expectedAllOutcomes.json");

        JSONAssert.assertEquals(expectedOutputAsString, contentAsString, JSONCompareMode.STRICT);
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_OUTCOMES)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_OUTCOMES)
    void testGetColonyOutcomeInPlan() throws Exception
    {
        ResultActions resultActions = mvc().perform(MockMvcRequestBuilders
            .get("/api/plans/PIN:0000000001/outcomes/TPO:000000000001")
            .header("Authorization", accessToken))
            .andExpect(status().isOk())
            .andDo(documentColonyOutcome());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        System.out.println(contentAsString);
        String expectedOutputAsString =
            loadFromResource("expectedColonyOutcomeTPO_000000000001.json");

        JSONAssert.assertEquals(expectedOutputAsString, contentAsString, JSONCompareMode.STRICT);
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
    void testGetSpecimenOutcomeInPlan() throws Exception
    {
        ResultActions resultActions = mvc().perform(MockMvcRequestBuilders
            .get("/api/plans/PIN:0000000001/outcomes/TPO:000000000004")
            .header("Authorization", accessToken))
            .andExpect(status().isOk())
            .andDo(documentSpecimenOutcome());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        System.out.println(contentAsString);
        String expectedOutputAsString =
            loadFromResource("expectedSpecimenOutcomeTPO_000000000004.json");

        JSONAssert.assertEquals(expectedOutputAsString, contentAsString, JSONCompareMode.STRICT);
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

        String payload = loadFromResource("colonyOutcomeCreationPayload.json");

        ResultActions resultActions = mvc().perform(MockMvcRequestBuilders
            .post("/api/plans/PIN:0000000001/outcomes")
            .header("Authorization", accessToken)
            .content(payload)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("outcomes/postColonyOutcome"));

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        String planLink = LinkUtil.getSelfHrefLinkStringFromJson(contentAsString);
        verifyGetOutcomeEqualsJsonIgnoringIdsAndPinAndDates(planLink, "expectedCreatedColonyOutcome.json");
    }

    private String loadFromResource(String resourceName)
        throws IOException
    {
        String completeResourcePath = TEST_RESOURCES_FOLDER + resourceName;
        return TestResourceLoader.loadJsonFromResource(completeResourcePath);
    }

    private void verifyGetOutcomeEqualsJsonIgnoringIdsAndPinAndDates(
        String planLink, String jsonFileName)
        throws Exception
    {
        ResultActions callGetWithObtainedUrl = mvc().perform(MockMvcRequestBuilders
            .get(planLink)
            .header("Authorization", accessToken))
            .andExpect(status().isOk());
        MvcResult obtainedProject = callGetWithObtainedUrl.andReturn();
        String obtainedPlanAsString = obtainedProject.getResponse().getContentAsString();
        System.out.println(obtainedPlanAsString);
        String expectedOutputAsString = loadFromResource(jsonFileName);

        JSONAssert.assertEquals(
            expectedOutputAsString,
            obtainedPlanAsString,
            new CustomComparator(
                JSONCompareMode.STRICT, OutcomeCustomizations.ignoreIdsAndTpoAndDates()));

        String mutationLink = LinkUtil.getCustomHrefLinkStringFromJson(obtainedPlanAsString, "mutations");
        System.out.println(mutationLink);
        verifyMutationIsExpectedIgnoringIds(mutationLink, "");
    }


    private void verifyMutationIsExpectedIgnoringIds(
        String mutationLink, String jsonFileName)
        throws Exception
    {
        ResultActions callGetWithObtainedUrl = mvc().perform(MockMvcRequestBuilders
            .get(mutationLink)
            .header("Authorization", accessToken))
            .andExpect(status().isOk());
        MvcResult obtainedMutation = callGetWithObtainedUrl.andReturn();
        String obtainedMutationAsString = obtainedMutation.getResponse().getContentAsString();
        //TODO: Check object is the expected
    }
}