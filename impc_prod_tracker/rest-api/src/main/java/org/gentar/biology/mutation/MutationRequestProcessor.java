package org.gentar.biology.mutation;

import org.gentar.biology.mutation.qc_results.MutationQcResult;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;
import java.util.Set;

@Component
public class MutationRequestProcessor
{
    private MutationUpdateMapper mutationUpdateMapper;

    public MutationRequestProcessor(MutationUpdateMapper mutationUpdateMapper)
    {
        this.mutationUpdateMapper = mutationUpdateMapper;
    }

    /**
     * Updates an mutation with the information than can be updated in a dto.
     * @param originalMutation The original mutation.
     * @param mutationUpdateDTO the dto with the new information.
     * @return the updated mutation.
     */
    public Mutation getMutationToUpdate(Mutation originalMutation, MutationUpdateDTO mutationUpdateDTO)
    {
        if (originalMutation == null || mutationUpdateDTO == null)
        {
            throw new UserOperationFailedException("Cannot update the mutation");
        }
        Mutation newMutation = new Mutation(originalMutation);
        Mutation mappedMutation = mutationUpdateMapper.toEntity(mutationUpdateDTO);
        newMutation.setMgiAlleleSymbolRequiresConstruction(
            mappedMutation.getMgiAlleleSymbolRequiresConstruction());
        newMutation.setMgiAlleleSymbolRequiresConstruction(
            mappedMutation.getMgiAlleleSymbolRequiresConstruction());
        newMutation.setGeneticMutationType(mappedMutation.getGeneticMutationType());
        newMutation.setMolecularMutationType(mappedMutation.getMolecularMutationType());
        newMutation.setAlleleConfirmed(mappedMutation.getAlleleConfirmed());
        newMutation.setGenes(mappedMutation.getGenes());
        newMutation.setMutationSequences(mappedMutation.getMutationSequences());
        newMutation.setMutationCategorizations(mappedMutation.getMutationCategorizations());
        setMutationQcResults(newMutation, mappedMutation);
        return newMutation;
    }

    private void setMutationQcResults(Mutation newMutation, Mutation mappedMutation)
    {
        Set<MutationQcResult> mappedMutationQcResults = mappedMutation.getMutationQcResults();
        newMutation.setMutationQcResults(mappedMutationQcResults);
        if (mappedMutationQcResults != null)
        {
            mappedMutationQcResults.forEach(x -> x.setMutation(newMutation));
        }
    }
}
