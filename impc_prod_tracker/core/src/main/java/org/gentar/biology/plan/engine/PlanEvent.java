package org.gentar.biology.plan.engine;

import org.gentar.biology.plan.engine.processors.AbortProcessor;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.gentar.statemachine.Processor;

public enum PlanEvent implements ProcessEvent
{
    abortWhenInProgress(
        "Abort plan in MI In Progress",
        PlanState.MicroInjectionInProgress,
        PlanState.Aborted,
        true,
        null)
        {
            @Override
            public Class<? extends Processor> nextStepProcessor()
            {
                return AbortProcessor.class;
            }
        },
    complete(
        "Complete MI",
        PlanState.MicroInjectionInProgress,
        PlanState.MicroInjectionComplete,
        false,
        "executed by system when there is any outcome registered"),
    reverseAbortion(
        "Reverse abortion",
        PlanState.Aborted,
        PlanState.MicroInjectionInProgress,
        true,
        null),
    abortWhenComplete(
        "Abort plan with MI Complete",
        PlanState.MicroInjectionComplete,
        PlanState.Aborted,
        true,
        "Only possible if...");

    PlanEvent(
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
    public Class<? extends Processor> nextStepProcessor()
    {
        return PlanProcessor.class;
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
}
