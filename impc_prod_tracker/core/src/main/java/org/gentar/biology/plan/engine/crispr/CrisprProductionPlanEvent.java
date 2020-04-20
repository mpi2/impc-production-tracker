package org.gentar.biology.plan.engine.crispr;

import org.gentar.biology.plan.engine.crispr.processors.AttemptInProgressProcessor;
import org.gentar.biology.plan.engine.crispr.processors.EmbryosObtainedProcessor;
import org.gentar.biology.plan.engine.processors.PlanAbortProcessor;
import org.gentar.biology.plan.engine.processors.PlanAbortReverserProcessor;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.gentar.statemachine.Processor;
import org.gentar.statemachine.StateMachineConstants;
import java.util.Arrays;
import java.util.List;

public enum CrisprProductionPlanEvent implements ProcessEvent
{
    abortWhenInProgress(
            "Abort the plan that is in progress",
            CrisprProductionPlanState.AttemptInProgress,
            CrisprProductionPlanState.AttemptAborted,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return PlanAbortProcessor.class;
                }
            },
    abortWhenCreated(
            "Abort the plan that has been created",
            CrisprProductionPlanState.PlanCreated,
            CrisprProductionPlanState.AttemptAborted,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return PlanAbortProcessor.class;
                }
            },
    changeToInProgress(
            "Attempt in progress",
            CrisprProductionPlanState.PlanCreated,
            CrisprProductionPlanState.AttemptInProgress,
            StateMachineConstants.NOT_TRIGGERED_BY_USER,
            "executed by the system when details of the attempt are registered")
        {
            @Override
            public Class<? extends Processor> getNextStepProcessor()
            {
                return AttemptInProgressProcessor.class;
            }
        },
    changeToEmbryosObtained(
            "Embryos obtained from the attempt",
            CrisprProductionPlanState.AttemptInProgress,
            CrisprProductionPlanState.EmbryosObtained,
            StateMachineConstants.NOT_TRIGGERED_BY_USER,
            "executed by the system when injected embryos are registered")
        {
            @Override
            public Class<? extends Processor> getNextStepProcessor()
            {
                return EmbryosObtainedProcessor.class;
            }
        },
    changeToGlt(
            "Germ line transmission obtained for the attempt",
            CrisprProductionPlanState.EmbryosObtained,
            CrisprProductionPlanState.GLT,
            StateMachineConstants.NOT_TRIGGERED_BY_USER,
            "executed by the system when germ line transmission is registered.")
        {
            @Override
            public Class<? extends Processor> getNextStepProcessor()
            {
                return null;
            }
        },
    reverseAbortion(
            "Reverse abortion",
            CrisprProductionPlanState.AttemptAborted,
            CrisprProductionPlanState.PlanCreated,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return PlanAbortReverserProcessor.class;
                }
            },
    abortWhenEmbryosObtained(
            "Abort the plan after embryos obtained",
            CrisprProductionPlanState.EmbryosObtained,
            CrisprProductionPlanState.AttemptAborted,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return PlanAbortProcessor.class;
                }
            },
    abortWhenGLT(
            "Abort the plan after germ line transmission obtained",
            CrisprProductionPlanState.GLT,
            CrisprProductionPlanState.AttemptAborted,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return PlanAbortProcessor.class;
                }
            };

    CrisprProductionPlanEvent(
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

    public static CrisprProductionPlanEvent getEventByName(String name)
    {
        CrisprProductionPlanEvent[] productionPlanEvents = CrisprProductionPlanEvent.values();
        for (CrisprProductionPlanEvent productionPlanEvent : productionPlanEvents)
        {
            if (productionPlanEvent.name().equalsIgnoreCase(name))
                return productionPlanEvent;
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
        return Arrays.asList(CrisprProductionPlanEvent.values());
    }
}
