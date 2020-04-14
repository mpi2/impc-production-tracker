package org.gentar.biology.plan.attempt.phenotyping.stage.engine.events;

import org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors.LateHaploessentialPhenotypingAbortProcessor;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors.LateHaploessentialPhenotypingAbortReverserProcessor;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors.PhenotypingStageProcessor;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.state.LateHaploessentialPhenotypingStageState;

import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.gentar.statemachine.Processor;

import java.util.Arrays;
import java.util.List;

public enum LateHaploessentialEvent implements ProcessEvent {
    lateHaploessentialPhenotypingStarted(
            "Marked as started when the DCC receives late haplo-essential phenotype data",
            LateHaploessentialPhenotypingStageState.LateHaploessentialPhenotypingProductionRegistered,
            LateHaploessentialPhenotypingStageState.LateHaploessentialPhenotypingStarted,
            true,
            "executed by the DCC when late haplo-essential phenotyping is started."),
    lateHaploessentialPhenotypingDataReceived(
            "The CDA has received late haplo-essential phenotype data",
            LateHaploessentialPhenotypingStageState.LateHaploessentialPhenotypingStarted,
            LateHaploessentialPhenotypingStageState.LateHaploessentialPhenotypingDataReceived,
            true,
            "executed by the CDA when late haplo-essential phenotyping data received."),
    lateHaploessentialPhenotypingAllDataSent(
            "No more late haplo-essential phenotype data will be sent to the DCC.",
            LateHaploessentialPhenotypingStageState.LateHaploessentialPhenotypingDataReceived,
            LateHaploessentialPhenotypingStageState.LateHaploessentialPhenotypingAllDataSent,
            true,
            "Used to indicate all late haplo-essential phenotype data has been sent to the DCC."),
    lateHaploessentialPhenotypingAllDataProcessed(
            "Set by the DCC when all late haplo-essential phenotype data received and processed.",
            LateHaploessentialPhenotypingStageState.LateHaploessentialPhenotypingAllDataSent,
            LateHaploessentialPhenotypingStageState.LateHaploessentialPhenotypingAllDataProcessed,
            true,
            "executed by the DCC when all late haplo-essential phenotype data received and processed."),
    lateHaploessentialPhenotypingFinished(
            "Marked as finished by the CDA when all late haplo-essential phenotype data published",
            LateHaploessentialPhenotypingStageState.LateHaploessentialPhenotypingAllDataProcessed,
            LateHaploessentialPhenotypingStageState.LateHaploessentialPhenotypingFinished,
            true,
            "executed by the CDA."),
    rollbackLateHaploessentialPhenotypingAllDataSent(
            "Rollback the state of late haplo-essential phenotyping marked as having all phenotype data sent to allow data entry.",
            LateHaploessentialPhenotypingStageState.LateHaploessentialPhenotypingAllDataSent,
            LateHaploessentialPhenotypingStageState.LateHaploessentialPhenotypingDataReceived,
            true,
            "Allows more data to be sent."),
    rollbackLateHaploessentialPhenotypingAllDataProcessed(
            "Rollback the state of late haplo-essential phenotyping marked as having all phenotype data processed to allow data entry.",
            LateHaploessentialPhenotypingStageState.LateHaploessentialPhenotypingAllDataProcessed,
            LateHaploessentialPhenotypingStageState.LateHaploessentialPhenotypingDataReceived,
            true,
            "Executed by the DCC and used when more data needs to be sent."),
    rollbackLateHaploessentialPhenotypingFinished(
            "Rollback the state of late haplo-essential phenotyping marked as finished to allow an update.",
            LateHaploessentialPhenotypingStageState.LateHaploessentialPhenotypingFinished,
            LateHaploessentialPhenotypingStageState.LateHaploessentialPhenotypingAllDataSent,
            true,
            "executed by the CDA when late haplo-essential phenotyping needs updating."),
    reverseAbortion(
            "Reverse abortion",
            LateHaploessentialPhenotypingStageState.LateHaploessentialPhenotypeProductionAborted,
            LateHaploessentialPhenotypingStageState.LateHaploessentialPhenotypingProductionRegistered,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return LateHaploessentialPhenotypingAbortReverserProcessor.class;
        }
    },
    abortWhenLateHaploessentialPhenotypingProductionRegistered(
            "Abort late haplo-essential phenotyping when a phenotype attempt has been registered",
            LateHaploessentialPhenotypingStageState.LateHaploessentialPhenotypingProductionRegistered,
            LateHaploessentialPhenotypingStageState.LateHaploessentialPhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return LateHaploessentialPhenotypingAbortProcessor.class;
        }
    },
    abortWhenLateHaploessentialPhenotypingStarted(
            "Abort late haplo-essential phenotyping when phenotyping has been started",
            LateHaploessentialPhenotypingStageState.LateHaploessentialPhenotypingStarted,
            LateHaploessentialPhenotypingStageState.LateHaploessentialPhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return LateHaploessentialPhenotypingAbortProcessor.class;
        }
    },
    abortWhenLateHaploessentialPhenotypingDataReceived(
            "Abort late haplo-essential phenotyping when data has been received by the CDA.",
            LateHaploessentialPhenotypingStageState.LateHaploessentialPhenotypingDataReceived,
            LateHaploessentialPhenotypingStageState.LateHaploessentialPhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return LateHaploessentialPhenotypingAbortProcessor.class;
        }
    },
    abortWhenLateHaploessentialPhenotypingAllDataSent(
            "Abort late haplo-essential phenotyping when all phenotype data has been sent to the DCC",
            LateHaploessentialPhenotypingStageState.LateHaploessentialPhenotypingAllDataSent,
            LateHaploessentialPhenotypingStageState.LateHaploessentialPhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return LateHaploessentialPhenotypingAbortProcessor.class;
        }
    },
    abortWhenLateHaploessentialPhenotypingAllDataProcessed(
            "Abort late haplo-essential phenotyping when all phenotype data has been processed by the DCC",
            LateHaploessentialPhenotypingStageState.LateHaploessentialPhenotypingAllDataProcessed,
            LateHaploessentialPhenotypingStageState.LateHaploessentialPhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return LateHaploessentialPhenotypingAbortProcessor.class;
        }
    },
    abortWhenLateHaploessentialPhenotypingFinished(
            "Abort late haplo-essential phenotyping when it is finished",
            LateHaploessentialPhenotypingStageState.LateHaploessentialPhenotypingFinished,
            LateHaploessentialPhenotypingStageState.LateHaploessentialPhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return LateHaploessentialPhenotypingAbortProcessor.class;
        }
    };

    LateHaploessentialEvent(
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

    public static LateHaploessentialEvent getEventByName(String name) {
        LateHaploessentialEvent[] LateHaploessentialEvents = LateHaploessentialEvent.values();
        for (LateHaploessentialEvent LateHaploessentialEvent : LateHaploessentialEvents) {
            if (LateHaploessentialEvent.name().equalsIgnoreCase(name))
                return LateHaploessentialEvent;
        }
        return null;
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
    public Class<? extends Processor> getNextStepProcessor() {
        return PhenotypingStageProcessor.class;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDescription() {
        return description;
    }

    public static List<ProcessEvent> getAllEvents() {
        return Arrays.asList(LateHaploessentialEvent.values());
    }
}
