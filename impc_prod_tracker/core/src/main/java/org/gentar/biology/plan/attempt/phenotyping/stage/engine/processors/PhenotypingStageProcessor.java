package org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors;

import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStage;
import org.gentar.biology.status.Status;
import org.gentar.biology.status.StatusService;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.Processor;
import org.springframework.stereotype.Component;

@Component
public class PhenotypingStageProcessor implements Processor
{
    private StatusService statusService;

    public PhenotypingStageProcessor(StatusService statusService)
    {
        this.statusService = statusService;
    }

    @Override
    public ProcessData process(ProcessData data)
    {
        ProcessEvent processEvent = data.getEvent();
        String statusName = processEvent.getEndState().getInternalName();
        Status newStatus = statusService.getStatusByName(statusName);
        ((PhenotypingStage)data).setStatus(newStatus);
        return data;
    }
}

