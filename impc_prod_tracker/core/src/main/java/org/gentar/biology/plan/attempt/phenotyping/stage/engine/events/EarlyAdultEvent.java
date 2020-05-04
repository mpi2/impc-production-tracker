package org.gentar.biology.plan.attempt.phenotyping.stage.engine.events;

import org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors.EarlyAdultPhenotypingAbortProcessor;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors.EarlyAdultPhenotypingAbortReverserProcessor;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors.PhenotypingStageProcessor;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.state.EarlyAdultPhenotypingStageState;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.gentar.statemachine.Processor;

import java.util.Arrays;
import java.util.List;

public enum EarlyAdultEvent implements ProcessEvent {
    updateToRederivationStarted(
            "Started rederivation of the colony for phenotyping",
            EarlyAdultPhenotypingStageState.PhenotypingProductionRegistered,
            EarlyAdultPhenotypingStageState.RederivationStarted,
            true,
            "executed by the user when rederivation is started."),
    updateToRederivationComplete(
            "Completed rederivation of the colony for phenotyping",
            EarlyAdultPhenotypingStageState.RederivationStarted,
            EarlyAdultPhenotypingStageState.RederivationComplete,
            true,
            "executed by the user when rederivation is complete."),
    updateToPhenotypingStartedAfterRederivation(
            "Marked as started when the DCC recieves phenotype data following a rederivation step",
            EarlyAdultPhenotypingStageState.RederivationComplete,
            EarlyAdultPhenotypingStageState.PhenotypingStarted,
            true,
            "executed by the DCC following completion of rederivation."),
    updateToPhenotypingStarted(
            "Marked as started when the DCC receives phenotype data",
            EarlyAdultPhenotypingStageState.PhenotypingProductionRegistered,
            EarlyAdultPhenotypingStageState.PhenotypingStarted,
            true,
            "executed by the DCC when phenotyping is started."),
    phenotypingAllDataSent(
            "No more phenotype data will be sent to the DCC.",
            EarlyAdultPhenotypingStageState.PhenotypingStarted,
            EarlyAdultPhenotypingStageState.PhenotypingAllDataSent,
            true,
            "Used to indicate all phenotype data has been sent to the DCC."),
    PhenotypingAllDataProcessed(
            "Set by the DCC when all phenotype data received and processed.",
            EarlyAdultPhenotypingStageState.PhenotypingAllDataSent,
            EarlyAdultPhenotypingStageState.PhenotypingAllDataProcessed,
            true,
            "executed by the DCC when all phenotype data received and processed."),
    phenotypingFinished(
            "Marked as finished by the CDA when all phenotype data published",
            EarlyAdultPhenotypingStageState.PhenotypingAllDataProcessed,
            EarlyAdultPhenotypingStageState.PhenotypingFinished,
            true,
            "executed by the CDA."),
    rollbackPhenotypingAllDataSent(
            "Rollback the state of phenotyping marked as having all phenotype data sent to allow data entry.",
            EarlyAdultPhenotypingStageState.PhenotypingAllDataSent,
            EarlyAdultPhenotypingStageState.PhenotypingStarted,
            true,
            "Allows more data to be sent."),
    rollbackPhenotypingAllDataProcessed(
            "Rollback the state of phenotyping marked as having all phenotype data processed to allow data entry.",
            EarlyAdultPhenotypingStageState.PhenotypingAllDataProcessed,
            EarlyAdultPhenotypingStageState.PhenotypingStarted,
            true,
            "Executed by the DCC and used when more data needs to be sent."),
    rollbackPhenotypingFinished(
            "Rollback the state of phenotyping marked as finished to allow an update.",
            EarlyAdultPhenotypingStageState.PhenotypingFinished,
            EarlyAdultPhenotypingStageState.PhenotypingAllDataSent,
            true,
            "executed by the CDA when phenotyping needs updating."),
    reverseAbortion(
            "Reverse abortion",
            EarlyAdultPhenotypingStageState.PhenotypeProductionAborted,
            EarlyAdultPhenotypingStageState.PhenotypingProductionRegistered,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return EarlyAdultPhenotypingAbortReverserProcessor.class;
        }
    },
    abortWhenPhenotypeProductionRegistered(
            "Abort phenotyping when a phenotype attempt has been registered",
            EarlyAdultPhenotypingStageState.PhenotypingProductionRegistered,
            EarlyAdultPhenotypingStageState.PhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return EarlyAdultPhenotypingAbortProcessor.class;
        }
    },
    abortWhenRederivationStarted(
            "Abort phenotyping when rederivation has been started",
            EarlyAdultPhenotypingStageState.RederivationStarted,
            EarlyAdultPhenotypingStageState.PhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return EarlyAdultPhenotypingAbortProcessor.class;
        }
    },
    abortWhenRederivationComplete(
            "Abort phenotyping when rederivation is complete",
            EarlyAdultPhenotypingStageState.RederivationComplete,
            EarlyAdultPhenotypingStageState.PhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return EarlyAdultPhenotypingAbortProcessor.class;
        }
    },
    abortWhenPhenotypingStarted(
            "Abort phenotyping when phenotyping has been started",
            EarlyAdultPhenotypingStageState.PhenotypingStarted,
            EarlyAdultPhenotypingStageState.PhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return EarlyAdultPhenotypingAbortProcessor.class;
        }
    },
    abortWhenPhenotypingAllDataSent(
            "Abort phenotyping when all phenotype data has been sent to the DCC",
            EarlyAdultPhenotypingStageState.PhenotypingAllDataSent,
            EarlyAdultPhenotypingStageState.PhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return EarlyAdultPhenotypingAbortProcessor.class;
        }
    },
    abortWhenPhenotypingAllDataProcessed(
            "Abort phenotyping when all phenotype data has been processed by the DCC",
            EarlyAdultPhenotypingStageState.PhenotypingAllDataProcessed,
            EarlyAdultPhenotypingStageState.PhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return EarlyAdultPhenotypingAbortProcessor.class;
        }
    },
    abortWhenPhenotypingFinished(
            "Abort phenotyping when it is finished",
            EarlyAdultPhenotypingStageState.PhenotypingFinished,
            EarlyAdultPhenotypingStageState.PhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return EarlyAdultPhenotypingAbortProcessor.class;
        }
    };

    EarlyAdultEvent(
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

    public static EarlyAdultEvent getEventByName(String name) {
        EarlyAdultEvent[] earlyAdultEvents = EarlyAdultEvent.values();
        for (EarlyAdultEvent earlyAdultEvent : earlyAdultEvents) {
            if (earlyAdultEvent.name().equalsIgnoreCase(name))
                return earlyAdultEvent;
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
        return Arrays.asList(EarlyAdultEvent.values());
    }
}
