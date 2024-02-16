package org.gentar.biology.plan;

import static org.gentar.mockdata.MockData.PIN_000000001;
import static org.gentar.mockdata.MockData.TEST_COMMENT;
import static org.gentar.mockdata.MockData.TEST_NAME;
import static org.gentar.mockdata.MockData.TPN_000000001;
import static org.gentar.mockdata.MockData.attemptTypeMockData;
import static org.gentar.mockdata.MockData.historyMockData;
import static org.gentar.mockdata.MockData.planMockData;
import static org.gentar.mockdata.MockData.processEventMockData;
import static org.gentar.mockdata.MockData.transitionEvaluationMockData;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;

import java.util.Collections;
import java.util.List;
import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.colony.ColonyValidator;
import org.gentar.biology.plan.attempt.AttemptType;
import org.gentar.biology.plan.attempt.AttemptTypeService;
import org.gentar.biology.plan.attempt.AttemptTypesName;
import org.gentar.biology.plan.attempt.crispr.CrisprAttemptValidator;
import org.gentar.biology.plan.attempt.crispr_allele_modification.CrisprAlleleModificationValidator;
import org.gentar.biology.plan.attempt.es_cell_allele_modification.EsCellAlleleModificationValidator;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypeAttemptValidator;
import org.gentar.biology.plan.engine.PlanCreator;
import org.gentar.biology.plan.engine.PlanStateMachineResolver;
import org.gentar.biology.plan.engine.PlanUpdater;
import org.gentar.biology.plan.engine.PlanValidator;
import org.gentar.biology.plan.filter.PlanFilter;
import org.gentar.biology.plan.filter.PlanFilterBuilder;
import org.gentar.biology.project.engine.ProjectValidator;
import org.gentar.biology.status.Status;
import org.gentar.exceptions.NotFoundException;
import org.gentar.security.abac.ResourceAccessChecker;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.gentar.security.permissions.Actions;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionAvailabilityEvaluator;
import org.gentar.statemachine.TransitionEvaluation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class PlanServiceImplTest {

    @Mock
    private PlanRepository planRepository;
    @Mock
    private PlanUpdater planUpdater;
    @Mock
    private HistoryService<Plan> historyService;
    @Mock
    private PlanCreator planCreator;
    @Mock
    private PlanStateMachineResolver planStateMachineResolver;
    @Mock
    private TransitionAvailabilityEvaluator transitionAvailabilityEvaluator;
    @InjectMocks
    private PlanValidator planValidator;
    @Mock
    private CrisprAttemptValidator crisprAttemptValidator;
    @Mock
    private AttemptTypeService attemptTypeService;
    @Mock
    private ContextAwarePolicyEnforcement policyEnforcement;
    @Mock
    private ResourceAccessChecker<Plan> resourceAccessChecker;
    @Mock
    private ProjectValidator projectValidator;
    @Mock
    private PhenotypeAttemptValidator phenotypeAttemptValidator;
    @Mock
    private ColonyValidator colonyValidator;
    @Mock
    private EsCellAlleleModificationValidator esCellAlleleModificationValidator;
    @Mock
    private CrisprAlleleModificationValidator crisprAlleleModificationValidator;

    private static final String PLAN_NOT_EXISTS_ERROR =
        "The plan[%s] does not exist.";

    private PlanServiceImpl testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new PlanServiceImpl(planRepository,
            planUpdater,
            historyService,
            planCreator,
            planStateMachineResolver,
            transitionAvailabilityEvaluator,
            planValidator);

        planValidator =
            new PlanValidator(crisprAttemptValidator,
                attemptTypeService,
                policyEnforcement,
                resourceAccessChecker,
                projectValidator,
                phenotypeAttemptValidator,
                colonyValidator,
                esCellAlleleModificationValidator,
                crisprAlleleModificationValidator);
    }


    @Test
    void getNotNullPlanByPin() {

        lenient().when(planRepository.findPlanByPin(PIN_000000001)).thenReturn(planMockData());
        lenient().when(resourceAccessChecker
            .checkAccess(Mockito.any(Plan.class), eq(Actions.READ_PLAN_ACTION)))
            .thenReturn(planMockData());
//        lenient().when(projectQueryHelper.getIntentionGenesByProject(Mockito.any(Project.class))).thenReturn(projectIntentionGeneListMockData());
        Plan plan = testInstance.getNotNullPlanByPin(PIN_000000001);
        assertEquals(plan.getId(), 1L);
    }

    @Test
    void getNotNullPlanByPinPlanNull() {
        Exception exception = assertThrows(NotFoundException.class, () -> testInstance.getNotNullPlanByPin(PIN_000000001));
        String expectedMessage = String.format(PLAN_NOT_EXISTS_ERROR, PIN_000000001);
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getPlans() {
        PlanFilter planFilter =
            PlanFilterBuilder.getInstance().withTpns(List.of(TPN_000000001)).build();

        lenient().when(planRepository.findAll(Mockito.any(Specification.class)))
            .thenReturn(Collections.singletonList(planMockData()));

        lenient().when(resourceAccessChecker
            .checkAccess(Mockito.any(Plan.class), eq(Actions.READ_PLAN_ACTION)))
            .thenReturn(planMockData());

        List<Plan> plans = testInstance.getPlans(planFilter);
        assertEquals(plans.getFirst().getPin(), PIN_000000001);

    }

    @Test
    void getPageablePlans() {
        PlanFilter planFilter =
            PlanFilterBuilder.getInstance().withTpns(List.of(TPN_000000001)).build();

        Pageable pageable = PageRequest.of(0, 1);
        final Page<Plan> page = new PageImpl<>(Collections.singletonList(planMockData()));
        lenient().when(planRepository.findAll(Mockito.any(Specification.class), eq(pageable)))
            .thenReturn(page);

        lenient().when(resourceAccessChecker
            .checkAccess(Mockito.any(Plan.class), eq(Actions.READ_PROJECT_ACTION)))
            .thenReturn(planMockData());

        Page<Plan> plans = testInstance.getPageablePlans(pageable, planFilter);
        assertEquals(plans.getTotalElements(), 1);

    }

    @Test
    void getPlanByPinWithoutCheckPermissions() {
        lenient().when(planRepository.findPlanByPin(PIN_000000001)).thenReturn(planMockData());
        Plan plan = testInstance.getPlanByPinWithoutCheckPermissions(PIN_000000001);
        assertEquals(plan.getPin(), PIN_000000001);

    }

    @Test
    void updatePlan() {
        lenient().when(planRepository.findPlanByPin(PIN_000000001)).thenReturn(planMockData());
        History exception = assertDoesNotThrow(() ->
            testInstance.updatePlan(PIN_000000001, planMockData())
        );

        assertNull(exception);
    }

    @Test
    void getPlanHistory() {
        lenient()
            .when(historyService.getHistoryByEntityNameAndEntityId(Plan.class.getSimpleName(), 1L))
            .thenReturn(List.of(historyMockData()));
        List<History> history = testInstance.getPlanHistory(planMockData());
        assertEquals(history.getFirst().getComment(), TEST_COMMENT);

    }

    @Test
    void createPlan() {
        lenient().when(planCreator.createPlan(Mockito.any(Plan.class)))
            .thenReturn(planMockData());
        Plan plan = testInstance.createPlan(planMockData());
        assertEquals(plan.getPin(), PIN_000000001);

    }

    @Test
    void evaluateNextTransitions() {
        lenient().when(
            planStateMachineResolver.getAvailableTransitionsByEntityStatus(Mockito.any(Plan.class)))
            .thenReturn(List.of(processEventMockData()));
        lenient().when(transitionAvailabilityEvaluator
            .evaluate(Mockito.any(List.class), Mockito.any(Plan.class)))
            .thenReturn(List.of(transitionEvaluationMockData()));
        List<TransitionEvaluation> transitionEvaluations =
            testInstance.evaluateNextTransitions(planMockData());

        assertEquals(transitionEvaluations.size(), 1);

    }

    @Test
    void getProcessEventByName() {

        lenient().when(
            planStateMachineResolver.getProcessEventByActionName(Mockito.any(ProcessData.class), eq(TEST_NAME)))
            .thenReturn(processEventMockData());

        ProcessEvent processEvent =
            testInstance.getProcessEventByName(planMockData(), TEST_NAME);

        assertEquals(processEvent.getName(), "abandonWhenCreated");
    }

    @Test
    void canCreateOutcomeAbortionStatusTrue() {
        Plan plan = planMockData();
        Status status= new Status();
        status.setIsAbortionStatus(true);
        plan.setProcessDataStatus(status);
        lenient().when(planRepository.findPlanByPin(PIN_000000001)).thenReturn(planMockData());
        lenient().when(resourceAccessChecker
            .checkAccess(Mockito.any(Plan.class), eq(Actions.READ_PLAN_ACTION)))
            .thenReturn(plan);
      boolean canCreateOutcome =  testInstance.canCreateOutcome(PIN_000000001);
      assertFalse(canCreateOutcome);
    }

    @Test
    void canCreateOutcomeAttemptTypeEsCell() {
        Plan plan = planMockData();
        Status status= new Status();
        status.setIsAbortionStatus(false);
        status.setName("");
        plan.setProcessDataStatus(status);
        AttemptType attemptType = attemptTypeMockData();
        attemptType.setName(AttemptTypesName.ES_CELL.getLabel());
        plan.setAttemptType(attemptType);
        lenient().when(planRepository.findPlanByPin(PIN_000000001)).thenReturn(planMockData());
        lenient().when(resourceAccessChecker
            .checkAccess(Mockito.any(Plan.class), eq(Actions.READ_PLAN_ACTION)))
            .thenReturn(plan);
        boolean canCreateOutcome =  testInstance.canCreateOutcome(PIN_000000001);
        assertFalse(canCreateOutcome);
    }

    @Test
    void canCreateOutcomeAttemptTypeCrispr() {
        Plan plan = planMockData();
        Status status= new Status();
        status.setIsAbortionStatus(false);
        status.setName("");
        plan.setProcessDataStatus(status);
        AttemptType attemptType = attemptTypeMockData();
        attemptType.setName(AttemptTypesName.CRISPR.getLabel());
        plan.setAttemptType(attemptType);
        lenient().when(planRepository.findPlanByPin(PIN_000000001)).thenReturn(planMockData());
        lenient().when(resourceAccessChecker
            .checkAccess(Mockito.any(Plan.class), eq(Actions.READ_PLAN_ACTION)))
            .thenReturn(plan);
        boolean canCreateOutcome =  testInstance.canCreateOutcome(PIN_000000001);
        assertFalse(canCreateOutcome);
    }

    @Test
    void canCreateOutcomeAttemptTypeHaploEssentialCrispr() {
        Plan plan = planMockData();
        Status status= new Status();
        status.setIsAbortionStatus(false);
        status.setName("");
        plan.setProcessDataStatus(status);
        AttemptType attemptType = attemptTypeMockData();
        attemptType.setName(AttemptTypesName.HAPLOESSENTIAL_CRISPR.getLabel());
        plan.setAttemptType(attemptType);
        lenient().when(planRepository.findPlanByPin(PIN_000000001)).thenReturn(planMockData());
        lenient().when(resourceAccessChecker
            .checkAccess(Mockito.any(Plan.class), eq(Actions.READ_PLAN_ACTION)))
            .thenReturn(plan);
        boolean canCreateOutcome =  testInstance.canCreateOutcome(PIN_000000001);
        assertFalse(canCreateOutcome);
    }

    @Test
    void canCreateOutcomeAttemptTypeOutcomeZero() {
        Plan plan = planMockData();
        Status status= new Status();
        status.setIsAbortionStatus(false);
        status.setName("");
        plan.setProcessDataStatus(status);
        AttemptType attemptType = attemptTypeMockData();
        attemptType.setName(AttemptTypesName.ES_CELL.getLabel());
        plan.setAttemptType(attemptType);
        lenient().when(planRepository.findPlanByPin(PIN_000000001)).thenReturn(planMockData());
        lenient().when(resourceAccessChecker
            .checkAccess(Mockito.any(Plan.class), eq(Actions.READ_PLAN_ACTION)))
            .thenReturn(plan);
        boolean canCreateOutcome =  testInstance.canCreateOutcome(PIN_000000001);
        assertFalse(canCreateOutcome);
    }

    @Test
    void canCreateOutcomeAttemptType() {
        Plan plan = planMockData();
        Status status= new Status();
        status.setIsAbortionStatus(false);
        status.setName("Founder Obtained");
        plan.setProcessDataStatus(status);
        AttemptType attemptType = attemptTypeMockData();
        attemptType.setName(AttemptTypesName.CRISPR.getLabel());
        plan.setAttemptType(attemptType);
        lenient().when(planRepository.findPlanByPin(PIN_000000001)).thenReturn(planMockData());
        lenient().when(resourceAccessChecker
            .checkAccess(Mockito.any(Plan.class), eq(Actions.READ_PLAN_ACTION)))
            .thenReturn(plan);
        boolean canCreateOutcome =  testInstance.canCreateOutcome(PIN_000000001);
        assertTrue(canCreateOutcome);
    }
}