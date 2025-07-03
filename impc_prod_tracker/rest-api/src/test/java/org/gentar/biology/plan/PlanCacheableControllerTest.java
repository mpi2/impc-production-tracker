package org.gentar.biology.plan;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import java.io.IOException;
import org.gentar.framework.ControllerTestTemplate;
import org.gentar.framework.RestCaller;
import org.gentar.framework.ResultValidator;
import org.gentar.framework.SequenceResetter;
import org.gentar.framework.TestResourceLoader;
import org.gentar.framework.asserts.json.PlanCustomizations;
import org.gentar.framework.db.DBSetupFilesPaths;
import org.gentar.helpers.LinkUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;

class PlanCacheableControllerTest extends ControllerTestTemplate
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

    /**
     * This Test will Fails when getWorkUnitByName() methode is Cashed.
     * Dont write '@Cacheable("workUnitsNames")' annotation.
     * In the second plan creation you will get  'give  detached entity passed to persist: org.gentar.organization.work_unit.WorkUnit' error.
     */
    @Disabled
    void testCreatePhenotypingPlanForDetachedWorkUnitError() throws Exception
    {
        sequenceResetter.syncSequence("PLAN_SEQ", "PLAN");
        sequenceResetter.syncSequence("PLAN_STARTING_POINT_SEQ", "PLAN_STARTING_POINT");
        sequenceResetter.syncSequence("PLAN_STATUS_STAMP_SEQ", "PLAN_STATUS_STAMP");
        sequenceResetter.syncSequence("PLAN_SUMMARY_STATUS_STAMP_SEQ", "PLAN_SUMMARY_STATUS_STAMP");
        sequenceResetter.syncSequence("HISTORY_SEQ", "HISTORY");
        sequenceResetter.syncSequence("HISTORY_DETAIL_SEQ", "HISTORY_DETAIL");

        String payload = loadFromResource("phenotypingPlanCreationPayload.json");

        String expectedJson = getCompleteResourcePath("expectedCreatedPhenotypingPlan.json");
        String obtainedJson =
            restCaller.executePostAndDocument("/api/plans/", payload, document("plans/postPhenotypingPlanCaheable"));
        String phenotypingPlanUrl = LinkUtil.getSelfHrefLinkStringFromJson(obtainedJson);
        verifyCreatedPhenotypingPlan(phenotypingPlanUrl, expectedJson);
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



    private void verifyCreatedPhenotypingPlan(
        String phenotypingPlanUrl, String expectedJson) throws Exception
    {
        String obtainedPhenotypingStage = restCaller.executeGet(phenotypingPlanUrl);
        resultValidator.validateObtainedMatchesJson(
            obtainedPhenotypingStage, expectedJson, PlanCustomizations.ignoreIdsAndPinAndDates());
    }

}
