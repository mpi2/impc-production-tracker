package org.gentar.biology.plan.attempt.phenotyping.stage.engine;

import org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors.AllDataProcessedToPhenotypingFinishedProcessor;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors.AllDataSentToAllDataProcessedProcessor;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors.PhenotypingStageProcessorWithoutValidations;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors.PhenotypingStartedProcessor;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors.RollbackToAllDataProcessedProcessor;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors.UpdateToPhenotypingStartedProcessor;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.gentar.statemachine.Processor;
import org.gentar.statemachine.StateMachineConstants;
import java.util.Arrays;
import java.util.List;

public enum PhenotypingStageEvent implements ProcessEvent
{
    updateToPhenotypingStarted(
        "Marked as started when the DCC receives phenotype data",
        PhenotypingStageState.PhenotypingProductionRegistered,
        PhenotypingStageState.PhenotypingStarted,
        StateMachineConstants.TRIGGERED_BY_USER,
        "executed by the DCC when phenotyping is started.")
        {
            @Override
            public Class<? extends Processor> getNextStepProcessor()
            {
                return PhenotypingStartedProcessor.class;
            }
        },
    updateToPhenotypingAllDataSent(
        "No more phenotype data will be sent to the DCC.",
        PhenotypingStageState.PhenotypingStarted,
        PhenotypingStageState.PhenotypingAllDataSent,
        StateMachineConstants.TRIGGERED_BY_USER,
        "Used to indicate all phenotype data has been sent to the DCC."),
    updateToPhenotypingAllDataProcessed(
        "Set by the DCC when all phenotype data received and processed.",
        PhenotypingStageState.PhenotypingAllDataSent,
        PhenotypingStageState.PhenotypingAllDataProcessed,
        StateMachineConstants.TRIGGERED_BY_USER,
        "executed by the DCC when all phenotype data received and processed.")
        {
            @Override
            public Class<? extends Processor> getNextStepProcessor()
            {
                return AllDataSentToAllDataProcessedProcessor.class;
            }
        },
    updateToPhenotypingFinished(
        "Marked as finished by the CDA when all phenotype data published",
        PhenotypingStageState.PhenotypingAllDataProcessed,
        PhenotypingStageState.PhenotypingFinished,
        StateMachineConstants.TRIGGERED_BY_USER,
        "executed by the CDA.")
        {
            @Override
            public Class<? extends Processor> getNextStepProcessor()
            {
                return AllDataProcessedToPhenotypingFinishedProcessor.class;
            }
        },
    rollbackPhenotypingAllDataSent(
        "Rollback the state of phenotyping marked as having all phenotype data sent to allow data entry.",
        PhenotypingStageState.PhenotypingAllDataSent,
        PhenotypingStageState.PhenotypingStarted,
        StateMachineConstants.TRIGGERED_BY_USER,
        "Allows more data to be sent.")
        {
            @Override
            public Class<? extends Processor> getNextStepProcessor()
            {
                return AllDataProcessedToPhenotypingFinishedProcessor.class;
            }
        },
    rollbackPhenotypingAllDataProcessed(
        "Rollback the state of phenotyping marked as having all phenotype data processed to allow data entry.",
        PhenotypingStageState.PhenotypingAllDataProcessed,
        PhenotypingStageState.PhenotypingStarted,
        StateMachineConstants.TRIGGERED_BY_USER,
        "Executed by the DCC and used when more data needs to be sent.")
        {
            @Override
            public Class<? extends Processor> getNextStepProcessor()
            {
                return UpdateToPhenotypingStartedProcessor.class;
            }
        },
    rollbackPhenotypingFinished(
        "Rollback the state of phenotyping marked as finished to allow an update.",
        PhenotypingStageState.PhenotypingFinished,
        PhenotypingStageState.PhenotypingAllDataProcessed,
        StateMachineConstants.TRIGGERED_BY_USER,
        "executed by the CDA or DCC when phenotyping needs updating.")
        {
            @Override
            public Class<? extends Processor> getNextStepProcessor()
            {
                return RollbackToAllDataProcessedProcessor.class;
            }
        },
    reverseAbortion(
        "Reverse abortion",
        PhenotypingStageState.PhenotypeProductionAborted,
        PhenotypingStageState.PhenotypingProductionRegistered,
        StateMachineConstants.TRIGGERED_BY_USER,
        null),
    abortWhenPhenotypingProductionRegistered(
        "Abort phenotyping when a phenotype attempt has been registered",
        PhenotypingStageState.PhenotypingProductionRegistered,
        PhenotypingStageState.PhenotypeProductionAborted,
        StateMachineConstants.TRIGGERED_BY_USER,
        null),
    abortWhenPhenotypingStarted(
        "Abort phenotyping when phenotyping has been started",
        PhenotypingStageState.PhenotypingStarted,
        PhenotypingStageState.PhenotypeProductionAborted,
        StateMachineConstants.TRIGGERED_BY_USER,
        null),
    abortWhenPhenotypingAllDataSent(
        "Abort phenotyping when all phenotype data has been sent to the DCC",
        PhenotypingStageState.PhenotypingAllDataSent,
        PhenotypingStageState.PhenotypeProductionAborted,
        StateMachineConstants.TRIGGERED_BY_USER,
        null),
    abortWhenPhenotypingAllDataProcessed(
        "Abort phenotyping when all phenotype data has been processed by the DCC",
        PhenotypingStageState.PhenotypingAllDataProcessed,
        PhenotypingStageState.PhenotypeProductionAborted,
        StateMachineConstants.TRIGGERED_BY_USER,
        null),
    abortWhenPhenotypingFinished(
        "Abort phenotyping when it is finished",
        PhenotypingStageState.PhenotypingFinished,
        PhenotypingStageState.PhenotypeProductionAborted,
        StateMachineConstants.TRIGGERED_BY_USER,
        null);

    PhenotypingStageEvent(
        String description,
        ProcessState initialState,
        ProcessState endState,
        boolean triggeredByUser,
        String triggerNote)
    {
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
        return PhenotypingStageProcessorWithoutValidations.class;
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