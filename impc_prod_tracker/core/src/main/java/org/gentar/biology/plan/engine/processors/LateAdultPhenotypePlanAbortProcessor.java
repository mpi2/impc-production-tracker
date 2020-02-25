package org.gentar.biology.plan.engine.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.status.Status;
import org.gentar.biology.status.StatusService;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.Processor;
import org.springframework.stereotype.Component;

/**
 * Class with the logic to move a Late Adult Phenotype Plan to the state "Late Adult Phenotype Production Aborted"
 */
@Component
public class LateAdultPhenotypePlanAbortProcessor implements Processor {
    private StatusService statusService;

    public LateAdultPhenotypePlanAbortProcessor(StatusService statusService)
    {
        this.statusService = statusService;
    }

    @Override
    public ProcessData process(ProcessData data)
    {
        abortPlan((Plan)data);
        return data;
    }

    private void abortPlan(Plan plan)
    {
        if (canAbortPlan(plan))
        {
            ProcessEvent processEvent = plan.getEvent();
            String statusName = processEvent.getEndState().getInternalName();
            Status newPlanStatus = statusService.getStatusByName(statusName);
            plan.setStatus(newPlanStatus);
        }
    }

    private boolean canAbortPlan(Plan plan)
    {
        // Put here the validations before aborting a Late Adult Phenotyping Plan.
        return true;
    }
}
