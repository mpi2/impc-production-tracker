package org.gentar.biology.plan.engine.crispr_allele_modification;

import java.util.Arrays;
import java.util.List;
import org.gentar.biology.plan.engine.crispr_allele_modification.processors.CrisprAlleleModificationPlanAbortProcessor;
import org.gentar.biology.plan.engine.crispr_allele_modification.processors.CrisprAlleleModificationPlanCreExcisionCompleteProcessor;
import org.gentar.biology.plan.engine.crispr_allele_modification.processors.CrisprAlleleModificationPlanCreExcisionStartedProcessor;
import org.gentar.biology.plan.engine.crispr_allele_modification.processors.CrisprAlleleModificationPlanGenotypeConfirmedProcessor;
import org.gentar.biology.plan.engine.crispr_allele_modification.processors.CrisprAlleleModificationPlanRegisteredProcessor;
import org.gentar.biology.plan.engine.crispr_allele_modification.processors.CrisprAlleleModificationPlanRevertToCreExcisionCompleteProcessor;
import org.gentar.biology.plan.engine.processors.PlanProcessorWithoutValidations;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.gentar.statemachine.Processor;
import org.gentar.statemachine.StateMachineConstants;

public enum CrisprAlleleModificationPlanEvent implements ProcessEvent
{
    updateToMouseAlleleModificationRegistered(
            "Update to mouse allele modification registered.",
            CrisprAlleleModificationPlanState.PlanCreated,
            CrisprAlleleModificationPlanState.MouseAlleleModificationRegistered,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return CrisprAlleleModificationPlanRegisteredProcessor.class;
                }
            },
    updateToCreExcisionStarted(
            "Update to Cre Excision Started.",
            CrisprAlleleModificationPlanState.MouseAlleleModificationRegistered,
            CrisprAlleleModificationPlanState.CreExcisionStarted,
            StateMachineConstants.NOT_TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return CrisprAlleleModificationPlanCreExcisionStartedProcessor.class;
                }
            },
    updateToCreExcisionComplete(
            "Update to Cre Excision Complete.",
            CrisprAlleleModificationPlanState.CreExcisionStarted,
            CrisprAlleleModificationPlanState.CreExcisionComplete,
            StateMachineConstants.NOT_TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return CrisprAlleleModificationPlanCreExcisionCompleteProcessor.class;
                }
            },
    updateToMouseAlleleModificationGenotypeConfirmed(
            "Update to Mouse Allele Modification Genotype Confirmed.",
            CrisprAlleleModificationPlanState.CreExcisionComplete,
            CrisprAlleleModificationPlanState.MouseAlleleModificationGenotypeConfirmed,
            StateMachineConstants.NOT_TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return CrisprAlleleModificationPlanGenotypeConfirmedProcessor.class;
                }
            },
    revertToCreExcisionComplete(
            "Update to Mouse Allele Modification Genotype Confirmed.",
            CrisprAlleleModificationPlanState.MouseAlleleModificationGenotypeConfirmed,
            CrisprAlleleModificationPlanState.CreExcisionComplete,
            StateMachineConstants.NOT_TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return CrisprAlleleModificationPlanRevertToCreExcisionCompleteProcessor.class;
                }
            },
    updateToRederivationStarted(
            "Started rederivation of the colony for ES Cekk allele modification",
            CrisprAlleleModificationPlanState.MouseAlleleModificationRegistered,
            CrisprAlleleModificationPlanState.RederivationForModificationStarted,
            StateMachineConstants.TRIGGERED_BY_USER,
            "executed by the user when rederivation is started.")
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return PlanProcessorWithoutValidations.class;
                }
            },
    updateToRederivationComplete(
            "Completed rederivation of the colony for Crispr allele modification",
            CrisprAlleleModificationPlanState.RederivationForModificationStarted,
            CrisprAlleleModificationPlanState.RederivationForModificationComplete,
            StateMachineConstants.TRIGGERED_BY_USER,
            "executed by the user when rederivation is complete.")
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return PlanProcessorWithoutValidations.class;
                }
            },
    updateToCreExcisionStartedAfterRederivation(
            "Marked as Cre Excision Started following a rederivation step",
            CrisprAlleleModificationPlanState.RederivationForModificationComplete,
            CrisprAlleleModificationPlanState.CreExcisionStarted,
            StateMachineConstants.NOT_TRIGGERED_BY_USER,
            "executed following completion of rederivation.")
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return CrisprAlleleModificationPlanCreExcisionStartedProcessor.class;
                }
            },
    abandonWhenCreated(
            "Abandon a Crispr allele modification plan that has been created",
            CrisprAlleleModificationPlanState.PlanCreated,
            CrisprAlleleModificationPlanState.PlanAbandoned,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return CrisprAlleleModificationPlanAbortProcessor.class;
                }
            },
    abortWhenMouseAlleleModificationRegistered(
            "Abort a Crispr allele modification plan that has been registered.",
            CrisprAlleleModificationPlanState.MouseAlleleModificationRegistered,
            CrisprAlleleModificationPlanState.MouseAlleleModificationAborted,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return CrisprAlleleModificationPlanAbortProcessor.class;
                }
            },
    abortWhenRederivationForModificationStarted(
            "Abort a Crispr allele modification plan when rederivation has started.",
            CrisprAlleleModificationPlanState.RederivationForModificationStarted,
            CrisprAlleleModificationPlanState.MouseAlleleModificationAborted,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return CrisprAlleleModificationPlanAbortProcessor.class;
                }
            },
    abortWhenRederivationForModificationComplete(
            "Abort a Crispr allele modification plan when rederivation is complete.",
            CrisprAlleleModificationPlanState.RederivationForModificationComplete,
            CrisprAlleleModificationPlanState.MouseAlleleModificationAborted,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return CrisprAlleleModificationPlanAbortProcessor.class;
                }
            },
    abortWhenCreExcisionStarted(
            "Abort an allele modification plan when Cre excision has been started.",
            CrisprAlleleModificationPlanState.CreExcisionStarted,
            CrisprAlleleModificationPlanState.MouseAlleleModificationAborted,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return CrisprAlleleModificationPlanAbortProcessor.class;
                }
            },
    abortWhenCreExcisionComplete(
            "Abort an allele modification plan when Cre excision is complete",
            CrisprAlleleModificationPlanState.CreExcisionComplete,
            CrisprAlleleModificationPlanState.MouseAlleleModificationAborted,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return CrisprAlleleModificationPlanAbortProcessor.class;
                }
            },
    abortWhenMouseAlleleModificationGenotypeConfirmed(
            "Abort an allele modification plan when a genotype confirmed colony exists.",
            CrisprAlleleModificationPlanState.MouseAlleleModificationGenotypeConfirmed,
            CrisprAlleleModificationPlanState.MouseAlleleModificationAborted,
            StateMachineConstants.TRIGGERED_BY_USER,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return CrisprAlleleModificationPlanAbortProcessor.class;
                }
            };

    CrisprAlleleModificationPlanEvent(
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
        return Arrays.asList(CrisprAlleleModificationPlanEvent.values());
    }
}
