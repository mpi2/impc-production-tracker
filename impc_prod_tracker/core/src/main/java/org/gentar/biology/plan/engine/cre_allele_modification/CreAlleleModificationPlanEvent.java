package org.gentar.biology.plan.engine.cre_allele_modification;

import org.gentar.biology.plan.engine.cre_allele_modification.processors.*;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.gentar.statemachine.Processor;
import org.gentar.statemachine.StateMachineConstants;

import java.util.Arrays;
import java.util.List;

public enum CreAlleleModificationPlanEvent implements ProcessEvent 
{
    updateToMouseAlleleModificationRegistered(
            "Update to mouse allele modification registered.",
            CreAlleleModificationPlanState.PlanCreated,
            CreAlleleModificationPlanState.MouseAlleleModificationRegistered,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return CreAlleleModificationPlanRegisteredProcessor.class;
                }
            },
    updateToCreExcisionStarted(
            "Update to Cre Excision Started.",
            CreAlleleModificationPlanState.MouseAlleleModificationRegistered,
            CreAlleleModificationPlanState.CreExcisionStarted,
            StateMachineConstants.NOT_TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return CreAlleleModificationPlanCreExcisionStartedProcessor.class;
                }
            },
    updateToCreExcisionComplete(
            "Update to Cre Excision Complete.",
            CreAlleleModificationPlanState.CreExcisionStarted,
            CreAlleleModificationPlanState.CreExcisionComplete,
            StateMachineConstants.NOT_TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return CreAlleleModificationPlanCreExcisionCompleteProcessor.class;
                }
            },
    updateToMouseAlleleModificationGenotypeConfirmed(
            "Update to Mouse Allele Modification Genotype Confirmed.",
            CreAlleleModificationPlanState.CreExcisionComplete,
            CreAlleleModificationPlanState.MouseAlleleModificationGenotypeConfirmed,
            StateMachineConstants.NOT_TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return CreAlleleModificationPlanGenotypeConfirmedProcessor.class;
                }
            },
    revertToCreExcisionComplete(
            "Update to Mouse Allele Modification Genotype Confirmed.",
            CreAlleleModificationPlanState.MouseAlleleModificationGenotypeConfirmed,
            CreAlleleModificationPlanState.CreExcisionComplete,
            StateMachineConstants.NOT_TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return CreAlleleModificationPlanRevertToCreExcisionCompleteProcessor.class;
                }
            },
    updateToRederivationStarted(
            "Started rederivation of the colony for Cre allele modification",
            CreAlleleModificationPlanState.MouseAlleleModificationRegistered,
            CreAlleleModificationPlanState.RederivationForModificationStarted,
            StateMachineConstants.TRIGGERED_BY_USER,
            "executed by the user when rederivation is started.")
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return null;
                }
            },
    updateToRederivationComplete(
            "Completed rederivation of the colony for Cre allele modification",
            CreAlleleModificationPlanState.RederivationForModificationStarted,
            CreAlleleModificationPlanState.RederivationForModificationComplete,
            StateMachineConstants.TRIGGERED_BY_USER,
            "executed by the user when rederivation is complete.")
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return null;
                }
            },
    updateToCreExcisionStartedAfterRederivation(
            "Marked as Cre Excision Started following a rederivation step",
            CreAlleleModificationPlanState.RederivationForModificationComplete,
            CreAlleleModificationPlanState.CreExcisionStarted,
            StateMachineConstants.NOT_TRIGGERED_BY_USER,
            "executed following completion of rederivation.")
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return CreAlleleModificationPlanCreExcisionStartedProcessor.class;
                }
            },
    abandonWhenCreated(
            "Abandon a Cre allele modification plan that has been created",
            CreAlleleModificationPlanState.PlanCreated,
            CreAlleleModificationPlanState.PlanAbandoned,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return CreAlleleModificationPlanAbortProcessor.class;
                }
            },
    abortWhenMouseAlleleModificationRegistered(
            "Abort a Cre allele modification plan that has been registered.",
            CreAlleleModificationPlanState.MouseAlleleModificationRegistered,
            CreAlleleModificationPlanState.MouseAlleleModificationAborted,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return CreAlleleModificationPlanAbortProcessor.class;
                }
            },
    abortWhenRederivationForModificationStarted(
            "Abort a Cre allele modification plan when rederivation has started.",
            CreAlleleModificationPlanState.RederivationForModificationStarted,
            CreAlleleModificationPlanState.MouseAlleleModificationAborted,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return CreAlleleModificationPlanAbortProcessor.class;
                }
            },
    abortWhenRederivationForModificationComplete(
            "Abort a Cre allele modification plan when rederivation is complete.",
            CreAlleleModificationPlanState.RederivationForModificationComplete,
            CreAlleleModificationPlanState.MouseAlleleModificationAborted,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return CreAlleleModificationPlanAbortProcessor.class;
                }
            },
    abortWhenCreExcisionStarted(
            "Abort an allele modification plan when Cre excision has been started.",
            CreAlleleModificationPlanState.CreExcisionStarted,
            CreAlleleModificationPlanState.MouseAlleleModificationAborted,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return CreAlleleModificationPlanAbortProcessor.class;
                }
            },
    abortWhenCreExcisionComplete(
            "Abort an allele modification plan when Cre excision is complete",
            CreAlleleModificationPlanState.CreExcisionComplete,
            CreAlleleModificationPlanState.MouseAlleleModificationAborted,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return CreAlleleModificationPlanAbortProcessor.class;
                }
            },
    abortWhenMouseAlleleModificationGenotypeConfirmed(
            "Abort an allele modification plan when a genotype confirmed colony exists.",
            CreAlleleModificationPlanState.MouseAlleleModificationGenotypeConfirmed,
            CreAlleleModificationPlanState.MouseAlleleModificationAborted,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return CreAlleleModificationPlanAbortProcessor.class;
                }
            };

    CreAlleleModificationPlanEvent(
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
        return Arrays.asList(CreAlleleModificationPlanEvent.values());
    }
}
