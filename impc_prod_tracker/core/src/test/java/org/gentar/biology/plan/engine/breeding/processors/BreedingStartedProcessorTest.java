package org.gentar.biology.plan.engine.breeding.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.attempt.breeding.BreedingAttempt;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.biology.plan.engine.breeding.BreedingPlanEvent;
import org.gentar.biology.plan.engine.breeding.BreedingPlanState;
import org.gentar.test_util.PlanBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BreedingStartedProcessorTest
{
    private BreedingStartedProcessor testInstance;

    @Mock
    private PlanStateSetter planStateSetter;

    @BeforeEach
    void setUp()
    {
        testInstance = new BreedingStartedProcessor(planStateSetter);
    }

    @Test
    public void testWhenNoBreedingAttempt()
    {
        Plan plan = new Plan();

        testInstance.process(plan);

        verify(planStateSetter, times(0)).setStatusByName(any(Plan.class), any(String.class));
    }

    @Test
    public void testWhenBreedingAttempt()
    {
        Plan plan = PlanBuilder.getInstance()
            .withStatus(BreedingPlanState.PlanCreated.getInternalName())
            .build();
        BreedingAttempt breedingAttempt = new BreedingAttempt();
        plan.setBreedingAttempt(breedingAttempt);

        plan.setProcessDataEvent(BreedingPlanEvent.updateToBreedingStarted);
        testInstance.process(plan);

        verify(planStateSetter, times(1)).setStatusByName(any(Plan.class), any(String.class));
    }
}