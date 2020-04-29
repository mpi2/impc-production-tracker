package org.gentar.biology.plan.engine.phenotyping;

import org.gentar.biology.plan.engine.phenotyping.processors.PhenotypePlanAbortProcessor;
import org.gentar.biology.plan.engine.phenotyping.processors.PhenotypingInProgressProcessor;
import org.gentar.biology.plan.engine.processors.PlanProcessorWithoutValidations;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.gentar.statemachine.Processor;
import org.gentar.statemachine.StateMachineConstants;
import java.util.Arrays;
import java.util.List;

public enum PhenotypePlanEvent implements ProcessEvent
{
    abandonWhenCreated(
            "Abandon the phenotyping plan that has been created",
            PhenotypePlanState.PlanCreated,
            PhenotypePlanState.PlanAbandoned,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return PlanProcessorWithoutValidations.class;
                }
            },
    updateToInProgress(
            "Update to phenotyping in progress",
            PhenotypePlanState.PlanCreated,
            PhenotypePlanState.PhenotypingInProgress,
            StateMachineConstants.NOT_TRIGGERED_BY_USER,
            "executed by the system when phenotyping details are registered")
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return PhenotypingInProgressProcessor.class;
                }
            },
    abortPhenotypingPlan(
        "Abort a phenotyping plan that is in progress",
        PhenotypePlanState.PhenotypingInProgress,
        PhenotypePlanState.PhenotypePlanAborted,
        StateMachineConstants.TRIGGERED_BY_USER,
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
