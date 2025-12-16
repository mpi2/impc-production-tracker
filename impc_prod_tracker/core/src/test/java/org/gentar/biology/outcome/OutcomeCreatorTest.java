package org.gentar.biology.outcome;

import jakarta.persistence.EntityManager;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.colony.Colony;
import org.gentar.biology.colony.engine.ColonyStateSetter;
import org.gentar.biology.mutation.MutationService;
import org.gentar.biology.outcome.type.OutcomeType;
import org.gentar.biology.outcome.type.OutcomeTypeName;
import org.gentar.biology.plan.engine.PlanUpdater;
import org.gentar.biology.specimen.engine.SpecimenStateSetter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.gentar.mockdata.MockData.outcomeMockData;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OutcomeCreatorTest {
    @Mock
    private EntityManager entityManager;
    @Mock
    private HistoryService<Outcome> historyService;
    @Mock
    private ColonyStateSetter colonyStateSetter;
    @Mock
    private SpecimenStateSetter specimenStateSetter;
    @Mock
    private MutationService mutationService;
    @Mock
    private OutcomeValidator outcomeValidator;
    @Mock
    private PlanUpdater planUpdater;

    OutcomeCreator testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new OutcomeCreator(entityManager, historyService,
            colonyStateSetter,
            specimenStateSetter,
            mutationService,
            outcomeValidator,
            planUpdater);
    }

    @Test
    void create() {
        Outcome outcome =
            testInstance.create(outcomeMockData());


        assertEquals(outcome.getId(), 1L);
    }

    @Test
    void createOutcomeWithColonyWithoutStatusShouldSetStatus() {
        // This test replicates the scenario where a colony is created without a status
        // and verifies that setInitialStatus is called to set the status
        
        // Create outcome with colony that has null status (replicating the bug scenario)
        Outcome outcome = new Outcome();
        OutcomeType outcomeType = new OutcomeType();
        outcomeType.setName(OutcomeTypeName.COLONY.getLabel());
        outcome.setOutcomeType(outcomeType);
        
        Colony colony = new Colony();
        colony.setName("Test Colony");
        colony.setStatus(null); // Status is null - this is the bug scenario
        outcome.setColony(colony);
        
        // Mock entity manager to simulate persistence
        doAnswer(invocation -> {
            Outcome o = invocation.getArgument(0);
            o.setId(1L);
            return null;
        }).when(entityManager).persist(any(Outcome.class));
        
        // Execute
        Outcome createdOutcome = testInstance.create(outcome);
        
        // Verify that setInitialStatus was called on the colony
        verify(colonyStateSetter, times(1)).setInitialStatus(colony);
        
        // Verify the outcome was persisted
        assertNotNull(createdOutcome.getId(), "Outcome should have an ID after creation");
    }

    @Test
    void createOutcomeWithColonyWhenOutcomeTypeIsNullShouldStillSetStatus() {
        // This test verifies the defensive check: even if outcome type is null,
        // the status should still be set if a colony exists
        
        // Create outcome with colony but null outcome type
        Outcome outcome = new Outcome();
        outcome.setOutcomeType(null); // Outcome type is null
        
        Colony colony = new Colony();
        colony.setName("Test Colony");
        colony.setStatus(null); // Status is null
        outcome.setColony(colony);
        
        // Mock entity manager
        doAnswer(invocation -> {
            Outcome o = invocation.getArgument(0);
            o.setId(1L);
            return null;
        }).when(entityManager).persist(any(Outcome.class));
        
        // Execute
        Outcome createdOutcome = testInstance.create(outcome);
        
        // Verify that setInitialStatus was called on the colony (defensive check)
        verify(colonyStateSetter, times(1)).setInitialStatus(colony);
        
        // Verify the outcome was persisted
        assertNotNull(createdOutcome.getId(), "Outcome should have an ID after creation");
    }

    @Test
    void createOutcomeWithColonyWhenStatusServiceReturnsNullShouldThrowException() {
        // This test verifies that when StatusService.getStatusByNameFailWhenNotFound
        // throws an exception (status not found), it propagates correctly
        
        // Create outcome with colony
        Outcome outcome = new Outcome();
        OutcomeType outcomeType = new OutcomeType();
        outcomeType.setName(OutcomeTypeName.COLONY.getLabel());
        outcome.setOutcomeType(outcomeType);
        
        Colony colony = new Colony();
        colony.setName("Test Colony");
        colony.setStatus(null);
        outcome.setColony(colony);
        
        // Mock colonyStateSetter to throw exception when status not found
        doThrow(new org.gentar.exceptions.UserOperationFailedException(
            "Status [Genotype In Progress] not found"))
            .when(colonyStateSetter).setInitialStatus(any(Colony.class));
        
        // Execute and verify exception is thrown
        assertThrows(org.gentar.exceptions.UserOperationFailedException.class, () -> {
            testInstance.create(outcome);
        }, "Should throw exception when status not found");
        
        // Verify setInitialStatus was called
        verify(colonyStateSetter, times(1)).setInitialStatus(colony);
    }
}