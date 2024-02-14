package org.gentar.biology.outcome;

import static org.gentar.biology.outcome.OutcomeServiceImpl.READ_OUTCOME_ACTION;
import static org.gentar.mockdata.MockData.MIN_000000001;
import static org.gentar.mockdata.MockData.PIN_000000001;
import static org.gentar.mockdata.MockData.TEST_OUTCOME_TYPE_NAME;
import static org.gentar.mockdata.MockData.TPO_000000001;
import static org.gentar.mockdata.MockData.mutationMockData;
import static org.gentar.mockdata.MockData.mutationSetMockData;
import static org.gentar.mockdata.MockData.outcomeMockData;
import static org.gentar.mockdata.MockData.outcomeTypeMockData;
import static org.gentar.mockdata.MockData.planMockData;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;

import java.util.List;
import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.mutation.Mutation;
import org.gentar.biology.mutation.MutationService;
import org.gentar.biology.mutation.MutationValidator;
import org.gentar.biology.outcome.type.OutcomeType;
import org.gentar.biology.outcome.type.OutcomeTypeRepository;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanService;
import org.gentar.exceptions.NotFoundException;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.security.abac.ResourceAccessChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OutcomeServiceImplTest {

    @Mock
    private OutcomeRepository outcomeRepository;
    @Mock
    private OutcomeTypeRepository outcomeTypeRepository;
    @Mock
    private OutcomeCreator outcomeCreator;
    @Mock
    private OutcomeUpdater outcomeUpdater;
    @Mock
    private OutcomeValidator outcomeValidator;
    @Mock
    private PlanService planService;
    @Mock
    private MutationService mutationService;
    @Mock
    private MutationValidator mutationValidator;
    @Mock
    private ResourceAccessChecker<Outcome> resourceAccessChecker;
    @Mock
    private HistoryService<Outcome> historyService;

    OutcomeServiceImpl testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new OutcomeServiceImpl(outcomeRepository,
            outcomeTypeRepository,
            outcomeCreator,
            outcomeUpdater,
            outcomeValidator,
            planService,
            mutationService,
            mutationValidator,
            resourceAccessChecker,
            historyService);
    }

    @Test
    void getOutcomeByPinAndTpo() {
        Plan plan = planMockData();
        Outcome outcome = outcomeMockData();
        outcome.setPlan(plan);
        lenient().when(planService.getNotNullPlanByPin(PIN_000000001)).thenReturn(plan);
        lenient().when(outcomeRepository.findByTpo(TPO_000000001)).thenReturn(outcome);

        Outcome outcomeResponse = testInstance.getOutcomeByPinAndTpo(PIN_000000001, TPO_000000001);
        assertEquals(outcomeResponse.getId(), 1L);
    }

    @Test
    void getOutcomeByPinAndTpoPlanNull() {
        lenient().when(planService.getNotNullPlanByPin(PIN_000000001)).thenReturn(planMockData());
        lenient().when(outcomeRepository.findByTpo(TPO_000000001)).thenReturn(outcomeMockData());


        Exception exception = assertThrows(UserOperationFailedException.class, () -> {
            Outcome outcomeResponse =
                testInstance.getOutcomeByPinAndTpo(PIN_000000001, TPO_000000001);
        });

        String expectedMessage =
            "Plan " + PIN_000000001 + " is not related with outcome +" + TPO_000000001 + ".";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getOutcomes() {
        lenient().when(outcomeRepository.findAll()).thenReturn(List.of(outcomeMockData()));
        lenient().when(
            resourceAccessChecker.checkAccess(Mockito.any(Outcome.class), eq(READ_OUTCOME_ACTION)))
            .thenReturn(outcomeMockData());
        List<Outcome> outcomeList = testInstance.getOutcomes();
        assertEquals(outcomeList.getFirst().getId(), 1L);
    }

    @Test
    void create() {
        lenient().when(outcomeCreator.create(Mockito.any(Outcome.class)))
            .thenReturn(outcomeMockData());
        Outcome outcome = testInstance.create(outcomeMockData());
        assertEquals(outcome.getId(), 1L);
    }

    @Test
    void update() {
        lenient().when(outcomeCreator.create(Mockito.any(Outcome.class)))
            .thenReturn(outcomeMockData());
        lenient().when(outcomeRepository.findByTpo(TPO_000000001)).thenReturn(outcomeMockData());
        History exception = assertDoesNotThrow(() ->
            testInstance.update(outcomeMockData())
        );

        assertNull(exception);
    }

    @Test
    void getOutcomeTypeByName() {
        lenient().when(outcomeTypeRepository.findByNameIgnoreCase(TEST_OUTCOME_TYPE_NAME))
            .thenReturn(outcomeTypeMockData());
        OutcomeType outcomeType = testInstance.getOutcomeTypeByName(TEST_OUTCOME_TYPE_NAME);
        assertEquals(outcomeType.getId(), 1L);
    }

    @Test
    void getOutcomeTypeByNameFailingWhenNull() {
        lenient().when(outcomeTypeRepository.findByNameIgnoreCase(TEST_OUTCOME_TYPE_NAME))
            .thenReturn(outcomeTypeMockData());
        OutcomeType outcomeType =
            testInstance.getOutcomeTypeByNameFailingWhenNull(TEST_OUTCOME_TYPE_NAME);
        assertEquals(outcomeType.getId(), 1L);
    }

    @Test
    void getByTpoFailsIfNotFound() {

        Exception exception = assertThrows(NotFoundException.class, () -> {
            Outcome outcome = testInstance.getByTpoFailsIfNotFound(TPO_000000001);
        });

        String expectedMessage =
            "Outcome " + TPO_000000001 + " does not exist.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getMutationByPinTpoAndMin() {
        Plan plan = planMockData();
        Outcome outcome = outcomeMockData();
        outcome.setPlan(plan);
        outcome.setMutations(mutationSetMockData());
        lenient().when(planService.getNotNullPlanByPin(PIN_000000001)).thenReturn(plan);
        lenient().when(outcomeRepository.findByTpo(TPO_000000001)).thenReturn(outcome);

        Mutation mutation =
            testInstance.getMutationByPinTpoAndMin(PIN_000000001, TPO_000000001, MIN_000000001);

        assertEquals(mutation.getId(), 1L);
    }

    @Test
    void getMutationByPinTpoAndMinAllMutationsNull() {
        Plan plan = planMockData();
        Outcome outcome = outcomeMockData();
        outcome.setPlan(plan);
        lenient().when(planService.getNotNullPlanByPin(PIN_000000001)).thenReturn(plan);
        lenient().when(outcomeRepository.findByTpo(TPO_000000001)).thenReturn(outcome);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            Mutation mutation =
                testInstance.getMutationByPinTpoAndMin(PIN_000000001, TPO_000000001, MIN_000000001);
        });

        String expectedMessage =
            "Mutation " + MIN_000000001 + " does not exist.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    void getMutationByPinTpoAndMinPlanNull() {
        lenient().when(planService.getNotNullPlanByPin(PIN_000000001)).thenReturn(planMockData());
        lenient().when(outcomeRepository.findByTpo(TPO_000000001)).thenReturn(outcomeMockData());

        Exception exception = assertThrows(UserOperationFailedException.class, () -> {
            Mutation mutation =
                testInstance.getMutationByPinTpoAndMin(PIN_000000001, TPO_000000001, MIN_000000001);
        });

        String expectedMessage =
            "Plan " + PIN_000000001 + " is not related with outcome " + TPO_000000001 + ".";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void createMutationsAssociations() {
        lenient().when(outcomeRepository.findByTpo(TPO_000000001)).thenReturn(outcomeMockData());
        lenient().when(mutationService.getMutationByMinFailsIfNull(MIN_000000001))
            .thenReturn(mutationMockData());
        History exception = assertDoesNotThrow(() ->
            testInstance
                .createMutationsAssociations(PIN_000000001, TPO_000000001, List.of(MIN_000000001))
        );

        assertNull(exception);
    }


    @Test
    void deleteMutationsAssociations() {
        Outcome outcome = outcomeMockData();
        outcome.setMutations(mutationSetMockData());
        lenient().when(outcomeRepository.findByTpo(TPO_000000001)).thenReturn(outcome);
        lenient().when(mutationService.getMutationByMinFailsIfNull(MIN_000000001))
            .thenReturn(mutationMockData());
        History exception = assertDoesNotThrow(() ->
            testInstance
                .deleteMutationsAssociations(PIN_000000001, TPO_000000001, List.of(MIN_000000001))
        );

        assertNull(exception);
    }

    @Test
    void getOutcomeHistory() {
        Outcome outcome = outcomeMockData();
        outcome.setMutations(mutationSetMockData());
        lenient().when(outcomeRepository.findByTpo(TPO_000000001)).thenReturn(outcome);
        lenient().when(mutationService.getMutationByMinFailsIfNull(MIN_000000001))
            .thenReturn(mutationMockData());
        List<History> exceptions = assertDoesNotThrow(() ->
            testInstance.getOutcomeHistory(outcome)
        );

        assertEquals(exceptions.size(), 0);
    }

    @Test
    void associateOutcomeToPlan() {
        Outcome outcome = outcomeMockData();
        outcome.setMutations(mutationSetMockData());

        lenient().when(planService.getNotNullPlanByPin(PIN_000000001)).thenReturn(planMockData());
        assertDoesNotThrow(() ->
            testInstance.associateOutcomeToPlan(outcome, PIN_000000001)
        );
    }

}