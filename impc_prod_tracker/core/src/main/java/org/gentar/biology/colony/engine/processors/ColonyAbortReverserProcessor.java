package org.gentar.biology.colony.engine.processors;

import org.gentar.biology.colony.Colony;
import org.gentar.biology.status.Status;
import org.gentar.biology.status.StatusService;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.Processor;
import org.springframework.stereotype.Component;

/**
 * Class with the logic to move a Colony back to "Genotype Not Confirmed" after being aborted.
 */
@Component
public class ColonyAbortReverserProcessor  implements Processor {
    private StatusService statusService;

    public ColonyAbortReverserProcessor(StatusService statusService)
    {
        this.statusService = statusService;
    }

    @Override
    public ProcessData process(ProcessData data)
    {
        reverseAbortion((Colony)data);
        return data;
    }

    private boolean canRevertAbortion(Colony colony)
    {
        // Put here the needed validation before reverting an abortion.
        return true;
    }

    private void reverseAbortion(Colony colony)
    {
        if (canRevertAbortion(colony))
        {
            ProcessEvent processEvent = colony.getEvent();
            String statusName = processEvent.getEndState().getInternalName();
            Status newStatus = statusService.getStatusByName(statusName);
            colony.setStatus(newStatus);
        }
    }
}

