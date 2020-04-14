package org.gentar.biology.plan.attempt.phenotyping.stage.engine.events;

import org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors.EarlyHaploessentialPhenotypingAbortProcessor;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors.EarlyHaploessentialPhenotypingAbortReverserProcessor;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors.PhenotypingStageProcessor;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.state.EarlyHaploessentialPhenotypingStageState;

import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.gentar.statemachine.Processor;

import java.util.Arrays;
import java.util.List;


public enum EarlyHaploessentialEvent implements ProcessEvent {
    earlyHaploessentialPhenotypingStarted(
            "Marked as started when the DCC receives early haplo-essential phenotype data",
            EarlyHaploessentialPhenotypingStageState.EarlyHaploessentialPhenotypingProductionRegistered,
            EarlyHaploessentialPhenotypingStageState.EarlyHaploessentialPhenotypingStarted,
            true,
            "executed by the DCC when early haplo-essential phenotyping is started."),
    earlyHaploessentialPhenotypingDataReceived(
            "The CDA has received early haplo-essential phenotype data",
            EarlyHaploessentialPhenotypingStageState.EarlyHaploessentialPhenotypingStarted,
            EarlyHaploessentialPhenotypingStageState.EarlyHaploessentialPhenotypingDataReceived,
            true,
            "executed by the CDA when early haplo-essential phenotyping data received."),
    earlyHaploessentialPhenotypingAllDataSent(
            "No more early haplo-essential phenotype data will be sent to the DCC.",
            EarlyHaploessentialPhenotypingStageState.EarlyHaploessentialPhenotypingDataReceived,
            EarlyHaploessentialPhenotypingStageState.EarlyHaploessentialPhenotypingAllDataSent,
            true,
            "Used to indicate all early haplo-essential phenotype data has been sent to the DCC."),
    earlyHaploessentialPhenotypingAllDataProcessed(
            "Set by the DCC when all early haplo-essential phenotype data received and processed.",
            EarlyHaploessentialPhenotypingStageState.EarlyHaploessentialPhenotypingAllDataSent,
            EarlyHaploessentialPhenotypingStageState.EarlyHaploessentialPhenotypingAllDataProcessed,
            true,
            "executed by the DCC when all early haplo-essential phenotype data received and processed."),
    earlyHaploessentialPhenotypingFinished(
            "Marked as finished by the CDA when all early haplo-essential phenotype data published",
            EarlyHaploessentialPhenotypingStageState.EarlyHaploessentialPhenotypingAllDataProcessed,
            EarlyHaploessentialPhenotypingStageState.EarlyHaploessentialPhenotypingFinished,
            true,
            "executed by the CDA."),
    rollbackEarlyHaploessentialPhenotypingAllDataSent(
            "Rollback the state of early haplo-essential phenotyping marked as having all phenotype data sent to allow data entry.",
            EarlyHaploessentialPhenotypingStageState.EarlyHaploessentialPhenotypingAllDataSent,
            EarlyHaploessentialPhenotypingStageState.EarlyHaploessentialPhenotypingDataReceived,
            true,
            "Allows more data to be sent."),
    rollbackEarlyHaploessentialPhenotypingAllDataProcessed(
            "Rollback the state of early haplo-essential phenotyping marked as having all phenotype data processed to allow data entry.",
            EarlyHaploessentialPhenotypingStageState.EarlyHaploessentialPhenotypingAllDataProcessed,
            EarlyHaploessentialPhenotypingStageState.EarlyHaploessentialPhenotypingDataReceived,
            true,
            "Executed by the DCC and used when more data needs to be sent."),
    rollbackEarlyHaploessentialPhenotypingFinished(
            "Rollback the state of early haplo-essential phenotyping marked as finished to allow an update.",
            EarlyHaploessentialPhenotypingStageState.EarlyHaploessentialPhenotypingFinished,
            EarlyHaploessentialPhenotypingStageState.EarlyHaploessentialPhenotypingAllDataSent,
            true,
            "executed by the CDA when early haplo-essential phenotyping needs updating."),
    reverseAbortion(
            "Reverse abortion",
            EarlyHaploessentialPhenotypingStageState.EarlyHaploessentialPhenotypeProductionAborted,
            EarlyHaploessentialPhenotypingStageState.EarlyHaploessentialPhenotypingProductionRegistered,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return EarlyHaploessentialPhenotypingAbortReverserProcessor.class;
        }
    },
    abortWhenEarlyHaploessentialPhenotypingProductionRegistered(
            "Abort early haplo-essential phenotyping when a phenotype attempt has been registered",
            EarlyHaploessentialPhenotypingStageState.EarlyHaploessentialPhenotypingProductionRegistered,
            EarlyHaploessentialPhenotypingStageState.EarlyHaploessentialPhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return EarlyHaploessentialPhenotypingAbortProcessor.class;
        }
    },
    abortWhenEarlyHaploessentialPhenotypingStarted(
            "Abort early haplo-essential phenotyping when phenotyping has been started",
            EarlyHaploessentialPhenotypingStageState.EarlyHaploessentialPhenotypingStarted,
            EarlyHaploessentialPhenotypingStageState.EarlyHaploessentialPhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return EarlyHaploessentialPhenotypingAbortProcessor.class;
        }
    },
    abortWhenEarlyHaploessentialPhenotypingDataReceived(
            "Abort early haplo-essential phenotyping when data has been received by the CDA.",
            EarlyHaploessentialPhenotypingStageState.EarlyHaploessentialPhenotypingDataReceived,
            EarlyHaploessentialPhenotypingStageState.EarlyHaploessentialPhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return EarlyHaploessentialPhenotypingAbortProcessor.class;
        }
    },
    abortWhenEarlyHaploessentialPhenotypingAllDataSent(
            "Abort early haplo-essential phenotyping when all phenotype data has been sent to the DCC",
            EarlyHaploessentialPhenotypingStageState.EarlyHaploessentialPhenotypingAllDataSent,
            EarlyHaploessentialPhenotypingStageState.EarlyHaploessentialPhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return EarlyHaploessentialPhenotypingAbortProcessor.class;
        }
    },
    abortWhenEarlyHaploessentialPhenotypingAllDataProcessed(
            "Abort early haplo-essential phenotyping when all phenotype data has been processed by the DCC",
            EarlyHaploessentialPhenotypingStageState.EarlyHaploessentialPhenotypingAllDataProcessed,
            EarlyHaploessentialPhenotypingStageState.EarlyHaploessentialPhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return EarlyHaploessentialPhenotypingAbortProcessor.class;
        }
    },
    abortWhenEarlyHaploessentialPhenotypingFinished(
            "Abort early haplo-essential phenotyping when it is finished",
            EarlyHaploessentialPhenotypingStageState.EarlyHaploessentialPhenotypingFinished,
            EarlyHaploessentialPhenotypingStageState.EarlyHaploessentialPhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return EarlyHaploessentialPhenotypingAbortProcessor.class;
        }
    };

    EarlyHaploessentialEvent(
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

    public static EarlyHaploessentialEvent getEventByName(String name) {
        EarlyHaploessentialEvent[] EarlyHaploessentialEvents = EarlyHaploessentialEvent.values();
        for (EarlyHaploessentialEvent EarlyHaploessentialEvent : EarlyHaploessentialEvents) {
            if (EarlyHaploessentialEvent.name().equalsIgnoreCase(name))
                return EarlyHaploessentialEvent;
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
        return Arrays.asList(EarlyHaploessentialEvent.values());
    }
}
