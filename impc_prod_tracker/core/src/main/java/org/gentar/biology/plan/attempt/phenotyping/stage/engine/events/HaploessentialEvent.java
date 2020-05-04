package org.gentar.biology.plan.attempt.phenotyping.stage.engine.events;

import org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors.HaploessentialPhenotypingAbortProcessor;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors.HaploessentialPhenotypingAbortReverserProcessor;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors.PhenotypingStageProcessor;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.state.HaploessentialPhenotypingStageState;

import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.gentar.statemachine.Processor;

import java.util.Arrays;
import java.util.List;


public enum HaploessentialEvent implements ProcessEvent {
    haploessentialPhenotypingStarted(
            "Marked as started when the DCC receives haplo-essential phenotype data",
            HaploessentialPhenotypingStageState.HaploessentialPhenotypingProductionRegistered,
            HaploessentialPhenotypingStageState.HaploessentialPhenotypingStarted,
            true,
            "executed by the DCC when haplo-essential phenotyping is started."),
    haploessentialPhenotypingAllDataSent(
            "No more haplo-essential phenotype data will be sent to the DCC.",
            HaploessentialPhenotypingStageState.HaploessentialPhenotypingStarted,
            HaploessentialPhenotypingStageState.HaploessentialPhenotypingAllDataSent,
            true,
            "Used to indicate all haplo-essential phenotype data has been sent to the DCC."),
    haploessentialPhenotypingAllDataProcessed(
            "Set by the DCC when all haplo-essential phenotype data received and processed.",
            HaploessentialPhenotypingStageState.HaploessentialPhenotypingAllDataSent,
            HaploessentialPhenotypingStageState.HaploessentialPhenotypingAllDataProcessed,
            true,
            "executed by the DCC when all haplo-essential phenotype data received and processed."),
    haploessentialPhenotypingFinished(
            "Marked as finished by the CDA when all haplo-essential phenotype data published",
            HaploessentialPhenotypingStageState.HaploessentialPhenotypingAllDataProcessed,
            HaploessentialPhenotypingStageState.HaploessentialPhenotypingFinished,
            true,
            "executed by the CDA."),
    rollbackHaploessentialPhenotypingAllDataSent(
            "Rollback the state of haplo-essential phenotyping marked as having all phenotype data sent to allow data entry.",
            HaploessentialPhenotypingStageState.HaploessentialPhenotypingAllDataSent,
            HaploessentialPhenotypingStageState.HaploessentialPhenotypingStarted,
            true,
            "Allows more data to be sent."),
    rollbackHaploessentialPhenotypingAllDataProcessed(
            "Rollback the state of haplo-essential phenotyping marked as having all phenotype data processed to allow data entry.",
            HaploessentialPhenotypingStageState.HaploessentialPhenotypingAllDataProcessed,
            HaploessentialPhenotypingStageState.HaploessentialPhenotypingStarted,
            true,
            "Executed by the DCC and used when more data needs to be sent."),
    rollbackHaploessentialPhenotypingFinished(
            "Rollback the state of haplo-essential phenotyping marked as finished to allow an update.",
            HaploessentialPhenotypingStageState.HaploessentialPhenotypingFinished,
            HaploessentialPhenotypingStageState.HaploessentialPhenotypingAllDataSent,
            true,
            "executed by the CDA when haplo-essential phenotyping needs updating."),
    reverseAbortion(
            "Reverse abortion",
            HaploessentialPhenotypingStageState.HaploessentialPhenotypeProductionAborted,
            HaploessentialPhenotypingStageState.HaploessentialPhenotypingProductionRegistered,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return HaploessentialPhenotypingAbortReverserProcessor.class;
        }
    },
    abortWhenHaploessentialPhenotypingProductionRegistered(
            "Abort haplo-essential phenotyping when a phenotype attempt has been registered",
            HaploessentialPhenotypingStageState.HaploessentialPhenotypingProductionRegistered,
            HaploessentialPhenotypingStageState.HaploessentialPhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return HaploessentialPhenotypingAbortProcessor.class;
        }
    },
    abortWhenHaploessentialPhenotypingStarted(
            "Abort haplo-essential phenotyping when phenotyping has been started",
            HaploessentialPhenotypingStageState.HaploessentialPhenotypingStarted,
            HaploessentialPhenotypingStageState.HaploessentialPhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return HaploessentialPhenotypingAbortProcessor.class;
        }
    },
    abortWhenHaploessentialPhenotypingAllDataSent(
            "Abort haplo-essential phenotyping when all phenotype data has been sent to the DCC",
            HaploessentialPhenotypingStageState.HaploessentialPhenotypingAllDataSent,
            HaploessentialPhenotypingStageState.HaploessentialPhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return HaploessentialPhenotypingAbortProcessor.class;
        }
    },
    abortWhenHaploessentialPhenotypingAllDataProcessed(
            "Abort haplo-essential phenotyping when all phenotype data has been processed by the DCC",
            HaploessentialPhenotypingStageState.HaploessentialPhenotypingAllDataProcessed,
            HaploessentialPhenotypingStageState.HaploessentialPhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return HaploessentialPhenotypingAbortProcessor.class;
        }
    },
    abortWhenHaploessentialPhenotypingFinished(
            "Abort haplo-essential phenotyping when it is finished",
            HaploessentialPhenotypingStageState.HaploessentialPhenotypingFinished,
            HaploessentialPhenotypingStageState.HaploessentialPhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return HaploessentialPhenotypingAbortProcessor.class;
        }
    };

    HaploessentialEvent(
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

    public static HaploessentialEvent getEventByName(String name) {
        HaploessentialEvent[] haploessentialEvents = HaploessentialEvent.values();
        for (HaploessentialEvent haploessentialEvent : haploessentialEvents) {
            if (haploessentialEvent.name().equalsIgnoreCase(name))
                return haploessentialEvent;
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
        return Arrays.asList(HaploessentialEvent.values());
    }
}
