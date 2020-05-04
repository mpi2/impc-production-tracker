package org.gentar.biology.plan.engine;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.attempt.AttemptTypesName;
import org.gentar.biology.plan.engine.crispr.CrisprProductionPlanEvent;
import org.gentar.biology.plan.type.PlanTypes;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.test_util.PlanBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class PlanStateMachineResolverTest
{
    private PlanStateMachineResolver testInstance;

    @BeforeEach
    public void setup()
    {
        testInstance = new PlanStateMachineResolver();
    }

    @Test
    public void testWhenCrispr()
    {
        Plan plan = PlanBuilder.getInstance()
            .withPlanType(PlanTypes.PRODUCTION.getTypeName())
            .withAttemptType(AttemptTypesName.CRISPR.getLabel())
            .build();
        ProcessEvent processEvent =
            testInstance.getProcessEventByActionName(
                plan, CrisprProductionPlanEvent.abandonWhenCreated.getName());
        assertThat(
            "Invalid state machine",
            (processEvent instanceof CrisprProductionPlanEvent),
            is(true));
        assertThat(
            "Invalid event",
            processEvent.getName(),
            is(CrisprProductionPlanEvent.abandonWhenCreated.getName()));
        System.out.println(processEvent);
    }
}