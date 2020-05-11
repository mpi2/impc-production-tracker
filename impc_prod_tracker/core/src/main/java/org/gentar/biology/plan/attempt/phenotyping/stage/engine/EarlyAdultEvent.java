package org.gentar.biology.plan.attempt.phenotyping.stage.engine;

import org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors.AllDataProcessedToPhenotypingFinishedProcessor;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors.AllDataSentToAllDataProcessedProcessor;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors.PhenotypingStageProcessorWithoutValidations;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors.RollbackToAllDataProcessedProcessor;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors.UpdateToPhenotypingStartedProcessor;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.gentar.statemachine.Processor;
import org.gentar.statemachine.StateMachineConstants;
import java.util.Arrays;
import java.util.List;

public enum EarlyAdultEvent implements ProcessEvent
{
    updateToRederivationStarted(
        "Started rederivation of the colony for phenotyping",
        EarlyAdultState.PhenotypingProductionRegistered,
        EarlyAdultState.RederivationStarted,
        StateMachineConstants.TRIGGERED_BY_USER,
        "executed by the user when rederivation is started."),
    updateToRederivationComplete(
        "Completed rederivation of the colony for phenotyping",
        EarlyAdultState.RederivationStarted,
        EarlyAdultState.RederivationComplete,
        StateMachineConstants.TRIGGERED_BY_USER,
        "executed by the user when rederivation is complete."),
    updateToPhenotypingStartedAfterRederivation(
        "Marked as started when the DCC recieves phenotype data following a rederivation step",
        EarlyAdultState.RederivationComplete,
        EarlyAdultState.PhenotypingStarted,
        StateMachineConstants.TRIGGERED_BY_USER,
        "executed by the DCC following completion of rederivation.")
        {
            @Override
            public Class<? extends Processor> getNextStepProcessor()
            {
                return UpdateToPhenotypingStartedProcessor.class;
            }
        },
    updateToPhenotypingStarted(
        "Marked as started when the DCC receives phenotype data",
        EarlyAdultState.PhenotypingProductionRegistered,
        EarlyAdultState.PhenotypingStarted,
        StateMachineConstants.TRIGGERED_BY_USER,
        "executed by the DCC when phenotyping is started.")
        {
            @Override
            public Class<? extends Processor> getNextStepProcessor()
            {
                return UpdateToPhenotypingStartedProcessor.class;
            }
        },
    phenotypingAllDataSent(
        "No more phenotype data will be sent to the DCC.",
        EarlyAdultState.PhenotypingStarted,
        EarlyAdultState.PhenotypingAllDataSent,
        StateMachineConstants.TRIGGERED_BY_USER,
        "Used to indicate all phenotype data has been sent to the DCC."),
    PhenotypingAllDataProcessed(
        "Set by the DCC when all phenotype data received and processed.",
        EarlyAdultState.PhenotypingAllDataSent,
        EarlyAdultState.PhenotypingAllDataProcessed,
        StateMachineConstants.TRIGGERED_BY_USER,
        "executed by the DCC when all phenotype data received and processed.")
        {
            @Override
            public Class<? extends Processor> getNextStepProcessor()
            {
                return AllDataSentToAllDataProcessedProcessor.class;
            }
        },
    phenotypingFinished(
        "Marked as finished by the CDA when all phenotype data published",
        EarlyAdultState.PhenotypingAllDataProcessed,
        EarlyAdultState.PhenotypingFinished,
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
        EarlyAdultState.PhenotypingAllDataSent,
        EarlyAdultState.PhenotypingStarted,
        StateMachineConstants.TRIGGERED_BY_USER,
        "Allows more data to be sent.")
        {
            @Override
            public Class<? extends Processor> getNextStepProcessor()
            {
                return UpdateToPhenotypingStartedProcessor.class;
            }
        },
    rollbackPhenotypingAllDataProcessed(
        "Rollback the state of phenotyping marked as having all phenotype data processed to allow data entry.",
        EarlyAdultState.PhenotypingAllDataProcessed,
        EarlyAdultState.PhenotypingStarted,
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
        EarlyAdultState.PhenotypingFinished,
        EarlyAdultState.PhenotypingAllDataProcessed,
        StateMachineConstants.TRIGGERED_BY_USER,
        "executed by the CDA when phenotyping needs updating.")
        {
            @Override
            public Class<? extends Processor> getNextStepProcessor()
            {
                return RollbackToAllDataProcessedProcessor.class;
            }
        },
    reverseAbortion(
        "Reverse abortion",
        EarlyAdultState.PhenotypeProductionAborted,
        EarlyAdultState.PhenotypingProductionRegistered,
        StateMachineConstants.TRIGGERED_BY_USER,
        null),
    abortWhenPhenotypeProductionRegistered(
        "Abort phenotyping when a phenotype attempt has been registered",
        EarlyAdultState.PhenotypingProductionRegistered,
        EarlyAdultState.PhenotypeProductionAborted,
        StateMachineConstants.TRIGGERED_BY_USER,
        null),
    abortWhenRederivationStarted(
        "Abort phenotyping when rederivation has been started",
        EarlyAdultState.RederivationStarted,
        EarlyAdultState.PhenotypeProductionAborted,
        StateMachineConstants.TRIGGERED_BY_USER,
        null),
    abortWhenRederivationComplete(
        "Abort phenotyping when rederivation is complete",
        EarlyAdultState.RederivationComplete,
        EarlyAdultState.PhenotypeProductionAborted,
        StateMachineConstants.TRIGGERED_BY_USER,
        null),
    abortWhenPhenotypingStarted(
        "Abort phenotyping when phenotyping has been started",
        EarlyAdultState.PhenotypingStarted,
        EarlyAdultState.PhenotypeProductionAborted,
        StateMachineConstants.TRIGGERED_BY_USER,
        null),
    abortWhenPhenotypingAllDataSent(
        "Abort phenotyping when all phenotype data has been sent to the DCC",
        EarlyAdultState.PhenotypingAllDataSent,
        EarlyAdultState.PhenotypeProductionAborted,
        StateMachineConstants.TRIGGERED_BY_USER,
        null),
    abortWhenPhenotypingAllDataProcessed(
        "Abort phenotyping when all phenotype data has been processed by the DCC",
        EarlyAdultState.PhenotypingAllDataProcessed,
        EarlyAdultState.PhenotypeProductionAborted,
        StateMachineConstants.TRIGGERED_BY_USER,
        null),
    abortWhenPhenotypingFinished(
        "Abort phenotyping when it is finished",
        EarlyAdultState.PhenotypingFinished,
        EarlyAdultState.PhenotypeProductionAborted,
        StateMachineConstants.TRIGGERED_BY_USER,
        null);

    EarlyAdultEvent(
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
    public ProcessState getInitialState()
    {
        return initialState;
    }

    @Override
    public ProcessState getEndState()
    {
        return endState;
    }

    @Override
    public boolean isTriggeredByUser()
    {
        return triggeredByUser;
    }

    @Override
    public String getTriggerNote()
    {
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
        return Arrays.asList(EarlyAdultEvent.values());
    }
}
