package org.gentar.biology.plan.attempt.phenotyping.stage.engine.events;

import org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors.LateAdultPhenotypingAbortProcessor;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors.LateAdultPhenotypingAbortReverserProcessor;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors.PhenotypingStageProcessor;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.state.LateAdultPhenotypingStageState;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.gentar.statemachine.Processor;

import java.util.Arrays;
import java.util.List;

public enum LateAdultEvent implements ProcessEvent {
    lateAdultPhenotypingStarted(
            "Marked as started when the DCC receives late adult phenotype data",
            LateAdultPhenotypingStageState.RegisteredForLateAdultPhenotypingProduction,
            LateAdultPhenotypingStageState.LateAdultPhenotypingStarted,
            true,
            "executed by the DCC when late adult phenotyping is started."),
    lateAdultPhenotypingDataReceived(
            "The CDA has received late adult phenotype data",
            LateAdultPhenotypingStageState.LateAdultPhenotypingStarted,
            LateAdultPhenotypingStageState.LateAdultPhenotypingDataReceived,
            true,
            "executed by the CDA when late adult phenotyping data received."),
    lateAdultPhenotypingAllDataSent(
            "No more late adult phenotype data will be sent to the DCC.",
            LateAdultPhenotypingStageState.LateAdultPhenotypingDataReceived,
            LateAdultPhenotypingStageState.LateAdultPhenotypingAllDataSent,
            true,
            "Used to indicate all late adult phenotype data has been sent to the DCC."),
    lateAdultPhenotypingAllDataProcessed(
            "Set by the DCC when all late adult phenotype data received and processed.",
            LateAdultPhenotypingStageState.LateAdultPhenotypingAllDataSent,
            LateAdultPhenotypingStageState.LateAdultPhenotypingAllDataProcessed,
            true,
            "executed by the DCC when all late adult phenotype data received and processed."),
    lateAdultPhenotypingFinished(
            "Marked as finished by the CDA when all late adult phenotype data published",
            LateAdultPhenotypingStageState.LateAdultPhenotypingAllDataProcessed,
            LateAdultPhenotypingStageState.LateAdultPhenotypingFinished,
            true,
            "executed by the CDA."),
    rollbackLateAdultPhenotypingAllDataSent(
            "Rollback the state of late adult phenotyping marked as having all phenotype data sent to allow data entry.",
            LateAdultPhenotypingStageState.LateAdultPhenotypingAllDataSent,
            LateAdultPhenotypingStageState.LateAdultPhenotypingDataReceived,
            true,
            "Allows more data to be sent."),
    rollbackLateAdultPhenotypingAllDataProcessed(
            "Rollback the state of late adult phenotyping marked as having all phenotype data processed to allow data entry.",
            LateAdultPhenotypingStageState.LateAdultPhenotypingAllDataProcessed,
            LateAdultPhenotypingStageState.LateAdultPhenotypingDataReceived,
            true,
            "Executed by the DCC and used when more data needs to be sent."),
    rollbackLateAdultPhenotypingFinished(
            "Rollback the state of late adult phenotyping marked as finished to allow an update.",
            LateAdultPhenotypingStageState.LateAdultPhenotypingFinished,
            LateAdultPhenotypingStageState.LateAdultPhenotypingAllDataSent,
            true,
            "executed by the CDA when late adult phenotyping needs updating."),
    reverseAbortion(
            "Reverse abortion",
            LateAdultPhenotypingStageState.LateAdultPhenotypeProductionAborted,
            LateAdultPhenotypingStageState.RegisteredForLateAdultPhenotypingProduction,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return LateAdultPhenotypingAbortReverserProcessor.class;
        }
    },
    abortWhenRegisteredForLateAdultPhenotypingProduction(
            "Abort late adult phenotyping when a phenotype attempt has been registered",
            LateAdultPhenotypingStageState.RegisteredForLateAdultPhenotypingProduction,
            LateAdultPhenotypingStageState.LateAdultPhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return LateAdultPhenotypingAbortProcessor.class;
        }
    },
    abortWhenLateAdultPhenotypingStarted(
            "Abort late adult phenotyping when phenotyping has been started",
            LateAdultPhenotypingStageState.LateAdultPhenotypingStarted,
            LateAdultPhenotypingStageState.LateAdultPhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return LateAdultPhenotypingAbortProcessor.class;
        }
    },
    abortWhenLateAdultPhenotypingDataReceived(
            "Abort late adult phenotyping when data has been received by the CDA.",
            LateAdultPhenotypingStageState.LateAdultPhenotypingDataReceived,
            LateAdultPhenotypingStageState.LateAdultPhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return LateAdultPhenotypingAbortProcessor.class;
        }
    },
    abortWhenLateAdultPhenotypingAllDataSent(
            "Abort late adult phenotyping when all phenotype data has been sent to the DCC",
            LateAdultPhenotypingStageState.LateAdultPhenotypingAllDataSent,
            LateAdultPhenotypingStageState.LateAdultPhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return LateAdultPhenotypingAbortProcessor.class;
        }
    },
    abortWhenLateAdultPhenotypingAllDataProcessed(
            "Abort late adult phenotyping when all phenotype data has been processed by the DCC",
            LateAdultPhenotypingStageState.LateAdultPhenotypingAllDataProcessed,
            LateAdultPhenotypingStageState.LateAdultPhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return LateAdultPhenotypingAbortProcessor.class;
        }
    },
    abortWhenLateAdultPhenotypingFinished(
            "Abort late adult phenotyping when it is finished",
            LateAdultPhenotypingStageState.LateAdultPhenotypingFinished,
            LateAdultPhenotypingStageState.LateAdultPhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return LateAdultPhenotypingAbortProcessor.class;
        }
    };

    LateAdultEvent(
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

    public static LateAdultEvent getEventByName(String name) {
        LateAdultEvent[] LateAdultEvents = LateAdultEvent.values();
        for (LateAdultEvent LateAdultEvent : LateAdultEvents) {
            if (LateAdultEvent.name().equalsIgnoreCase(name))
                return LateAdultEvent;
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
        return Arrays.asList(LateAdultEvent.values());
    }
}
