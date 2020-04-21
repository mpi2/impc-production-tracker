package org.gentar.biology.plan.engine.events;

import org.gentar.biology.plan.engine.state.PhenotypePlanState;
import org.gentar.biology.plan.engine.processors.PhenotypePlanAbortProcessor;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.gentar.statemachine.Processor;
import java.util.Arrays;
import java.util.List;

public enum PhenotypePlanEvent implements ProcessEvent
{
    abortPhenotypingPlan(
            "Abort a phenotyping plan",
            PhenotypePlanState.PlanCreated,
            PhenotypePlanState.PhenotypePlanAborted,
            true,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return PhenotypePlanAbortProcessor.class;
                }
            };

    PhenotypePlanEvent(
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

    public static PhenotypePlanEvent getEventByName(String name)
    {
        PhenotypePlanEvent[] phenotypePlanEvents = PhenotypePlanEvent.values();
        for (PhenotypePlanEvent phenotypePlanEvent : phenotypePlanEvents)
        {
            if (phenotypePlanEvent.name().equalsIgnoreCase(name))
                return phenotypePlanEvent;
        }
        return null;
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
        return Arrays.asList(PhenotypePlanEvent.values());
    }
}
