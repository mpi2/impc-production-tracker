package org.gentar.biology.plan.engine.es_cell_allele_modification;

import org.gentar.biology.plan.engine.es_cell_allele_modification.processors.*;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.gentar.statemachine.Processor;
import org.gentar.statemachine.StateMachineConstants;

import java.util.Arrays;
import java.util.List;

public enum EsCellAlleleModificationPlanEvent implements ProcessEvent
{
    updateToMouseAlleleModificationRegistered(
            "Update to mouse allele modification registered.",
            EsCellAlleleModificationPlanState.PlanCreated,
            EsCellAlleleModificationPlanState.MouseAlleleModificationRegistered,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return EsCellAlleleModificationPlanRegisteredProcessor.class;
                }
            },
    updateToCreExcisionStarted(
            "Update to Cre Excision Started.",
            EsCellAlleleModificationPlanState.MouseAlleleModificationRegistered,
            EsCellAlleleModificationPlanState.CreExcisionStarted,
            StateMachineConstants.NOT_TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return EsCellAlleleModificationPlanCreExcisionStartedProcessor.class;
                }
            },
    updateToCreExcisionComplete(
            "Update to Cre Excision Complete.",
            EsCellAlleleModificationPlanState.CreExcisionStarted,
            EsCellAlleleModificationPlanState.CreExcisionComplete,
            StateMachineConstants.NOT_TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return EsCellAlleleModificationPlanCreExcisionCompleteProcessor.class;
                }
            },
    updateToMouseAlleleModificationGenotypeConfirmed(
            "Update to Mouse Allele Modification Genotype Confirmed.",
            EsCellAlleleModificationPlanState.CreExcisionComplete,
            EsCellAlleleModificationPlanState.MouseAlleleModificationGenotypeConfirmed,
            StateMachineConstants.NOT_TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return EsCellAlleleModificationPlanGenotypeConfirmedProcessor.class;
                }
            },
    revertToCreExcisionComplete(
            "Update to Mouse Allele Modification Genotype Confirmed.",
            EsCellAlleleModificationPlanState.MouseAlleleModificationGenotypeConfirmed,
            EsCellAlleleModificationPlanState.CreExcisionComplete,
            StateMachineConstants.NOT_TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return EsCellAlleleModificationPlanRevertToCreExcisionCompleteProcessor.class;
                }
            },
    updateToRederivationStarted(
            "Started rederivation of the colony for ES Cekk allele modification",
            EsCellAlleleModificationPlanState.MouseAlleleModificationRegistered,
            EsCellAlleleModificationPlanState.RederivationForModificationStarted,
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
            "Completed rederivation of the colony for Es Cell allele modification",
            EsCellAlleleModificationPlanState.RederivationForModificationStarted,
            EsCellAlleleModificationPlanState.RederivationForModificationComplete,
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
            EsCellAlleleModificationPlanState.RederivationForModificationComplete,
            EsCellAlleleModificationPlanState.CreExcisionStarted,
            StateMachineConstants.NOT_TRIGGERED_BY_USER,
            "executed following completion of rederivation.")
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return EsCellAlleleModificationPlanCreExcisionStartedProcessor.class;
                }
            },
    abandonWhenCreated(
            "Abandon a Es Cell allele modification plan that has been created",
            EsCellAlleleModificationPlanState.PlanCreated,
            EsCellAlleleModificationPlanState.PlanAbandoned,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return EsCellAlleleModificationPlanAbortProcessor.class;
                }
            },
    abortWhenMouseAlleleModificationRegistered(
            "Abort a ES Cell allele modification plan that has been registered.",
            EsCellAlleleModificationPlanState.MouseAlleleModificationRegistered,
            EsCellAlleleModificationPlanState.MouseAlleleModificationAborted,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return EsCellAlleleModificationPlanAbortProcessor.class;
                }
            },
    abortWhenRederivationForModificationStarted(
            "Abort a ES Cell allele modification plan when rederivation has started.",
            EsCellAlleleModificationPlanState.RederivationForModificationStarted,
            EsCellAlleleModificationPlanState.MouseAlleleModificationAborted,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return EsCellAlleleModificationPlanAbortProcessor.class;
                }
            },
    abortWhenRederivationForModificationComplete(
            "Abort a ES Cell allele modification plan when rederivation is complete.",
            EsCellAlleleModificationPlanState.RederivationForModificationComplete,
            EsCellAlleleModificationPlanState.MouseAlleleModificationAborted,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return EsCellAlleleModificationPlanAbortProcessor.class;
                }
            },
    abortWhenCreExcisionStarted(
            "Abort an allele modification plan when Cre excision has been started.",
            EsCellAlleleModificationPlanState.CreExcisionStarted,
            EsCellAlleleModificationPlanState.MouseAlleleModificationAborted,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return EsCellAlleleModificationPlanAbortProcessor.class;
                }
            },
    abortWhenCreExcisionComplete(
            "Abort an allele modification plan when Cre excision is complete",
            EsCellAlleleModificationPlanState.CreExcisionComplete,
            EsCellAlleleModificationPlanState.MouseAlleleModificationAborted,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return EsCellAlleleModificationPlanAbortProcessor.class;
                }
            },
    abortWhenMouseAlleleModificationGenotypeConfirmed(
            "Abort an allele modification plan when a genotype confirmed colony exists.",
            EsCellAlleleModificationPlanState.MouseAlleleModificationGenotypeConfirmed,
            EsCellAlleleModificationPlanState.MouseAlleleModificationAborted,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return EsCellAlleleModificationPlanAbortProcessor.class;
                }
            };

    EsCellAlleleModificationPlanEvent(
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
        return Arrays.asList(EsCellAlleleModificationPlanEvent.values());
    }
}
