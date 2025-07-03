package org.gentar.biology.mutation;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.gentar.audit.history.HistoryFieldsDescriptors;
import org.gentar.framework.ControllerTestTemplate;
import org.gentar.framework.RestCaller;
import org.gentar.framework.ResultValidator;
import org.gentar.framework.SequenceResetter;
import org.gentar.framework.TestResourceLoader;
import org.gentar.framework.asserts.json.ChangeResponseCustomizations;
import org.gentar.framework.db.DBSetupFilesPaths;
import org.gentar.helpers.LinkUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.ResultHandler;
import java.io.IOException;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

class MutationControllerTest extends ControllerTestTemplate
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
    void testGetAllMutationsInOutcome() throws Exception
    {
        String url = "/api/plans/PIN:0000000001/outcomes/TPO:000000000002/mutations";
        String expectedJson = getCompleteResourcePath("expectedMutationCollection.json");
        String obtainedJson = restCaller.executeGet(url);
        resultValidator.validateObtainedMatchesJson(obtainedJson, expectedJson);
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_MUTATIONS)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_MUTATIONS)
    void testGetMutationInOutcome() throws Exception
    {
        String url = "/api/plans/PIN:0000000001/outcomes/TPO:000000000002/mutations/MIN:000000000002";
        String expectedJson = getCompleteResourcePath("expectedMutationMIN_000000000002.json");
        String obtainedJson = restCaller.executeGetAndDocument(url, documentMutation());
        resultValidator.validateObtainedMatchesJson(obtainedJson, expectedJson);
    }

    private ResultHandler documentMutation()
    {
        List<FieldDescriptor> mutationFieldsDescriptions =
            MutationFieldsDescriptors.getMutationFieldsDescriptions();
        return document("mutations/mutation", responseFields(mutationFieldsDescriptions));
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_MUTATIONS)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_MUTATIONS)
    void testGetMutationHistoryInOutcome() throws Exception
    {
        String url = "/api/plans/PIN:0000000001/outcomes/TPO:000000000002/mutations/MIN:000000000002/history";
        String expectedJson = getCompleteResourcePath("expectedMutationHistoryMIN_000000000002.json");
        String obtainedJson = restCaller.executeGetAndDocument(url, documentMutationHistory());
        resultValidator.validateObtainedMatchesJson(obtainedJson, expectedJson);
    }

    private ResultHandler documentMutationHistory()
    {
        List<FieldDescriptor> historyFieldDescriptions =
            HistoryFieldsDescriptors.getHistoryFieldDescriptions();
        return document("mutations/mutation/history", responseFields(historyFieldDescriptions));
    }

    @Test
    @DatabaseSetup(DBSetupFilesPaths.MULTIPLE_MUTATIONS)
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilesPaths.MULTIPLE_MUTATIONS)
    void testUpdateMutation() throws Exception
    {
        sequenceResetter.syncSequence("MUTATION_QC_RESULT_SEQ", "MUTATION_QC_RESULT");
        sequenceResetter.syncSequence("HISTORY_SEQ", "HISTORY");
        sequenceResetter.syncSequence("HISTORY_DETAIL_SEQ", "HISTORY_DETAIL");
        sequenceResetter.syncSequence("GENE_SEQ", "GENE");

        String payload = loadFromResource("mutationMIN_000000000002UpdatePayload.json");
        String url = "/api/plans/PIN:0000000001/outcomes/TPO:000000000002/mutations/MIN:000000000002/";
        String obtainedJson =
            restCaller.executePutAndDocument(url, payload, document("mutations/mutation/putMutation"));
        verifyUpdateResponse(obtainedJson, "expectedResponseUpdateMutationMIN_000000000002.json");
        String mutationUrl = LinkUtil.getSelfHrefLinkStringFromJson(obtainedJson);
        verifyUpdatedMutation(mutationUrl, "expectedMutationMIN_000000000002AfterUpdate.json");
    }


    private void verifyUpdateResponse(String obtainedJson, String expectedJsonPath) throws Exception
    {
        String expectedJsonCompletePath = getCompleteResourcePath(expectedJsonPath);
        resultValidator.validateObtainedMatchesJson(
            obtainedJson, expectedJsonCompletePath, ChangeResponseCustomizations.ignoreDates());
    }

    void verifyUpdatedMutation(String mutationUrl, String expectedJsonPath) throws Exception
    {
        String expectedJsonCompletePath = getCompleteResourcePath(expectedJsonPath);
        String obtainedMutation = restCaller.executeGet(mutationUrl);
        resultValidator.validateObtainedMatchesJson(obtainedMutation, expectedJsonCompletePath);
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
