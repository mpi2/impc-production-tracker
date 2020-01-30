package org.gentar.biology.plan.engine.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.engine.PlanState;
import org.gentar.biology.status.Status;
import org.gentar.biology.status.StatusService;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.Processor;
import org.springframework.stereotype.Component;

@Component
public class AbortProcessor implements Processor
{
    private StatusService statusService;

    public AbortProcessor(StatusService statusService)
    {
        this.statusService = statusService;
    }

    @Override
    public ProcessData process(ProcessData data)
    {
        ProcessEvent processEvent = data.getEvent();
        PlanState planState = (PlanState) processEvent.getEndState();
        if (planState.equals(PlanState.Aborted))
        {
            abortPlan((Plan)data, processEvent);
        }
        else if (planState.equals(PlanState.MicroInjectionInProgress))
        {
            reverseAbortion((Plan)data, processEvent);
        }
        System.out.println(processEvent);
        System.out.println("Aborting!!!!!!!!!!!!");

        return data;
    }

    private void abortPlan(Plan plan, ProcessEvent processEvent)
    {
        if (canAbortPlan(plan))
        {
            String statusName = processEvent.getEndState().getInternalName();
            Status newPlanStatus = statusService.getStatusByName(statusName);
            plan.setStatus(newPlanStatus);
        }
    }

    private void reverseAbortion(Plan plan, ProcessEvent processEvent)
    {
        if (canRevertAbortion(plan))
        {
            String statusName = processEvent.getEndState().getInternalName();
            Status newPlanStatus = statusService.getStatusByName(statusName);
            plan.setStatus(newPlanStatus);
        }
    }

    private boolean canAbortPlan(Plan plan)
    {
        return true;
    }

    private boolean canRevertAbortion(Plan plan)
    {
        return true;
    }
}
