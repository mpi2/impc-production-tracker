package org.gentar.biology.plan.engine.events;

import org.gentar.biology.plan.engine.PlanProcessor;
import org.gentar.biology.plan.engine.state.ProductionPlanState;
import org.gentar.biology.plan.engine.processors.PlanAbortProcessor;
import org.gentar.biology.plan.engine.processors.PlanAbortReverserProcessor;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.gentar.statemachine.Processor;
import java.util.Arrays;
import java.util.List;

public enum ProductionPlanEvent implements ProcessEvent
{
    abortWhenInProgress(
            "Abort plan in MI In Progress",
            ProductionPlanState.MicroInjectionInProgress,
            ProductionPlanState.Aborted,
            true,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return PlanAbortProcessor.class;
                }
            },
    abortWhenCreated(
            "Abort a plan that has been created",
            ProductionPlanState.PlanCreated,
            ProductionPlanState.Aborted,
            true,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return PlanAbortProcessor.class;
                }
            },
    inProgress(
            "Attempt in progress",
            ProductionPlanState.PlanCreated,
            ProductionPlanState.MicroInjectionInProgress,
            false,
            "executed by the system when details of the attempt are registered"),
    embryosProduced(
            "Embryos produced from the MI",
            ProductionPlanState.MicroInjectionInProgress,
            ProductionPlanState.EmbryosProduced,
            false,
            "executed by the system when injected embryos are registered"),
    foundersObtained(
            "Founder obtained from the MI",
            ProductionPlanState.EmbryosProduced,
            ProductionPlanState.FounderObtained,
            false,
            "executed by the system when a founder is registered."),
    reverseAbortion(
            "Reverse abortion",
            ProductionPlanState.Aborted,
            ProductionPlanState.PlanCreated,
            true,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return PlanAbortReverserProcessor.class;
                }
            },
    abortWhenEmbryosProduced(
            "Abort plan with MI embryos produced",
            ProductionPlanState.EmbryosProduced,
            ProductionPlanState.Aborted,
            true,
            "Only possible if...")
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return PlanAbortProcessor.class;
                }
            },
    abortWhenFounderObtained(
            "Abort plan with MI founder obtained",
            ProductionPlanState.FounderObtained,
            ProductionPlanState.Aborted,
            true,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return PlanAbortProcessor.class;
                }
            };

    ProductionPlanEvent(
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

    public static ProductionPlanEvent getEventByName(String name)
    {
        ProductionPlanEvent[] productionPlanEvents = ProductionPlanEvent.values();
        for (ProductionPlanEvent productionPlanEvent : productionPlanEvents)
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
    public Class<? extends Processor> getNextStepProcessor()
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

    public static List<ProcessEvent> getAllEvents()
    {
        return Arrays.asList(ProductionPlanEvent.values());
    }
}
