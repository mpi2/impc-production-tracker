package org.gentar.biology.colony.engine.processors;

import org.gentar.biology.colony.Colony;
import org.gentar.biology.colony.engine.ColonyEvent;
import org.gentar.biology.colony.engine.ColonyState;
import org.gentar.biology.colony.engine.ColonyStateSetter;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.engine.phenotyping.PhenotypePlanState;
import org.gentar.biology.plan.starting_point.PlanStartingPoint;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ColonyAbortProcessorTest
{
    private ColonyAbortProcessor testInstance;

    @Mock
    private ColonyStateSetter colonyStateSetter;

    @BeforeEach
    void setUp()
    {
        testInstance = new ColonyAbortProcessor(colonyStateSetter);
    }

    @Test
    public void testWhenNoDependantPlans()
    {
        Colony colony = buildColony(ColonyState.GenotypeNotConfirmed.getInternalName());

        colony.setProcessDataEvent(ColonyEvent.abortGenotypeNotConfirmed);
        testInstance.process(colony);

        verify(colonyStateSetter, times(1)).setStatusByName(any(Colony.class), any(String.class));
    }

    @Test
    public void testWhenAllDependantPlansAreAborted()
    {
        Colony colony = buildColony(ColonyState.GenotypeNotConfirmed.getInternalName());
        Plan plan1 = PlanBuilder.getInstance()
            .withId(1L)
            .withStatus(PhenotypePlanState.PhenotypePlanAborted.getInternalName())
            .withIsStatusAbortion(true)
            .build();
        Plan plan2 = PlanBuilder.getInstance()
            .withId(2L)
            .withStatus(PhenotypePlanState.PhenotypePlanAborted.getInternalName())
            .withIsStatusAbortion(true)
            .build();
        Set<PlanStartingPoint> planStartingPoints = new HashSet<>();
        PlanStartingPoint planStartingPoint1 = new PlanStartingPoint();
        planStartingPoint1.setId(1L);
        planStartingPoint1.setOutcome(colony.getOutcome());
        planStartingPoint1.setPlan(plan1);
        PlanStartingPoint planStartingPoint2 = new PlanStartingPoint();
        planStartingPoint2.setId(2L);
        planStartingPoint2.setOutcome(colony.getOutcome());
        planStartingPoint2.setPlan(plan2);
        planStartingPoints.add(planStartingPoint1);
        planStartingPoints.add(planStartingPoint2);
        colony.getOutcome().setPlanStartingPoints(planStartingPoints);

        colony.setProcessDataEvent(ColonyEvent.abortGenotypeNotConfirmed);
        testInstance.process(colony);

        verify(colonyStateSetter, times(1)).setStatusByName(any(Colony.class), any(String.class));
    }

    @Test
    public void testWhenNotAllDependantPlansAreAborted()
    {
        Colony colony = buildColony(ColonyState.GenotypeNotConfirmed.getInternalName());
        Plan plan1 = PlanBuilder.getInstance()
            .withId(1L)
            .withStatus(PhenotypePlanState.PhenotypePlanAborted.getInternalName())
            .withIsStatusAbortion(true)
            .build();
        Plan plan2 = PlanBuilder.getInstance()
            .withId(2L)
            .withStatus(PhenotypePlanState.PlanCreated.getInternalName())
            .withIsStatusAbortion(false)
            .build();
        Set<PlanStartingPoint> planStartingPoints = new HashSet<>();
        PlanStartingPoint planStartingPoint1 = new PlanStartingPoint();
        planStartingPoint1.setId(1L);
        planStartingPoint1.setOutcome(colony.getOutcome());
        planStartingPoint1.setPlan(plan1);
        PlanStartingPoint planStartingPoint2 = new PlanStartingPoint();
        planStartingPoint2.setId(2L);
        planStartingPoint2.setOutcome(colony.getOutcome());
        planStartingPoint2.setPlan(plan2);
        planStartingPoints.add(planStartingPoint1);
        planStartingPoints.add(planStartingPoint2);
        colony.getOutcome().setPlanStartingPoints(planStartingPoints);

        colony.setProcessDataEvent(ColonyEvent.abortGenotypeNotConfirmed);
        UserOperationFailedException thrown = assertThrows(UserOperationFailedException.class,
            () -> testInstance.process(colony), "Exception not thrown");
        assertThat(
            "Not expected message", thrown.getMessage(), is("Transition cannot be executed The colony is used in plans that are not aborted"));


        verify(colonyStateSetter, times(0)).setStatusByName(any(Colony.class), any(String.class));
    }

    private Colony buildColony(String statusName)
    {
        Colony colony = new Colony();
        Status status = new Status();
        status.setName(statusName);
        colony.setProcessDataStatus(status);
        Outcome outcome = new Outcome();
        colony.setOutcome(outcome);
        return colony;
    }
}