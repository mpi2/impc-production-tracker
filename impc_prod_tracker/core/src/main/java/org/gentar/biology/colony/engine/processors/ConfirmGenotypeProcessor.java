package org.gentar.biology.colony.engine.processors;

import org.gentar.biology.colony.Colony;
import org.gentar.biology.colony.engine.ColonyStateSetter;
import org.gentar.biology.mutation.Mutation;
import org.gentar.biology.mutation.sequence.MutationSequence;
import org.gentar.biology.sequence.SequenceService;
import org.gentar.biology.sequence.category.SequenceCategoryName;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import java.util.Set;

@Component
public class ConfirmGenotypeProcessor extends AbstractProcessor
{
    private SequenceService sequenceService;

    public ConfirmGenotypeProcessor(
        ColonyStateSetter colonyStateSetter, SequenceService sequenceService)
    {
        super(colonyStateSetter);
        this.sequenceService = sequenceService;
    }

    @Override
    public TransitionEvaluation evaluateTransition(ProcessEvent transition, ProcessData data)
    {
        TransitionEvaluation transitionEvaluation = new TransitionEvaluation();
        transitionEvaluation.setTransition(transition);
        boolean canExecuteTransition = canExecuteTransition((Colony)data);
        transitionEvaluation.setExecutable(canExecuteTransition);
        if (!canExecuteTransition)
        {
            transitionEvaluation.setNote("A sequence and an allele symbol must exist.");
        }
        return transitionEvaluation;
    }

    private boolean canExecuteTransition(Colony colony)
    {
        return informationValidated(colony);
    }

    private boolean informationValidated(Colony colony)
    {
        boolean informationIsValidated = false;
        Set<Mutation> mutations = colony.getOutcome().getMutations();
        if (mutations != null)
        {
            for (Mutation mutation : mutations)
            {
                informationIsValidated = atLeastOneOutcomeSequenceExist(mutation)
                    && alleleSymbolExists(mutation);
                if (informationIsValidated)
                {
                    break;
                }
            }
        }
        return informationIsValidated;
    }

    private boolean atLeastOneOutcomeSequenceExist(Mutation mutation)
    {
        boolean atLeastOneOutcomeSequenceExist = false;
        Set<MutationSequence> mutationSequences = mutation.getMutationSequences();
        if (!CollectionUtils.isEmpty(mutationSequences))
        {
            atLeastOneOutcomeSequenceExist = mutationSequences.stream()
                .anyMatch(x -> sequenceService.sequenceHasCategory(
                    x.getSequence(), SequenceCategoryName.OUTCOME_SEQUENCE));
        }
        return atLeastOneOutcomeSequenceExist;
    }

    private boolean alleleSymbolExists(Mutation mutation)
    {
        return mutation.getSymbol() != null;
    }
}
