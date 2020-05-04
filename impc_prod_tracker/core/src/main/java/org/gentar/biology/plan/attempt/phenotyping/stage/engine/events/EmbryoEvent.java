package org.gentar.biology.plan.attempt.phenotyping.stage.engine.events;

import org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors.EmbryoPhenotypingAbortProcessor;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors.EmbryoPhenotypingAbortReverserProcessor;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors.PhenotypingStageProcessor;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.state.EmbryoPhenotypingStageState;

import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.gentar.statemachine.Processor;

import java.util.Arrays;
import java.util.List;

public enum EmbryoEvent implements ProcessEvent {
    embryoPhenotypingStarted(
            "Marked as started when the DCC receives embryo phenotype data",
            EmbryoPhenotypingStageState.EmbryoPhenotypingProductionRegistered,
            EmbryoPhenotypingStageState.EmbryoPhenotypingStarted,
            true,
            "executed by the DCC when embryo phenotyping is started."),
    embryoPhenotypingAllDataSent(
            "No more embryo phenotype data will be sent to the DCC.",
            EmbryoPhenotypingStageState.EmbryoPhenotypingStarted,
            EmbryoPhenotypingStageState.EmbryoPhenotypingAllDataSent,
            true,
            "Used to indicate all embryo phenotype data has been sent to the DCC."),
    embryoPhenotypingAllDataProcessed(
            "Set by the DCC when all embryo phenotype data received and processed.",
            EmbryoPhenotypingStageState.EmbryoPhenotypingAllDataSent,
            EmbryoPhenotypingStageState.EmbryoPhenotypingAllDataProcessed,
            true,
            "executed by the DCC when all embryo phenotype data received and processed."),
    embryoPhenotypingFinished(
            "Marked as finished by the CDA when all embryo phenotype data published",
            EmbryoPhenotypingStageState.EmbryoPhenotypingAllDataProcessed,
            EmbryoPhenotypingStageState.EmbryoPhenotypingFinished,
            true,
            "executed by the CDA."),
    rollbackEmbryoPhenotypingAllDataSent(
            "Rollback the state of embryo phenotyping marked as having all phenotype data sent to allow data entry.",
            EmbryoPhenotypingStageState.EmbryoPhenotypingAllDataSent,
            EmbryoPhenotypingStageState.EmbryoPhenotypingStarted,
            true,
            "Allows more data to be sent."),
    rollbackEmbryoPhenotypingAllDataProcessed(
            "Rollback the state of embryo phenotyping marked as having all phenotype data processed to allow data entry.",
            EmbryoPhenotypingStageState.EmbryoPhenotypingAllDataProcessed,
            EmbryoPhenotypingStageState.EmbryoPhenotypingStarted,
            true,
            "Executed by the DCC and used when more data needs to be sent."),
    rollbackEmbryoPhenotypingFinished(
            "Rollback the state of embryo phenotyping marked as finished to allow an update.",
            EmbryoPhenotypingStageState.EmbryoPhenotypingFinished,
            EmbryoPhenotypingStageState.EmbryoPhenotypingAllDataSent,
            true,
            "executed by the CDA when embryo phenotyping needs updating."),
    reverseAbortion(
            "Reverse abortion",
            EmbryoPhenotypingStageState.EmbryoPhenotypeProductionAborted,
            EmbryoPhenotypingStageState.EmbryoPhenotypingProductionRegistered,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return EmbryoPhenotypingAbortReverserProcessor.class;
        }
    },
    abortWhenEmbryoPhenotypingProductionRegistered(
            "Abort embryo phenotyping when a phenotype attempt has been registered",
            EmbryoPhenotypingStageState.EmbryoPhenotypingProductionRegistered,
            EmbryoPhenotypingStageState.EmbryoPhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return EmbryoPhenotypingAbortProcessor.class;
        }
    },
    abortWhenEmbryoPhenotypingStarted(
            "Abort embryo phenotyping when phenotyping has been started",
            EmbryoPhenotypingStageState.EmbryoPhenotypingStarted,
            EmbryoPhenotypingStageState.EmbryoPhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return EmbryoPhenotypingAbortProcessor.class;
        }
    },
    abortWhenEmbryoPhenotypingAllDataSent(
            "Abort embryo phenotyping when all phenotype data has been sent to the DCC",
            EmbryoPhenotypingStageState.EmbryoPhenotypingAllDataSent,
            EmbryoPhenotypingStageState.EmbryoPhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return EmbryoPhenotypingAbortProcessor.class;
        }
    },
    abortWhenEmbryoPhenotypingAllDataProcessed(
            "Abort embryo phenotyping when all phenotype data has been processed by the DCC",
            EmbryoPhenotypingStageState.EmbryoPhenotypingAllDataProcessed,
            EmbryoPhenotypingStageState.EmbryoPhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return EmbryoPhenotypingAbortProcessor.class;
        }
    },
    abortWhenEmbryoPhenotypingFinished(
            "Abort embryo phenotyping when it is finished",
            EmbryoPhenotypingStageState.EmbryoPhenotypingFinished,
            EmbryoPhenotypingStageState.EmbryoPhenotypeProductionAborted,
            true,
            null) {
        @Override
        public Class<? extends Processor> getNextStepProcessor() {
            return EmbryoPhenotypingAbortProcessor.class;
        }
    };

    EmbryoEvent(
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

    public static EmbryoEvent getEventByName(String name) {
        EmbryoEvent[] embryoEvents = EmbryoEvent.values();
        for (EmbryoEvent embryoEvent : embryoEvents) {
            if (embryoEvent.name().equalsIgnoreCase(name))
                return embryoEvent;
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
        return Arrays.asList(EmbryoEvent.values());
    }
}
