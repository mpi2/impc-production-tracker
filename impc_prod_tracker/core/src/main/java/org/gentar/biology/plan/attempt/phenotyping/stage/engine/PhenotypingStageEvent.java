package org.gentar.biology.plan.attempt.phenotyping.stage.engine;

import org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors.PhenotypingStageProcessor;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.gentar.statemachine.Processor;
import java.util.Arrays;
import java.util.List;

public enum PhenotypingStageEvent implements ProcessEvent
{
    updateToPhenotypingStarted(
        "Marked as started when the DCC receives phenotype data",
        PhenotypingStageState.PhenotypingProductionRegistered,
        PhenotypingStageState.PhenotypingStarted,
        true,
        "executed by the DCC when phenotyping is started."),
    updateToPhenotypingAllDataSent(
        "No more phenotype data will be sent to the DCC.",
        PhenotypingStageState.PhenotypingStarted,
        PhenotypingStageState.PhenotypingAllDataSent,
        true,
        "Used to indicate all phenotype data has been sent to the DCC."),
    updateToPhenotypingAllDataProcessed(
        "Set by the DCC when all phenotype data received and processed.",
        PhenotypingStageState.PhenotypingAllDataSent,
        PhenotypingStageState.PhenotypingAllDataProcessed,
        true,
        "executed by the DCC when all phenotype data received and processed."),
    updateToPhenotypingFinished(
        "Marked as finished by the CDA when all phenotype data published",
        PhenotypingStageState.PhenotypingAllDataProcessed,
        PhenotypingStageState.PhenotypingFinished,
        true,
        "executed by the CDA."),
    rollbackPhenotypingAllDataSent(
        "Rollback the state of phenotyping marked as having all phenotype data sent to allow data entry.",
        PhenotypingStageState.PhenotypingAllDataSent,
        PhenotypingStageState.PhenotypingStarted,
        true,
        "Allows more data to be sent."),
    rollbackPhenotypingAllDataProcessed(
        "Rollback the state of phenotyping marked as having all phenotype data processed to allow data entry.",
        PhenotypingStageState.PhenotypingAllDataProcessed,
        PhenotypingStageState.PhenotypingStarted,
        true,
        "Executed by the DCC and used when more data needs to be sent."),
    rollbackPhenotypingFinished(
        "Rollback the state of phenotyping marked as finished to allow an update.",
        PhenotypingStageState.PhenotypingFinished,
        PhenotypingStageState.PhenotypingAllDataSent,
        true,
        "executed by the CDA when phenotyping needs updating."),
    reverseAbortion(
        "Reverse abortion",
        PhenotypingStageState.PhenotypeProductionAborted,
        PhenotypingStageState.PhenotypingProductionRegistered,
        true,
        null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return null;
        }
    },
    abortWhenPhenotypingProductionRegistered(
        "Abort phenotyping when a phenotype attempt has been registered",
        PhenotypingStageState.PhenotypingProductionRegistered,
        PhenotypingStageState.PhenotypeProductionAborted,
        true,
        null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return null;
        }
    },
    abortWhenPhenotypingStarted(
        "Abort phenotyping when phenotyping has been started",
        PhenotypingStageState.PhenotypingStarted,
        PhenotypingStageState.PhenotypeProductionAborted,
        true,
        null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor()
        {
            return null;
        }
    },
    abortWhenPhenotypingAllDataSent(
        "Abort phenotyping when all phenotype data has been sent to the DCC",
        PhenotypingStageState.PhenotypingAllDataSent,
        PhenotypingStageState.PhenotypeProductionAborted,
        true,
        null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor()
        {
            return null;
        }
    },
    abortWhenPhenotypingAllDataProcessed(
        "Abort phenotyping when all phenotype data has been processed by the DCC",
        PhenotypingStageState.PhenotypingAllDataProcessed,
        PhenotypingStageState.PhenotypeProductionAborted,
        true,
        null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return null;
        }
    },
    abortWhenPhenotypingFinished(
        "Abort phenotyping when it is finished",
        PhenotypingStageState.PhenotypingFinished,
        PhenotypingStageState.PhenotypeProductionAborted,
        true,
        null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return null;
        }
    };

    PhenotypingStageEvent(
        String description,
        ProcessState initialState,
        ProcessState endState,
        boolean triggeredByUser,
        String triggerNote) {
        this.description = description;
        this.initialState = initialState;
        this.endState = endState;
        this.triggeredByUser = triggeredByUser;
        this.triggerNote = triggerNote;
    }

    @Override
    public ProcessState getInitialState() {
        return initialState;
    }

    @Override
    public ProcessState getEndState() {
        return endState;
    }

    @Override
    public boolean isTriggeredByUser() {
        return triggeredByUser;
    }

    @Override
    public String getTriggerNote() {
        return triggerNote;
    }

    private String description;
    private ProcessState initialState;
    private ProcessState endState;
    private boolean triggeredByUser;
    private String triggerNote;

    @Override
    public Class<? extends Processor> getNextStepProcessor()
    {
        return PhenotypingStageProcessor.class;
    }

    @Override
    public String getName()
    {
        return name();
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    public static List<ProcessEvent> getAllEvents()
    {
        return Arrays.asList(PhenotypingStageEvent.values());
    }
}
