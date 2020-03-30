package org.gentar.biology.colony.engine.processors;

import org.gentar.biology.colony.Colony;
import org.gentar.biology.status.Status;
import org.gentar.biology.status.StatusService;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.Processor;
import org.springframework.stereotype.Component;

/**
 * Class with the logic to move a Colony to the state "Colony Aborted"
 */
@Component
public class ColonyAbortProcessor implements Processor {

    private StatusService statusService;

    public ColonyAbortProcessor(StatusService statusService)
    {
        this.statusService = statusService;
    }

    @Override
    public ProcessData process(ProcessData data)
    {
        abortColony((Colony)data);
        return data;
    }

    private void abortColony(Colony colony)
    {
        if (canAbortColony(colony))
        {
            ProcessEvent processEvent = colony.getEvent();
            String statusName = processEvent.getEndState().getInternalName();
            Status newStatus = statusService.getStatusByName(statusName);
            colony.setStatus(newStatus);
        }
    }

    private boolean canAbortColony(Colony colony)
    {
        // Put here the validations before aborting a Colony.
        return true;
    }
}
