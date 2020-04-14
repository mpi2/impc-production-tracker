package org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors;

import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStage;
import org.gentar.biology.status.Status;
import org.gentar.biology.status.StatusService;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.Processor;

public class LateAdultPhenotypingAbortReverserProcessor implements Processor {
    private StatusService statusService;

    public LateAdultPhenotypingAbortReverserProcessor(StatusService statusService)
    {
        this.statusService = statusService;
    }

    @Override
    public ProcessData process(ProcessData data)
    {
        reverseAbortion((PhenotypingStage)data);
        return data;
    }

    private boolean canRevertAbortion(PhenotypingStage phenotypingStage)
    {
        // Put here the needed validation before reverting an abortion.
        return true;
    }

    private void reverseAbortion(PhenotypingStage phenotypingStage)
    {
        if (canRevertAbortion(phenotypingStage))
        {
            ProcessEvent processEvent = phenotypingStage.getEvent();
            String statusName = processEvent.getEndState().getInternalName();
            Status newStatus = statusService.getStatusByName(statusName);
            phenotypingStage.setStatus(newStatus);
        }
    }
}
