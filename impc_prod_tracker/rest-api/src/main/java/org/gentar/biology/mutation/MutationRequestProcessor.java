package org.gentar.biology.mutation;

import org.gentar.biology.mutation.mutation_deletion.MolecularMutationDeletion;
import org.gentar.biology.mutation.qc_results.MutationQcResult;
import org.gentar.biology.mutation.sequence.MutationSequence;
import org.gentar.biology.mutation.symbolConstructor.AlleleSymbolConstructor;
import org.gentar.biology.mutation.symbolConstructor.AlleleSymbolHandler;
import org.gentar.biology.mutation.symbolConstructor.SymbolSuggestionRequest;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanService;
import org.gentar.biology.plan.attempt.crispr.targeted_exon.TargetedExon;
import org.gentar.biology.sequence.Sequence;
import org.gentar.biology.sequence.category.SequenceCategoryService;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class MutationRequestProcessor {
    private final MutationUpdateMapper mutationUpdateMapper;
    private final MutationCreationMapper mutationCreationMapper;
    private final PlanService planService;
    private final AlleleSymbolHandler alleleSymbolHandler;
    private final SequenceCategoryService sequenceCategoryService;

    public MutationRequestProcessor(
            MutationUpdateMapper mutationUpdateMapper,
            MutationCreationMapper mutationCreationMapper,
            PlanService planService,
            AlleleSymbolHandler alleleSymbolHandler,
            SequenceCategoryService sequenceCategoryService) {
        this.mutationUpdateMapper = mutationUpdateMapper;
        this.mutationCreationMapper = mutationCreationMapper;
        this.planService = planService;
        this.alleleSymbolHandler = alleleSymbolHandler;
        this.sequenceCategoryService = sequenceCategoryService;
    }

    public Mutation getMutationToCreate(Outcome outcome, MutationCreationDTO mutationCreationDTO) {
        Mutation mutation = mutationCreationMapper.toEntity(mutationCreationDTO);
        setOutcomeCategoryType(mutation);
        Set<Outcome> outcomeSet = new HashSet<>();
        outcomeSet.add(outcome);
        mutation.setOutcomes(outcomeSet);
        return mutation;
    }

    private void setOutcomeCategoryType(Mutation mutation) {
        Set<MutationSequence> mutationSequences = mutation.getMutationSequences();
        if (mutationSequences != null) {
            mutationSequences.forEach(x -> {
                Sequence sequence = x.getSequence();
                if (sequence != null) {
                    sequence.setSequenceCategory(sequenceCategoryService.getOutcomeSequenceCategory());
                }
            });
        }
    }

    /**
     * Updates an mutation with the information than can be updated in a dto.
     *
     * @param originalMutation  The original mutation.
     * @param mutationUpdateDTO the dto with the new information.
     * @return the updated mutation.
     */
    public Mutation getMutationToUpdate(Mutation originalMutation, MutationUpdateDTO mutationUpdateDTO) {
        if (originalMutation == null || mutationUpdateDTO == null) {
            throw new UserOperationFailedException("Cannot update the mutation");
        }
        Mutation newMutation = new Mutation(originalMutation);

        Mutation mappedMutation = mutationUpdateMapper.toEntity(mutationUpdateDTO);
        newMutation.setSymbol(mappedMutation.getSymbol());
        newMutation.setMgiAlleleSymbolRequiresConstruction(
                mappedMutation.getMgiAlleleSymbolRequiresConstruction());
        newMutation.setGeneticMutationType(mappedMutation.getGeneticMutationType());
        newMutation.setMolecularMutationType(mappedMutation.getMolecularMutationType());

        newMutation.setDescription(mappedMutation.getDescription());
        newMutation.setQcNote(mappedMutation.getQcNote());
        newMutation.setCreatedAt(originalMutation.getCreatedAt());
        if (mutationUpdateDTO.getGeneUpdateDTOS() != null) {
            newMutation.setGenes(mappedMutation.getGenes());
        }
        newMutation.setMutationCategorizations(mappedMutation.getMutationCategorizations());
        setMutationQcResults(newMutation, mappedMutation);
        setMutationSequences(originalMutation, newMutation, mappedMutation);
        setMolecularMutationDeletions(originalMutation, newMutation, mappedMutation);
        setOutcomeCategoryType(newMutation);
        setTargetedExons(originalMutation, newMutation, mappedMutation);
        newMutation.setIsMutationDeletionChecked(mappedMutation.getIsMutationDeletionChecked());
        newMutation.setIsManualMutationDeletion(mappedMutation.getIsManualMutationDeletion());

        return newMutation;
    }

    private void setMutationQcResults(Mutation newMutation, Mutation mappedMutation) {
        Set<MutationQcResult> mappedMutationQcResults = mappedMutation.getMutationQcResults();
        newMutation.setMutationQcResults(mappedMutationQcResults);
        if (mappedMutationQcResults != null) {
            mappedMutationQcResults.forEach(x -> x.setMutation(newMutation));
        }
    }

    private void setMutationSequences(Mutation originalMutation, Mutation newMutation, Mutation mappedMutation) {
        Set<MutationSequence> mappedMutationSequences = mappedMutation.getMutationSequences();

        if (mappedMutationSequences != null) {
            // This is needed because the association with the mutation does not change, so this
            // allows to keep the reference that can be lost in the mapping.
            for (MutationSequence mutationSequence : mappedMutationSequences) {
                mutationSequence.setMutation(originalMutation);
                if (mutationSequence.getSequence().getSequenceLocations() != null &&
                        !mutationSequence.getSequence().getSequenceLocations().isEmpty()) {
                    throw new UserOperationFailedException(
                            "Mutation sequences do not accept locations.");
                }
            }
            newMutation.setMutationSequences(mappedMutationSequences);
        }
    }

    private void setMolecularMutationDeletions(Mutation originalMutation, Mutation newMutation, Mutation mappedMutation) {
        Set<MolecularMutationDeletion> mappedMolecularMutationDeletions = mappedMutation.getMolecularMutationDeletion();

        if (mappedMolecularMutationDeletions != null) {
            for (MolecularMutationDeletion molecularMutationDeletion : mappedMolecularMutationDeletions) {
                molecularMutationDeletion.setMutation(originalMutation);

            }
            newMutation.setMolecularMutationDeletion(mappedMolecularMutationDeletions);
        }
    }

    private void setTargetedExons(Mutation originalMutation, Mutation newMutation, Mutation mappedMutation) {
        Set<TargetedExon> mappedTargetedExons = mappedMutation.getTargetedExons();

        if (mappedTargetedExons != null) {
            for (TargetedExon targetedExon : mappedTargetedExons) {
                targetedExon.setMutation(originalMutation);

            }
            newMutation.setTargetedExons(mappedTargetedExons);
        }
    }


    /**
     * Creates a request to create a symbol suggestion for a mutation
     *
     * @param symbolSuggestionRequestDTO Information provided by the user
     * @param pin                        Plan identifier.
     * @return SymbolSuggestionRequest object with additional information.
     */
    public SymbolSuggestionRequest buildSymbolSuggestionRequest(
            SymbolSuggestionRequestDTO symbolSuggestionRequestDTO, String pin) {
        SymbolSuggestionRequest symbolSuggestionRequest = new SymbolSuggestionRequest();
        Plan plan = planService.getNotNullPlanByPin(pin);
        symbolSuggestionRequest.setIlarCode(plan.getWorkUnit().getIlarCode());
        symbolSuggestionRequest.setConsortiumAbbreviation(
                symbolSuggestionRequestDTO.getConsortiumAbbreviation());
        symbolSuggestionRequest.setExcludeConsortiumAbbreviation(
                symbolSuggestionRequestDTO.isExcludeConsortiumAbbreviation());
        return symbolSuggestionRequest;
    }

    public AlleleSymbolConstructor getAlleleSymbolConstructor(String pin) {
        return alleleSymbolHandler.getAlleleSymbolConstructor(planService.getNotNullPlanByPin(pin));
    }

    public Mutation getSimpleMappedMutation(MutationUpdateDTO mutationUpdateDTO) {
        return mutationUpdateMapper.toEntity(mutationUpdateDTO);
    }
}
