package org.gentar.biology.plan.engine.events;

import org.gentar.biology.plan.engine.PlanProcessor;
import org.gentar.biology.plan.engine.processors.LateAdultPhenotypePlanAbortProcessor;
import org.gentar.biology.plan.engine.processors.LateAdultPhenotypePlanAbortReverserProcessor;
import org.gentar.biology.plan.engine.state.LateAdultPhenotypePlanState;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.gentar.statemachine.Processor;

import java.util.Arrays;
import java.util.List;


public enum LateAdultPhenotypePlanEvent implements ProcessEvent
{
    abortWhenRegisteredForLateAdultPhenotypingProduction(
            "Abort a registered late adult phenotype plan.",
            LateAdultPhenotypePlanState.RegisteredForLateAdultPhenotypingProduction,
            LateAdultPhenotypePlanState.LateAdultPhenotypeProductionAborted,
            true,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return LateAdultPhenotypePlanAbortProcessor.class;
                }
            },
    lateAdultPhenotypingStarted(
            "Marked as started when the DCC recieves late adult phenotype data.",
            LateAdultPhenotypePlanState.RegisteredForLateAdultPhenotypingProduction,
            LateAdultPhenotypePlanState.LateAdultPhenotypingStarted,
            false,
            "executed by the DCC when late adult phenotyping is started."),
    lateAdultPhenotypingComplete(
            "Marked as complete when the CDA receives phenotype data",
            LateAdultPhenotypePlanState.LateAdultPhenotypingStarted,
            LateAdultPhenotypePlanState.LateAdultPhenotypingComplete,
            false,
            "executed by the CDA when late adult phenotyping data recieved."),
    lateAdultPhenotypingFinished(
            "Late Adult Phenotype Plan is marked as finished by the DCC",
            LateAdultPhenotypePlanState.LateAdultPhenotypingComplete,
            LateAdultPhenotypePlanState.LateAdultPhenotypingFinished,
            false,
            "executed by the DCC when all phenotype data received."),
    reverseAbortion(
            "Reverse abortion",
            LateAdultPhenotypePlanState.LateAdultPhenotypeProductionAborted,
            LateAdultPhenotypePlanState.RegisteredForLateAdultPhenotypingProduction,
            true,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return LateAdultPhenotypePlanAbortReverserProcessor.class;
                }
            },
    abortWhenLateAdultPhenotypingStarted(
            "Abort a late adult phenotyping plan when late adult phenotyping is started",
            LateAdultPhenotypePlanState.LateAdultPhenotypingStarted,
            LateAdultPhenotypePlanState.LateAdultPhenotypeProductionAborted,
            true,
            "Can only be carried out by the DCC or CDA")
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return LateAdultPhenotypePlanAbortProcessor.class;
                }
            },
    abortWhenLateAdultPhenotypingComplete(
            "Abort a late adult phenotyping plan when late adult phenotyping is complete",
            LateAdultPhenotypePlanState.LateAdultPhenotypingComplete,
            LateAdultPhenotypePlanState.LateAdultPhenotypeProductionAborted,
            true,
            "Can only be carried out by the DCC or CDA")
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return LateAdultPhenotypePlanAbortProcessor.class;
                }
            },
    abortWhenLateAdultPhenotypingFinished(
            "Abort a late adult phenotyping plan when late adult phenotyping is finished",
            LateAdultPhenotypePlanState.LateAdultPhenotypingFinished,
            LateAdultPhenotypePlanState.LateAdultPhenotypeProductionAborted,
            true,
            "Can only be carried out by the DCC or CDA")
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return LateAdultPhenotypePlanAbortProcessor.class;
                }
            };

    LateAdultPhenotypePlanEvent(
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

    public static LateAdultPhenotypePlanEvent getEventByName(String name)
    {
        LateAdultPhenotypePlanEvent[] LateAdultPhenotypePlanEvents = LateAdultPhenotypePlanEvent.values();
        for (LateAdultPhenotypePlanEvent LateAdultPhenotypePlanEvent : LateAdultPhenotypePlanEvents)
        {
            if (LateAdultPhenotypePlanEvent.name().equalsIgnoreCase(name))
                return LateAdultPhenotypePlanEvent;
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
        return Arrays.asList(LateAdultPhenotypePlanEvent.values());
    }
}
