package org.gentar.biology.colony.engine.processors;

import java.util.Set;
import org.gentar.biology.colony.Colony;
import org.gentar.biology.colony.engine.ColonyStateSetter;
import org.gentar.biology.mutation.Mutation;
import org.gentar.biology.mutation.sequence.MutationSequence;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.plan.attempt.AttemptTypesName;
import org.gentar.biology.sequence.SequenceService;
import org.gentar.biology.sequence.category.SequenceCategoryName;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class ConfirmGenotypeProcessor extends AbstractProcessor {
    private SequenceService sequenceService;

    public ConfirmGenotypeProcessor(
        ColonyStateSetter colonyStateSetter, SequenceService sequenceService) {
        super(colonyStateSetter);
        this.sequenceService = sequenceService;
    }

    @Override
    public TransitionEvaluation evaluateTransition(ProcessEvent transition, ProcessData data) {
        TransitionEvaluation transitionEvaluation = new TransitionEvaluation();
        transitionEvaluation.setTransition(transition);

        if (((Colony) data).getOutcome().getMutations() != null && ((Colony) data).getOutcome().getMutations().size() == 0) {
            transitionEvaluation
                .setNote("The colony must have at least one mutation to move to the Genotype Confirmed state.");
        } else {

            boolean canExecuteTransition = canExecuteTransition((Colony) data);
            transitionEvaluation.setExecutable(canExecuteTransition);
            if (!canExecuteTransition) {
                if (isEsCellAttempt(((Colony) data).getOutcome())) {
                    transitionEvaluation.setNote("All mutation symbols must exist.");
                } else {
                    transitionEvaluation.setNote("At least one sequence and All mutation symbols must exist.");
                }
            }
        }
        return transitionEvaluation;
    }

    private boolean canExecuteTransition(Colony colony) {
        return legacyInformationValidated(colony) &&
            attemptTypeInformationValidated(colony.getOutcome());
    }

    private boolean legacyInformationValidated(Colony colony) {

        if(colony.getOutcome().getMutations()==null){
            return false;
        }
        for (Mutation mutation : colony.getOutcome().getMutations()) {
            if (colony.getLegacyWithoutSequence() && !alleleSymbolExists(mutation)) {
                return false;
            }
        }
        return true;
    }

    private boolean attemptTypeInformationValidated(Outcome outcome) {
        Set<Mutation> mutations = outcome.getMutations();
        for (Mutation mutation : mutations) {
            if (isEsCellAttempt(outcome)) {
                if (!alleleSymbolExists(mutation)) {
                    return false;
                }
            } else if (!atLeastOneOutcomeSequenceExist(mutation)
                || !alleleSymbolExists(mutation)) {

                return false;
            }
        }
        return true;
    }

    private String getAttemptType(Outcome outcome) {
        return outcome.getPlan().getAttemptType().getName();
    }

    private boolean atLeastOneOutcomeSequenceExist(Mutation mutation) {
        boolean atLeastOneOutcomeSequenceExist = false;
        Set<MutationSequence> mutationSequences = mutation.getMutationSequences();
        if (!CollectionUtils.isEmpty(mutationSequences)) {
            atLeastOneOutcomeSequenceExist = mutationSequences.stream()
                .anyMatch(x -> sequenceService.sequenceHasCategory(
                    x.getSequence(), SequenceCategoryName.OUTCOME_SEQUENCE));
        }
        return atLeastOneOutcomeSequenceExist;
    }

    private boolean alleleSymbolExists(Mutation mutation) {
        return mutation.getSymbol() != null && !mutation.getSymbol().equals("");
    }

    private boolean isEsCellAttempt(Outcome outcome) {
        if(outcome.getPlan()==null){
            return false;
        }
        return getAttemptType(outcome).equals(AttemptTypesName.ES_CELL.getLabel()) ||
            getAttemptType(outcome)
                .equals(AttemptTypesName.ES_CELL_ALLELE_MODIFICATION.getLabel());
    }
}
