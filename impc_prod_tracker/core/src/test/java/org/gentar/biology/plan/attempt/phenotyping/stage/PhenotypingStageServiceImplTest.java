package org.gentar.biology.plan.attempt.phenotyping.stage;

import static org.gentar.mockdata.MockData.phenotypingStageMockData;
import static org.gentar.mockdata.MockData.planMockData;
import static org.gentar.mockdata.MockData.processEventMockData;
import static org.gentar.mockdata.MockData.transitionEvaluationMockData;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

import java.util.List;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanService;
import org.gentar.biology.plan.attempt.phenotyping.stage.type.PhenotypingStageTypeRepository;
import org.gentar.security.abac.ResourceAccessChecker;
import org.gentar.statemachine.TransitionAvailabilityEvaluator;
import org.gentar.statemachine.TransitionEvaluation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

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

//        lenient().when(
//            planStateMachineResolver.getAvailableTransitionsByEntityStatus(Mockito.any(Plan.class)))
//            .thenReturn(List.of(processEventMockData()));
//        lenient().when(transitionAvailabilityEvaluator
//            .evaluate(Mockito.any(List.class), Mockito.any(Plan.class)))
//            .thenReturn(List.of(transitionEvaluationMockData()));
//        List<TransitionEvaluation> transitionEvaluations =
//            testInstance.evaluateNextTransitions(phenotypingStageMockData());
//
//        assertEquals(transitionEvaluations.size(), 1);
    }

    @Test
    void getProcessEventByName() {
    }

    @Test
    void getPhenotypingStageByPinAndPsn() {
    }

    @Test
    void getByPsnFailsIfNotFound() {
    }

    @Test
    void getPhenotypingStages() {
    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void getPhenotypingStageHistory() {
    }

    @Test
    void getPhenotypingStageTypeByNameFailingWhenNull() {
    }

    @Test
    void getPhenotypingStageTypeByName() {
    }
}