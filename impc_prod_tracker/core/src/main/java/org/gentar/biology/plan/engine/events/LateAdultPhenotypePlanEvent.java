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
    lateAdultPhenotypingDataReceived(
            "The CDA has received phenotype data",
            LateAdultPhenotypePlanState.LateAdultPhenotypingStarted,
            LateAdultPhenotypePlanState.LateAdultPhenotypingDataReceived,
            false,
            "executed by the CDA when late adult phenotyping data recieved."),
    lateAdultPhenotypingAllDataSent(
            "No more Late Adult Phenotype data will be sent to the DCC.",
            LateAdultPhenotypePlanState.LateAdultPhenotypingDataReceived,
            LateAdultPhenotypePlanState.LateAdultPhenotypingAllDataSent,
            true,
            "Used to indicate all phenotype data has been sent to the DCC."),
    lateAdultPhenotypingFinished(
            "Late Adult Phenotype Plan is marked as finished by the DCC",
            LateAdultPhenotypePlanState.LateAdultPhenotypingAllDataSent,
            LateAdultPhenotypePlanState.LateAdultPhenotypingFinished,
            false,
            "executed by the DCC when all phenotype data received and validated."),
    revertLateAdultPhenotypingAllDataSent(
            "Rollback the state of a Late Adult Phenotype Plan marked as having all phenotype data sent to allow data entry.",
            LateAdultPhenotypePlanState.LateAdultPhenotypingAllDataSent,
            LateAdultPhenotypePlanState.LateAdultPhenotypingDataReceived,
            true,
            "Used when more data needs to be sent for a plan in the LateAdultPhenotypingAllDataSent state."),
    revertLateAdultPhenotypingFinished(
            "Rollback the state of a Late Adult Phenotype Plan marked as finished to allow data entry.",
            LateAdultPhenotypePlanState.LateAdultPhenotypingFinished,
            LateAdultPhenotypePlanState.LateAdultPhenotypingDataReceived,
            false,
            "executed by the DCC when more data needs to be sent for a finished plan."),
    reverseAbortion(
            "Reinstate an arborted plan.",
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
            "Abort the plan when late adult phenotyping has been started",
            LateAdultPhenotypePlanState.LateAdultPhenotypingStarted,
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
    abortWhenLateAdultPhenotypingComplete(
            "Abort the plan when late adult phenotype data has been received by the CDA.",
            LateAdultPhenotypePlanState.LateAdultPhenotypingDataReceived,
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
    abortWhenLateAdultPhenotypingAllDataSent(
            "Abort the late adult phenotyping plan when all phenotype data has been sent to the DCC.",
            LateAdultPhenotypePlanState.LateAdultPhenotypingAllDataSent,
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
    abortWhenLateAdultPhenotypingFinished(
            "Abort the plan when late adult phenotyping is finished",
            LateAdultPhenotypePlanState.LateAdultPhenotypingFinished,
            LateAdultPhenotypePlanState.LateAdultPhenotypeProductionAborted,
            true,
            null)
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
