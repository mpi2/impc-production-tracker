package org.gentar.biology.plan.engine.processors.crispr;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.biology.plan.engine.state.CrisprProductionPlanState;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.gentar.statemachine.Processor;
import org.springframework.stereotype.Component;

@Component
public class CrisprPlanAttemptInProgressProcessor implements Processor
{
    private PlanStateSetter planStateSetter;

    public CrisprPlanAttemptInProgressProcessor(PlanStateSetter planStateSetter)
    {
        this.planStateSetter = planStateSetter;
    }

    @Override
    public ProcessData process(ProcessData data)
    {
        tryToMoveToAttemptInProgress((Plan)data);
        return data;
    }

    private void tryToMoveToAttemptInProgress(Plan plan)
    {
        if (canMoveToAttemptInProgress(plan))
        {
            ProcessEvent processEvent = plan.getEvent();
            ProcessState endState = processEvent.getEndState();
            assert(endState.equals(CrisprProductionPlanState.AttemptInProgress));
            String statusName = endState.getInternalName();
            planStateSetter.setStatusByName(plan, statusName);
        }
    }

    private boolean canMoveToAttemptInProgress(Plan plan)
    {
        return plan.getCrisprAttempt() != null;
    }
}
