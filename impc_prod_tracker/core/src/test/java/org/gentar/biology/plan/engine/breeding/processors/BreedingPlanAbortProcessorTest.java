package org.gentar.biology.plan.engine.breeding.processors;

import org.gentar.biology.colony.Colony;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.outcome.type.OutcomeType;
import org.gentar.biology.outcome.type.OutcomeTypeName;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.biology.plan.engine.breeding.BreedingPlanEvent;
import org.gentar.biology.plan.engine.breeding.BreedingPlanState;
import org.gentar.biology.status.Status;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.test_util.PlanBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BreedingPlanAbortProcessorTest
{
    private BreedingPlanAbortProcessor testInstance;

    @Mock
    private PlanStateSetter planStateSetter;

    @BeforeEach
    void setUp()
    {
        testInstance = new BreedingPlanAbortProcessor(planStateSetter);
    }

    @Test
    public void testWhenNoColonies()
    {
        Plan plan = PlanBuilder.getInstance()
            .withStatus(BreedingPlanState.BreedingComplete.getInternalName())
            .build();

        plan.setEvent(BreedingPlanEvent.abortWhenBreedingComplete);
        testInstance.process(plan);

        verify(planStateSetter, times(1)).setStatusByName(any(Plan.class), any(String.class));
    }

    @Test
    public void testWhenAllColoniesAborted()
    {
        Plan plan = PlanBuilder.getInstance()
            .withStatus(BreedingPlanState.BreedingComplete.getInternalName())
            .build();
        Set<Outcome> outcomes = new HashSet<>();
        Outcome outcome1 = buildOutcomeColony(1L, true);
        Outcome outcome2 = buildOutcomeColony(2L, true);
        outcomes.add(outcome1);
        outcomes.add(outcome2);
        plan.setOutcomes(outcomes);

        plan.setEvent(BreedingPlanEvent.abortWhenBreedingComplete);
        testInstance.process(plan);

        verify(planStateSetter, times(1)).setStatusByName(any(Plan.class), any(String.class));
    }

    @Test
    public void testWhenNotAllColoniesAborted()
    {
        Plan plan = PlanBuilder.getInstance()
            .withStatus(BreedingPlanState.BreedingComplete.getInternalName())
            .build();
        Set<Outcome> outcomes = new HashSet<>();
        Outcome outcome1 = buildOutcomeColony(1L, true);
        Outcome outcome2 = buildOutcomeColony(2L, false);
        outcomes.add(outcome1);
        outcomes.add(outcome2);
        plan.setOutcomes(outcomes);

        plan.setEvent(BreedingPlanEvent.abortWhenBreedingComplete);

        UserOperationFailedException thrown = assertThrows(UserOperationFailedException.class,
            () -> testInstance.process(plan), "Exception not thrown");
        System.out.println(thrown.getMessage());
        assertThat("Not expected message", thrown.getMessage(), is("Plan cannot be aborted"));
        assertThat(
            "Not expected message",
            thrown.getDebugMessage(),
            is("The plan has colonies that are not aborted. Please abort them first"));
        verify(planStateSetter, times(0)).setStatusByName(any(Plan.class), any(String.class));
    }

    private Outcome buildOutcomeColony(long id, boolean isAborted)
    {
        Outcome outcome = new Outcome();
        outcome.setId(id);
        OutcomeType outcomeType = new OutcomeType();
        outcomeType.setName(OutcomeTypeName.COLONY.getLabel());
        outcome.setOutcomeType(outcomeType);
        Colony colony = new Colony();
        colony.setId(id);
        outcome.setColony(colony);
        Status status = new Status();
        status.setIsAbortionStatus(isAborted);
        colony.setStatus(status);
        return outcome;
    }
}