package org.gentar.biology.plan.engine.phenotyping;

import org.gentar.biology.plan.engine.phenotyping.processors.PhenotypePlanAbandonProcessor;
import org.gentar.biology.plan.engine.phenotyping.processors.PhenotypePlanAbortProcessor;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.gentar.statemachine.Processor;
import org.gentar.statemachine.StateMachineConstants;
import java.util.Arrays;
import java.util.List;

public enum PhenotypePlanEvent implements ProcessEvent
{
    abandonWhenCreated(
            "Abandon a phenotyping plan that has no associated phenotyping stage information",
            PhenotypePlanState.PlanCreated,
            PhenotypePlanState.PlanAbandoned,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return PhenotypePlanAbandonProcessor.class;
                }
            },
    abortPhenotypingPlan(
            "Abort a phenotyping plan that has associated aborted phenotyping stage information",
            PhenotypePlanState.PlanCreated,
            PhenotypePlanState.PhenotypePlanAborted,
            StateMachineConstants.NOT_TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return PhenotypePlanAbortProcessor.class;
                }
            },
    reverseAbortedPhenotypingPlan(
            "Reverse an aborted phenotyping plan that has valid associated phenotyping stage information",
            PhenotypePlanState.PhenotypePlanAborted,
            PhenotypePlanState.PlanCreated,
            StateMachineConstants.NOT_TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return ReversePhenotypePlanAbortProcessor.class;
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
