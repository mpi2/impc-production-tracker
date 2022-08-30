package org.gentar.biology.plan.attempt.phenotyping.stage;

import static org.gentar.mockdata.MockData.PIN_000000001;
import static org.gentar.mockdata.MockData.PSN_000000001;
import static org.gentar.mockdata.MockData.TEST_COMMENT;
import static org.gentar.mockdata.MockData.TEST_NAME;
import static org.gentar.mockdata.MockData.historyMockData;
import static org.gentar.mockdata.MockData.phenotypingAttemptMockData;
import static org.gentar.mockdata.MockData.phenotypingStageMockData;
import static org.gentar.mockdata.MockData.phenotypingStageTypeMockData;
import static org.gentar.mockdata.MockData.planMockData;
import static org.gentar.mockdata.MockData.processEventMockData;
import static org.gentar.mockdata.MockData.transitionEvaluationMockData;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;

import java.util.Collection;
import java.util.List;
import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanService;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttempt;
import org.gentar.biology.plan.attempt.phenotyping.stage.type.PhenotypingStageType;
import org.gentar.biology.plan.attempt.phenotyping.stage.type.PhenotypingStageTypeRepository;
import org.gentar.biology.project.ProjectRepository;
import org.gentar.exceptions.NotFoundException;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.security.abac.Resource;
import org.gentar.security.abac.ResourceAccessChecker;
import org.gentar.security.permissions.Actions;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionAvailabilityEvaluator;
import org.gentar.statemachine.TransitionEvaluation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PhenotypingStageServiceImplTest {


    PhenotypingStageServiceImpl testInstance;

    @Mock
    private PhenotypingStageStateMachineResolver phenotypingStageStateMachineResolver;
    @Mock
    private TransitionAvailabilityEvaluator transitionAvailabilityEvaluator;
    @Mock
    private PhenotypingStageRepository phenotypingStageRepository;
    @Mock
    private PhenotypingStageTypeRepository phenotypingStageTypeRepository;
    @Mock
    private PlanService planService;
    @Mock
    private ResourceAccessChecker<PhenotypingStage> resourceAccessChecker;
    @Mock
    private PhenotypingStageCreator phenotypingStageCreator;
    @Mock
    private PhenotypingStageUpdater phenotypingStageUpdater;
    @Mock
    private HistoryService<PhenotypingStage> historyService;


    public static final String READ_PHENOTYPING_STAGE_ACTION = "READ_PHENOTYPING_STAGE";

    @BeforeEach
    void setUp() {
        testInstance = new PhenotypingStageServiceImpl(phenotypingStageStateMachineResolver,
            transitionAvailabilityEvaluator,
            phenotypingStageRepository,
            phenotypingStageTypeRepository,
            planService,
            resourceAccessChecker,
            phenotypingStageCreator,
            phenotypingStageUpdater,
            historyService);

    }

    @Test
    void evaluateNextTransitions() {

        lenient().when(
            phenotypingStageStateMachineResolver
                .getAvailableTransitionsByEntityStatus(Mockito.any(PhenotypingStage.class)))
            .thenReturn(List.of(processEventMockData()));
        lenient().when(transitionAvailabilityEvaluator
            .evaluate(Mockito.any(List.class), Mockito.any(PhenotypingStage.class)))
            .thenReturn(List.of(transitionEvaluationMockData()));
        List<TransitionEvaluation> transitionEvaluations =
            testInstance.evaluateNextTransitions(phenotypingStageMockData());

        assertEquals(transitionEvaluations.size(), 1);
    }

    @Test
    void getProcessEventByName() {

        lenient().when(
            phenotypingStageStateMachineResolver
                .getProcessEventByActionName(Mockito.any(PhenotypingStage.class), eq(TEST_NAME)))
            .thenReturn(processEventMockData());
        ProcessEvent processEvent =
            testInstance.getProcessEventByName(phenotypingStageMockData(), TEST_NAME);

        assertEquals(processEvent.getName(), TEST_NAME);
    }

    @Test
    void getPhenotypingStageByPinAndPsnPlanNull() {

        PhenotypingStage phenotypingStageMockData = phenotypingStageMockData();
        phenotypingStageMockData.setPhenotypingAttempt(phenotypingAttemptMockData());

        lenient().when(planService.getNotNullPlanByPin(PIN_000000001))
            .thenReturn(planMockData());

        lenient().when(phenotypingStageRepository.findByPsn(PSN_000000001))
            .thenReturn(phenotypingStageMockData);

        Exception exception = assertThrows(UserOperationFailedException.class, () -> {
            PhenotypingStage phenotypingStage =
                testInstance.getPhenotypingStageByPinAndPsn(PIN_000000001, PSN_000000001);
        });
        String expectedMessage =
            "Plan " + PIN_000000001 + " is not related with phenotyping stage +" + PSN_000000001 +
                ".";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    void getPhenotypingStageByPinAndPsn() {

        Plan plan = new Plan();
        PhenotypingStage phenotypingStageMockData = phenotypingStageMockData();
        PhenotypingAttempt phenotypingAttemptMockData = phenotypingAttemptMockData();
        phenotypingAttemptMockData.setPlan(plan);
        phenotypingStageMockData.setPhenotypingAttempt(phenotypingAttemptMockData);

        lenient().when(planService.getNotNullPlanByPin(PIN_000000001))
            .thenReturn(plan);

        lenient().when(phenotypingStageRepository.findByPsn(PSN_000000001))
            .thenReturn(phenotypingStageMockData);


        PhenotypingStage phenotypingStage =
            testInstance.getPhenotypingStageByPinAndPsn(PIN_000000001, PSN_000000001);

        assertEquals(phenotypingStage.getPsn(), PSN_000000001);
    }

    @Test
    void getByPsnFailsIfNotFoundPhenotypingStageNull() {


        Exception exception = assertThrows(NotFoundException.class, () -> {
            PhenotypingStage phenotypingStage =
                testInstance.getByPsnFailsIfNotFound(PSN_000000001);
        });
        String expectedMessage = "PhenotypingsStage " + PSN_000000001 + " does not exist.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getByPsnFailsIfNotFound() {

        lenient().when(phenotypingStageRepository.findByPsn(PSN_000000001))
            .thenReturn(phenotypingStageMockData());

        PhenotypingStage phenotypingStage =
            testInstance.getByPsnFailsIfNotFound(PSN_000000001);

        assertEquals(phenotypingStage.getPsn(), PSN_000000001);
    }

    @Test
    void getPhenotypingStages() {
//
//        lenient().when(phenotypingStageRepository.findAll())
//            .thenReturn(List.of(phenotypingStageMockData()));
//
//        List<PhenotypingStage> phenotypingStages =
//            testInstance.getPhenotypingStages();
//
//        assertEquals(phenotypingStages.get(0).getPsn(), PSN_000000001);
    }

    @Test
    void create() {

        lenient().when(phenotypingStageCreator.create(Mockito.any(PhenotypingStage.class)))
            .thenReturn(phenotypingStageMockData());
        PhenotypingStage phenotypingStage = testInstance.create(phenotypingStageMockData());
        assertEquals(phenotypingStage.getPsn(), PSN_000000001);
    }

    @Test
    void update() {
        lenient().when(phenotypingStageRepository.findByPsn(PSN_000000001))
            .thenReturn(phenotypingStageMockData());
        History exception = assertDoesNotThrow(() ->
            testInstance.update(phenotypingStageMockData())
        );

        assertNull(exception);
    }

    @Test
    void getPhenotypingStageHistory() {
        lenient()
            .when(historyService.getHistoryByEntityNameAndEntityId(PhenotypingStage.class.getSimpleName(), 1L))
            .thenReturn(List.of(historyMockData()));
        List<History> history = testInstance.getPhenotypingStageHistory(phenotypingStageMockData() );
        assertEquals(history.get(0).getComment(), TEST_COMMENT);
    }

    @Test
    void getPhenotypingStageTypeByNameFailingWhenNull() {

        Exception exception = assertThrows(NotFoundException.class, () -> {
            PhenotypingStageType phenotypingStageType =
                testInstance.getPhenotypingStageTypeByNameFailingWhenNull(TEST_NAME);
        });
        String expectedMessage =  "Phenotyping stage type " + TEST_NAME + " does not exist.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));    }

    @Test
    void getPhenotypingStageTypeByNameFailingWhenNullNotNull() {
        lenient()
            .when(phenotypingStageTypeRepository.findByNameIgnoreCase(TEST_NAME))
            .thenReturn(phenotypingStageTypeMockData());


            PhenotypingStageType phenotypingStageType =
                testInstance.getPhenotypingStageTypeByNameFailingWhenNull(TEST_NAME);


        assertEquals(phenotypingStageType.getName(),TEST_NAME);
    }


    @Test
    void getPhenotypingStageTypeByName() {

        lenient()
            .when(phenotypingStageTypeRepository.findByNameIgnoreCase(TEST_NAME))
            .thenReturn(phenotypingStageTypeMockData());


        PhenotypingStageType phenotypingStageType =
            testInstance.getPhenotypingStageTypeByName(TEST_NAME);


        assertEquals(phenotypingStageType.getName(),TEST_NAME);
    }
}