package org.gentar.biology.plan.engine.es_cell;

import org.gentar.biology.plan.engine.es_cell.processors.AbortChimerasFounderObtainedProcessor;
import org.gentar.biology.plan.engine.es_cell.processors.EsCellAttemptInProgressProcessor;
import org.gentar.biology.plan.engine.es_cell.processors.ChimerasFounderObtainedProcessor;
import org.gentar.biology.plan.engine.processors.PlanProcessorWithoutValidations;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.gentar.statemachine.Processor;
import org.gentar.statemachine.StateMachineConstants;

import java.util.Arrays;
import java.util.List;

public enum EsCellProductionPlanEvent implements ProcessEvent
{
    abortWhenInProgress(
            "Abort the plan that is in progress",
            EsCellProductionPlanState.AttemptInProgress,
            EsCellProductionPlanState.AttemptAborted,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return PlanProcessorWithoutValidations.class;
                }
            },
    abandonWhenCreated(
            "Abandon the plan that has been created",
            EsCellProductionPlanState.PlanCreated,
            EsCellProductionPlanState.PlanAbandoned,
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
            "Update to attempt in progress",
            EsCellProductionPlanState.PlanCreated,
            EsCellProductionPlanState.AttemptInProgress,
            StateMachineConstants.NOT_TRIGGERED_BY_USER,
            "executed by the system when details of the attempt are registered")
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return EsCellAttemptInProgressProcessor.class;
                }
            },
    updateToChimerasFounderObtained(
            "Update to chimeras/founder obtained for the attempt",
            EsCellProductionPlanState.AttemptInProgress,
            EsCellProductionPlanState.ChimerasFounderObtained,
            StateMachineConstants.NOT_TRIGGERED_BY_USER,
            "executed by the system when a founder is registered.")
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return ChimerasFounderObtainedProcessor.class;
                }
            },
    abortWhenChimerasFounderObtained(
            "Abort the plan after a founder obtained",
            EsCellProductionPlanState.ChimerasFounderObtained,
            EsCellProductionPlanState.AttemptAborted,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return AbortChimerasFounderObtainedProcessor.class;
                }
            };

    EsCellProductionPlanEvent(
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
        return Arrays.asList(EsCellProductionPlanEvent.values());
    }
}
