package org.gentar.biology.plan.engine.events;

import org.gentar.biology.plan.engine.state.PhenotypePlanState;
import org.gentar.biology.plan.engine.PlanProcessor;
import org.gentar.biology.plan.engine.processors.PhenotypePlanAbortProcessor;
import org.gentar.biology.plan.engine.processors.PhenotypePlanAbortReverserProcessor;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.gentar.statemachine.Processor;

import java.util.Arrays;
import java.util.List;

//    PhenotypingProductionRegistered("Phenotyping Production Registered"),
//    PhenotypeAttemptRegistered("Phenotype Attempt Registered"),
//    RederivationStarted("Rederivation Started"),
//            RederivationComplete("Rederivation Complete"),
//            PhenotypingStarted("Phenotyping Started"),
//            PhenotypingComplete("Phenotyping Complete"),
//            PhenotypeProductionAborted("Phenotype Production Aborted");


public enum PhenotypePlanEvent implements ProcessEvent
{
    abortWhenProductionRegistered(
            "Abort a registered phenotyping plan",
            PhenotypePlanState.PhenotypingProductionRegistered,
            PhenotypePlanState.PhenotypeProductionAborted,
            true,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return PhenotypePlanAbortProcessor.class;
                }
            },
    phenotypeAttemptRegistered(
            "A new phenotype attempt is created from a phenotyping plan",
            PhenotypePlanState.PhenotypingProductionRegistered,
            PhenotypePlanState.PhenotypeAttemptRegistered,
            true,
            "executed by the user when a phenotype attempt is created"),
    rederivationStarted(
            "Rederivation of the colony for phenotyping has started",
            PhenotypePlanState.PhenotypeAttemptRegistered,
            PhenotypePlanState.RederivationStarted,
            true,
            "executed by the user when rederivation is started."),
    rederivationComplete(
            "Rederivation of the colony for phenotyping is complete",
            PhenotypePlanState.RederivationStarted,
            PhenotypePlanState.RederivationComplete,
            true,
            "executed by the user when rederivation is complete."),
    phenotypingStarted(
            "Marked as started when the DCC recieves phenotype data",
            PhenotypePlanState.RederivationComplete,
            PhenotypePlanState.PhenotypingStarted,
            false,
            "executed by the DCC following completion of rederivation."),
    phenotypingStartedWithoutRederivation(
            "Marked as started when the DCC recieves phenotype data following a rederivation step",
            PhenotypePlanState.PhenotypeAttemptRegistered,
            PhenotypePlanState.PhenotypingStarted,
            false,
            "executed by the DCC when phenotyping is started."),
    phenotypingComplete(
            "Marked as complete when the CDA receives phenotype data",
            PhenotypePlanState.PhenotypingStarted,
            PhenotypePlanState.PhenotypingComplete,
            false,
            "executed by the CDA when phenotyping data received."),
    phenotypingFinished(
            "Marked as finished by the DCC when all phenotype data received",
            PhenotypePlanState.PhenotypingComplete,
            PhenotypePlanState.PhenotypingFinished,
            false,
            "executed by the DCC when all phenotype data received."),
    reverseAbortion(
            "Reverse abortion",
            PhenotypePlanState.PhenotypeProductionAborted,
            PhenotypePlanState.PhenotypingProductionRegistered,
            true,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return PhenotypePlanAbortReverserProcessor.class;
                }
            },
    abortWhenPhenotypeAttemptRegistered(
            "Abort a phenotyping plan when a phenotype attempt has been registered",
            PhenotypePlanState.PhenotypeAttemptRegistered,
            PhenotypePlanState.PhenotypeProductionAborted,
            true,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return PhenotypePlanAbortProcessor.class;
                }
            },
    abortWhenRederivationStarted(
            "Abort a phenotyping plan when rederivation is started",
            PhenotypePlanState.RederivationStarted,
            PhenotypePlanState.PhenotypeProductionAborted,
            true,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return PhenotypePlanAbortProcessor.class;
                }
            },
    abortWhenRederivationComplete(
            "Abort a phenotyping plan when rederivation is complete",
            PhenotypePlanState.RederivationComplete,
            PhenotypePlanState.PhenotypeProductionAborted,
            true,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return PhenotypePlanAbortProcessor.class;
                }
            },
    abortWhenPhenotypingStarted(
            "Abort a phenotyping plan when phenotyping is started",
            PhenotypePlanState.PhenotypingStarted,
            PhenotypePlanState.PhenotypeProductionAborted,
            false,
            "Can only be carried out by the DCC or CDA")
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return PhenotypePlanAbortProcessor.class;
                }
            },
    abortWhenPhenotypingComplete(
            "Abort a phenotyping plan when phenotyping is complete",
            PhenotypePlanState.PhenotypingComplete,
            PhenotypePlanState.PhenotypeProductionAborted,
            false,
            "Can only be carried out by the DCC or CDA")
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return PhenotypePlanAbortProcessor.class;
                }
            },
    abortWhenPhenotypingFinished(
            "Abort a phenotyping plan when phenotyping is finished",
            PhenotypePlanState.PhenotypingFinished,
            PhenotypePlanState.PhenotypeProductionAborted,
            false,
            "Can only be carried out by the DCC or CDA")
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
        return Arrays.asList(PhenotypePlanEvent.values());
    }
}
