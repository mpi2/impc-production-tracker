package org.gentar.biology.colony.engine;

import org.gentar.biology.colony.engine.processors.ColonyProcessor;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.gentar.statemachine.Processor;
import org.gentar.statemachine.StateMachineConstants;
import java.util.Arrays;
import java.util.List;

public enum ColonyEvent implements ProcessEvent
{
    confirmGenotype(
        "Confirm Genotype",
        ColonyState.GenotypeNotConfirmed,
        ColonyState.GenotypeConfirmed,
        StateMachineConstants.TRIGGERED_BY_USER,
        null)
        {
            @Override
            public Class<? extends Processor> getNextStepProcessor()
            {
                return ColonyProcessor.class;
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
                return ColonyProcessor.class;
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

    private String description;
    private ProcessState initialState;
    private ProcessState endState;
    private boolean triggeredByUser;
    private String triggerNote;

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
