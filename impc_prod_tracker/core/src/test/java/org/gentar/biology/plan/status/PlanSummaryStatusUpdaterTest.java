package org.gentar.biology.plan.status;

import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.engine.crispr.CrisprProductionPlanState;
import org.gentar.biology.status.Status;
import org.gentar.test_util.OutcomeBuilder;
import org.gentar.test_util.PlanBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class PlanSummaryStatusUpdaterTest
{
    private PlanSummaryStatusUpdater testInstance;

    @BeforeEach
    void setUp()
    {
        testInstance = new PlanSummaryStatusUpdater();
    }

    @Test
    void setSummaryStatusWhenNoChildren()
    {
        Plan plan = PlanBuilder.getInstance()
            .withStatus(CrisprProductionPlanState.PlanCreated.getInternalName())
            .build();

        testInstance.setSummaryStatus(plan);

        assertThat(
            "Invalid state",
            plan.getSummaryStatus().getName(),
            is(CrisprProductionPlanState.PlanCreated.getInternalName()));
    }

    @Test
    void setSummaryStatusWhenOneAbortedChildren()
    {
        Plan plan = PlanBuilder.getInstance()
            .withStatusAndOrder(CrisprProductionPlanState.PlanCreated.getInternalName(), 1)
            .build();

        Outcome outcome1 = OutcomeBuilder.getInstance()
            .withId(1L)
            .withColony()
            .withIsAbortionStatusColony(true)
            .withStatusOrder(1)
            .build();
        Set<Outcome> outcomes = new HashSet<>();
        outcomes.add(outcome1);
        plan.setOutcomes(outcomes);

        testInstance.setSummaryStatus(plan);

        assertThat(
            "Invalid state",
            plan.getSummaryStatus().getName(),
            is(CrisprProductionPlanState.PlanCreated.getInternalName()));
    }

    @Test
    void setSummaryStatusWhenOneActiveChildren()
    {
        Plan plan = new Plan();
        Status planStatus = new Status(1L, "Plan Status", "", 100, false);
        plan.setPlanStatus(planStatus);

        Outcome outcome1 = OutcomeBuilder.getInstance()
            .withId(1L)
            .withColony()
            .build();
        Status outcome1Status = new Status(2L, "Outcome 1 Status", "", 200, false);
        outcome1.getColony().setStatus(outcome1Status);
        Set<Outcome> outcomes = new HashSet<>();
        outcomes.add(outcome1);
        plan.setOutcomes(outcomes);

        testInstance.setSummaryStatus(plan);

        assertThat("Invalid state", plan.getSummaryStatus().getName(), is("Outcome 1 Status"));
    }

    @Test
    void setSummaryStatusWhenSeveralActiveChildren()
    {
        Plan plan = new Plan();
        Status planStatus = new Status(1L, "Plan Status", "", 100, false);
        plan.setPlanStatus(planStatus);

        Outcome outcome1 = OutcomeBuilder.getInstance()
            .withId(1L)
            .withColony()
            .build();
        Status outcome1Status = new Status(2L, "Outcome 1 Status", "", 200, false);
        outcome1.getColony().setStatus(outcome1Status);

        Outcome outcome2 = OutcomeBuilder.getInstance()
            .withId(2L)
            .withColony()
            .build();
        Status outcome2Status = new Status(3L, "Outcome 2 Status", "", 201, false);
        outcome2.getColony().setStatus(outcome2Status);

        Set<Outcome> outcomes = new HashSet<>();
        outcomes.add(outcome1);
        outcomes.add(outcome2);
        plan.setOutcomes(outcomes);

        testInstance.setSummaryStatus(plan);

        assertThat("Invalid state", plan.getSummaryStatus().getName(), is("Outcome 2 Status"));
    }

    @Test
    void setSummaryStatusWhenSeveralActiveChildrenAndOneAborted()
    {
        Plan plan = new Plan();
        Status planStatus = new Status(1L, "Plan Status", "", 100, false);
        plan.setPlanStatus(planStatus);

        Outcome outcome1 = OutcomeBuilder.getInstance()
            .withId(1L)
            .withColony()
            .build();
        Status outcome1Status = new Status(2L, "Outcome 1 Status", "", 200, false);
        outcome1.getColony().setStatus(outcome1Status);

        Outcome outcome2 = OutcomeBuilder.getInstance()
            .withId(2L)
            .withColony()
            .build();
        Status outcome2Status = new Status(3L, "Outcome 2 Status", "", 201, false);
        outcome2.getColony().setStatus(outcome2Status);

        Outcome outcome3 = OutcomeBuilder.getInstance()
            .withId(3L)
            .withColony()
            .build();
        Status outcome3Status = new Status(4L, "Outcome 3 Status(aborted)", "", 2000, true);
        outcome3.getColony().setStatus(outcome3Status);

        Set<Outcome> outcomes = new HashSet<>();
        outcomes.add(outcome1);
        outcomes.add(outcome2);
        outcomes.add(outcome3);
        plan.setOutcomes(outcomes);

        testInstance.setSummaryStatus(plan);

        assertThat("Invalid state", plan.getSummaryStatus().getName(), is("Outcome 2 Status"));
    }
}