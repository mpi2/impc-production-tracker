package org.gentar.biology.colony.engine;

import org.gentar.biology.colony.engine.processors.ColonyAbortProcessor;
import org.gentar.biology.colony.engine.processors.ColonyProcessorWithoutValidations;
import org.gentar.biology.colony.engine.processors.ConfirmGenotypeProcessor;
import org.gentar.biology.colony.engine.processors.ReverseGenotypeConfirmationProcessor;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.gentar.statemachine.Processor;
import org.gentar.statemachine.StateMachineConstants;

import java.util.Arrays;
import java.util.List;

public enum ColonyEvent implements ProcessEvent
{
    confirmGenotypeWhenInProgress(
        "Confirm Genotype when Genotype In Progress",
        ColonyState.GenotypeInProgress,
        ColonyState.GenotypeConfirmed,
        StateMachineConstants.TRIGGERED_BY_USER,
        null)
        {
            @Override
            public Class<? extends Processor> getNextStepProcessor()
            {
                return ConfirmGenotypeProcessor.class;
            }
        },
    updateFromGenotypeInProgressToGenotypeNotConfirmed(
        "Update from Genotype In Progress to Genotype Not Confirmed",
        ColonyState.GenotypeInProgress,
        ColonyState.GenotypeNotConfirmed,
        StateMachineConstants.TRIGGERED_BY_USER,
        null)
        {
            @Override
            public Class<? extends Processor> getNextStepProcessor()
            {
                return ColonyProcessorWithoutValidations.class;
            }
        },
    reverseGenotypeConfirmation(
        "Reverse genotype confirmation",
        ColonyState.GenotypeConfirmed,
        ColonyState.GenotypeNotConfirmed,
        StateMachineConstants.TRIGGERED_BY_USER,
        null)
        {
            @Override
            public Class<? extends Processor> getNextStepProcessor()
            {
                return ReverseGenotypeConfirmationProcessor.class;
            }
        },
    confirmGenotypeWhenNotConfirmed(
        "Update from Genotype Not Confirmed to Genotype Confirmed",
        ColonyState.GenotypeNotConfirmed,
        ColonyState.GenotypeConfirmed,
        StateMachineConstants.TRIGGERED_BY_USER,
        null)
        {
            @Override
            public Class<? extends Processor> getNextStepProcessor()
            {
                return ConfirmGenotypeProcessor.class;
            }
        },
    updateToGenotypeExtinct(
        "Update from Genotype Confirmed to Genotype Extinct",
        ColonyState.GenotypeConfirmed,
        ColonyState.GenotypeExtinct,
        StateMachineConstants.TRIGGERED_BY_USER,
        null)
        {
            @Override
            public Class<? extends Processor> getNextStepProcessor()
            {
                return ColonyProcessorWithoutValidations.class;
            }
        },
    abortGenotypeNotConfirmed(
        "Abort a colony whose genotype has not been confirmed",
        ColonyState.GenotypeNotConfirmed,
        ColonyState.ColonyAborted,
        StateMachineConstants.TRIGGERED_BY_USER,
        null)
        {
            @Override
            public Class<? extends Processor> getNextStepProcessor()
            {
                return ColonyAbortProcessor.class;
            }
        },
    abortGenotypeConfirmed(
        "Abort a colony that is Genotype Confirmed",
        ColonyState.GenotypeConfirmed,
        ColonyState.ColonyAborted,
        StateMachineConstants.TRIGGERED_BY_USER,
        null)
        {
            @Override
            public Class<? extends Processor> getNextStepProcessor()
            {
                return ColonyAbortProcessor.class;
            }
        },
    abortGenotypeInProgress(
        "Abort a colony that is Genotype In Progress",
        ColonyState.GenotypeInProgress,
        ColonyState.ColonyAborted,
        StateMachineConstants.TRIGGERED_BY_USER,
        null)
        {
            @Override
            public Class<? extends Processor> getNextStepProcessor()
            {
                return ColonyAbortProcessor.class;
            }
        };

    ColonyEvent(
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

    private final String description;
    private final ProcessState initialState;
    private final ProcessState endState;
    private final boolean triggeredByUser;
    private final String triggerNote;

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
        return Arrays.asList(ColonyEvent.values());
    }
}
