package org.gentar.biology.plan.engine.breeding;

import org.gentar.biology.plan.engine.breeding.processors.BreedingPlanAbortProcessor;
import org.gentar.biology.plan.engine.breeding.processors.BreedingStartedProcessor;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.gentar.statemachine.Processor;
import org.gentar.statemachine.StateMachineConstants;
import java.util.Arrays;
import java.util.List;

public enum BreedingPlanEvent implements ProcessEvent 
{
    updateToBreedingStarted(
            "Update to breeding plan is started.",
            BreedingPlanState.PlanCreated,
            BreedingPlanState.BreedingStarted,
            StateMachineConstants.NOT_TRIGGERED_BY_USER,
            null)
        {
            @Override
            public Class<? extends Processor> getNextStepProcessor()
            {
                return BreedingStartedProcessor.class;
            }
        },
    updateToBreedingComplete(
            "Update to breeding plan is complete.",
            BreedingPlanState.BreedingStarted,
            BreedingPlanState.BreedingComplete,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
        {
            @Override
            public Class<? extends Processor> getNextStepProcessor()
            {
                return null;
            }
        },
    abandonWhenCreated(
            "Abandon a breeding plan that has been created",
            BreedingPlanState.PlanCreated,
            BreedingPlanState.PlanAbandoned,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return BreedingPlanAbortProcessor.class;
                }
            },
    abortWhenBreedingStarted(
            "Abort a breeding plan that has been started.",
            BreedingPlanState.BreedingStarted,
            BreedingPlanState.BreedingAborted,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return BreedingPlanAbortProcessor.class;
                }
            },
    abortWhenBreedingComplete(
            "Abort a breeding plan that is complete",
            BreedingPlanState.BreedingComplete,
            BreedingPlanState.BreedingAborted,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return BreedingPlanAbortProcessor.class;
                }
            };

    BreedingPlanEvent(
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
        return Arrays.asList(BreedingPlanEvent.values());
    }
}
