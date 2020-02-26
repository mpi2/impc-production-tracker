package org.gentar.biology.specimen.engine.processors;

import org.gentar.biology.specimen.Specimen;
import org.gentar.biology.status.Status;
import org.gentar.biology.status.StatusService;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.Processor;

public class SpecimenProcessor implements Processor
{
    private StatusService statusService;

    public SpecimenProcessor(StatusService statusService)
    {
        this.statusService = statusService;
    }

    @Override
    public ProcessData process(ProcessData data)
    {
        ProcessEvent processEvent = data.getEvent();
        String statusName = processEvent.getEndState().getInternalName();
        Status newStatus = statusService.getStatusByName(statusName);
        ((Specimen)data).setStatus(newStatus);
        return data;
    }

}
