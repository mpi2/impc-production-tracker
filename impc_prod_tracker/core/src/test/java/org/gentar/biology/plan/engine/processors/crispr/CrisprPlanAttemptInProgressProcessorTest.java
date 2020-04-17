package org.gentar.biology.plan.engine.processors.crispr;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.attempt.crispr.CrisprAttempt;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.biology.plan.engine.events.CrisprProductionPlanEvent;
import org.gentar.biology.plan.engine.state.CrisprProductionPlanState;
import org.gentar.test_util.PlanBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CrisprPlanAttemptInProgressProcessorTest
{
    private CrisprPlanAttemptInProgressProcessor testInstance;
    @Mock
    private PlanStateSetter planStateSetter;

    @BeforeEach
    public void setup()
    {
        testInstance = new CrisprPlanAttemptInProgressProcessor(planStateSetter);
    }

    @Test
    public void testWhenNoAttempt()
    {
        testInstance.process(new Plan());

        verify(planStateSetter, times(0)).setStatusByName(any(Plan.class), any(String.class));
    }

    @Test
    public void testWhenAttempt()
    {
        Plan plan = PlanBuilder.getInstance()
            .withStatus(CrisprProductionPlanState.PlanCreated.getInternalName())
            .build();
        CrisprAttempt crisprAttempt = new CrisprAttempt();
        plan.setCrisprAttempt(crisprAttempt);
        crisprAttempt.setPlan(plan);
        plan.setEvent(CrisprProductionPlanEvent.changeToInProgress);

        testInstance.process(plan);

        verify(
            planStateSetter,
            times(1)).setStatusByName(any(Plan.class),
            eq(CrisprProductionPlanEvent.changeToInProgress.getEndState().getInternalName()));
    }
}