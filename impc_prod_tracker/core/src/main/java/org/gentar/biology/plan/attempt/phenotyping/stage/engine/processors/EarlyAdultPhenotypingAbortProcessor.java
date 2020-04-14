package org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors;

import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStage;
import org.gentar.biology.status.Status;
import org.gentar.biology.status.StatusService;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.Processor;

public class EarlyAdultPhenotypingAbortProcessor implements Processor {
    private StatusService statusService;

    public EarlyAdultPhenotypingAbortProcessor(StatusService statusService) {
        this.statusService = statusService;
    }

    @Override
    public ProcessData process(ProcessData data) {
        abortPhenotypingStage((PhenotypingStage) data);
        return data;
    }

    private void abortPhenotypingStage(PhenotypingStage phenotypingStage) {
        if (canAbortPhenotypingStage(phenotypingStage)) {
            ProcessEvent processEvent = phenotypingStage.getEvent();
            String statusName = processEvent.getEndState().getInternalName();
            Status newStatus = statusService.getStatusByName(statusName);
            phenotypingStage.setStatus(newStatus);
        }
    }

    private boolean canAbortPhenotypingStage(PhenotypingStage phenotypingStage) {
        // Put here the validations before aborting a Phenotyping Stage.
        return true;
    }
}