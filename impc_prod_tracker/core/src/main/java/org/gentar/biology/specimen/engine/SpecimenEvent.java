package org.gentar.biology.specimen.engine;

import org.gentar.biology.specimen.engine.processors.SpecimenProcessor;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.gentar.statemachine.Processor;
import org.gentar.statemachine.StateMachineConstants;
import java.util.Arrays;
import java.util.List;

public enum SpecimenEvent implements ProcessEvent
{
    confirmGenotype(
            "Confirm Genotype",
            SpecimenState.GenotypeNotConfirmed,
            SpecimenState.GenotypeConfirmed,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return SpecimenProcessor.class;
                }
            },
    reverseGenotypeConfirmation(
            "Reverse genotype confirmation",
            SpecimenState.GenotypeConfirmed,
            SpecimenState.GenotypeNotConfirmed,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return SpecimenProcessor.class;
                }
            };

    SpecimenEvent(
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
        return Arrays.asList(org.gentar.biology.specimen.engine.SpecimenEvent.values());
    }
}