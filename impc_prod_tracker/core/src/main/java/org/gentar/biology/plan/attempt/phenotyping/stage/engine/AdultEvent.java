package org.gentar.biology.plan.attempt.phenotyping.stage.engine;

import org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors.*;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.gentar.statemachine.Processor;
import org.gentar.statemachine.StateMachineConstants;
import java.util.Arrays;
import java.util.List;

public enum AdultEvent implements ProcessEvent
{
    updateToRederivationStarted(
        "Started rederivation of the colony for phenotyping",
        AdultState.PhenotypingRegistered,
        AdultState.RederivationStarted,
        StateMachineConstants.TRIGGERED_BY_USER,
        "executed by the user when rederivation is started."),

    updateToRederivationComplete(
        "Completed rederivation of the colony for phenotyping",
        AdultState.RederivationStarted,
        AdultState.RederivationComplete,
        StateMachineConstants.TRIGGERED_BY_USER,
        "executed by the user when rederivation is complete."),

    updateToPhenotypingStartedAfterRederivation(
        "Marked as started when the DCC recieves phenotype data following a rederivation step",
        AdultState.RederivationComplete,
        AdultState.PhenotypingStarted,
        StateMachineConstants.TRIGGERED_BY_USER,
        "executed by the DCC following completion of rederivation.")
        {
            @Override
            public Class<? extends Processor> getNextStepProcessor()
            {
                return PhenotypingStartedProcessorWithTissueCreation.class;
            }
        },
    updateToPhenotypingStarted(
        "Marked as started when the DCC receives phenotype data",
        AdultState.PhenotypingRegistered,
        AdultState.PhenotypingStarted,
        StateMachineConstants.TRIGGERED_BY_USER,
        "executed by the DCC when phenotyping is started.")
        {
            @Override
            public Class<? extends Processor> getNextStepProcessor()
            {
                return PhenotypingStartedProcessorWithTissueCreation.class;
            }
        },
    updateToPhenotypingAllDataSent(
        "No more phenotype data will be sent to the DCC.",
        AdultState.PhenotypingStarted,
        AdultState.PhenotypingAllDataSent,
        StateMachineConstants.TRIGGERED_BY_USER,
        "Used to indicate all phenotype data has been sent to the DCC."),

    updateToPhenotypingAllDataProcessed(
        "Set by the DCC when all phenotype data received and processed.",
        AdultState.PhenotypingAllDataSent,
        AdultState.PhenotypingAllDataProcessed,
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
        AdultState.PhenotypingAllDataProcessed,
        AdultState.PhenotypingFinished,
        StateMachineConstants.TRIGGERED_BY_USER,
        "executed by the CDA.")
        {
            @Override
            public Class<? extends Processor> getNextStepProcessor()
            {
                return AllDataProcessedToPhenotypingFinishedProcessor.class;
            }
        },
    rollbackPhenotypingStarted(
            "Rollback the state of phenotyping marked as having phenotyping started.",
            AdultState.PhenotypingStarted,
            AdultState.PhenotypingRegistered,
            StateMachineConstants.TRIGGERED_BY_USER,
            "Executed by the DCC.")
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return RollbackToPhenotypingRegisteredProcessor.class;
                }
            },
    rollbackPhenotypingAllDataSent(
        "Rollback the state of phenotyping marked as having all phenotype data sent to allow data entry.",
        AdultState.PhenotypingAllDataSent,
        AdultState.PhenotypingStarted,
        StateMachineConstants.TRIGGERED_BY_USER,
        "Allows more data to be sent.")
        {
            @Override
            public Class<? extends Processor> getNextStepProcessor()
            {
                return PhenotypingStartedProcessor.class;
            }
        },
    rollbackPhenotypingAllDataProcessed(
        "Rollback the state of phenotyping marked as having all phenotype data processed to allow data entry.",
        AdultState.PhenotypingAllDataProcessed,
        AdultState.PhenotypingStarted,
        StateMachineConstants.TRIGGERED_BY_USER,
        "Executed by the DCC and used when more data needs to be sent.")
        {
            @Override
            public Class<? extends Processor> getNextStepProcessor()
            {
                return PhenotypingStartedProcessor.class;
            }
        },
    rollbackPhenotypingFinished(
        "Rollback the state of phenotyping marked as finished to allow an update.",
        AdultState.PhenotypingFinished,
        AdultState.PhenotypingAllDataProcessed,
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
        AdultState.PhenotypeProductionAborted,
        AdultState.PhenotypingRegistered,
        StateMachineConstants.TRIGGERED_BY_USER,
        null),
    abortWhenPhenotypeRegistered(
        "Abort phenotyping when a phenotype attempt has been registered",
        AdultState.PhenotypingRegistered,
        AdultState.PhenotypeProductionAborted,
        StateMachineConstants.TRIGGERED_BY_USER,
        null),
    abortWhenRederivationStarted(
        "Abort phenotyping when rederivation has been started",
        AdultState.RederivationStarted,
        AdultState.PhenotypeProductionAborted,
        StateMachineConstants.TRIGGERED_BY_USER,
        null),
    abortWhenRederivationComplete(
        "Abort phenotyping when rederivation is complete",
        AdultState.RederivationComplete,
        AdultState.PhenotypeProductionAborted,
        StateMachineConstants.TRIGGERED_BY_USER,
        null);

    AdultEvent(
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

    private final String description;
    private final ProcessState initialState;
    private final ProcessState endState;
    private final boolean triggeredByUser;
    private final String triggerNote;

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
        return Arrays.asList(AdultEvent.values());
    }
}
