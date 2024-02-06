package org.gentar.biology.plan.engine.crispr;

import org.gentar.biology.plan.engine.crispr.processors.*;
import org.gentar.biology.plan.engine.processors.PlanProcessorWithoutValidations;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.gentar.statemachine.Processor;
import org.gentar.statemachine.StateMachineConstants;

import java.util.Arrays;
import java.util.List;

public enum HaploessentialProductionPlanEvent implements ProcessEvent
{
    abortWhenInProgress(
            "Abort the plan that is in progress",
            HaploessentialProductionPlanState.AttemptInProgress,
            HaploessentialProductionPlanState.AttemptAborted,
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
            HaploessentialProductionPlanState.PlanCreated,
            HaploessentialProductionPlanState.PlanAbandoned,
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
            HaploessentialProductionPlanState.PlanCreated,
            HaploessentialProductionPlanState.AttemptInProgress,
            StateMachineConstants.NOT_TRIGGERED_BY_USER,
            "executed by the system when details of the attempt are registered")
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return AttemptInProgressProcessor.class;
                }
            },
    updateToEmbryosObtained(
            "Update to embryos obtained for the attempt",
            HaploessentialProductionPlanState.AttemptInProgress,
            HaploessentialProductionPlanState.EmbryosObtained,
            StateMachineConstants.NOT_TRIGGERED_BY_USER,
            "executed by the system when injected embryos are registered")
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return EmbryosObtainedProcessor.class;
                }
            },
    abortWhenEmbryosObtained(
            "Abort the plan after embryos obtained",
            HaploessentialProductionPlanState.EmbryosObtained,
            HaploessentialProductionPlanState.AttemptAborted,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return AbortEmbryosObtainedProcessor.class;
                }
            };

    HaploessentialProductionPlanEvent(
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
        return Arrays.asList(HaploessentialProductionPlanEvent.values());
    }
}

