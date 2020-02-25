package org.gentar.biology.plan.engine.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.status.Status;
import org.gentar.biology.status.StatusService;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.Processor;
import org.springframework.stereotype.Component;

/**
 * Class with the logic to move a Phenotyping Plan back to "Phenotyping Production Registered" after being aborted.
 */
@Component
public class PhenotypePlanAbortReverserProcessor implements Processor {
    private StatusService statusService;

    public PhenotypePlanAbortReverserProcessor(StatusService statusService)
    {
        this.statusService = statusService;
    }

    @Override
    public ProcessData process(ProcessData data)
    {
        reverseAbortion((Plan)data);
        return data;
    }

    private boolean canRevertAbortion(Plan plan)
    {
        // Put here the needed validation before reverting an abortion.
        return true;
    }

    private void reverseAbortion(Plan plan)
    {
        if (canRevertAbortion(plan))
        {
            ProcessEvent processEvent = plan.getEvent();
            String statusName = processEvent.getEndState().getInternalName();
            Status newPlanStatus = statusService.getStatusByName(statusName);
            plan.setStatus(newPlanStatus);
        }
    }
}
