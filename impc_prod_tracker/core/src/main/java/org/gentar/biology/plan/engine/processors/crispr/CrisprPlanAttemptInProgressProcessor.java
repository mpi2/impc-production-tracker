package org.gentar.biology.plan.engine.processors.crispr;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.springframework.stereotype.Component;

@Component
public class CrisprPlanAttemptInProgressProcessor extends AbstractProcessor
{
    public CrisprPlanAttemptInProgressProcessor(PlanStateSetter planStateSetter)
    {
        super(planStateSetter);
    }

    @Override
    protected boolean canExecuteTransition(ProcessData entity)
    {
        Plan plan = (Plan)entity;
        return plan.getCrisprAttempt() != null;
    }
}
