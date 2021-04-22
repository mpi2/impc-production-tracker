package org.gentar.biology.mutation;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.mutation.genetic_type.GeneticMutationType;
import org.gentar.biology.mutation.molecular_type.MolecularMutationType;
import org.gentar.biology.mutation.qc_results.MutationQcResult;
import org.gentar.biology.mutation.sequence.MutationSequence;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class MutationCommonMapper implements Mapper<Mutation, MutationCommonDTO>
{
    private final MutationQCResultMapper mutationQCResultMapper;
    private final MutationCategorizationMapper mutationCategorizationMapper;
    private final MutationSequenceMapper mutationSequenceMapper;
    private final GeneticMutationTypeMapper geneticMutationTypeMapper;
    private final MolecularMutationTypeMapper molecularMutationTypeMapper;

    public MutationCommonMapper(
        MutationQCResultMapper mutationQCResultMapper,
        MutationCategorizationMapper mutationCategorizationMapper,
        MutationSequenceMapper mutationSequenceMapper,
        GeneticMutationTypeMapper geneticMutationTypeMapper,
        MolecularMutationTypeMapper molecularMutationTypeMapper)
    {
        this.mutationQCResultMapper = mutationQCResultMapper;
        this.mutationCategorizationMapper = mutationCategorizationMapper;
        this.mutationSequenceMapper = mutationSequenceMapper;
        this.geneticMutationTypeMapper = geneticMutationTypeMapper;
        this.molecularMutationTypeMapper = molecularMutationTypeMapper;
    }

    @Override
    public MutationCommonDTO toDto(Mutation mutation)
    {
        MutationCommonDTO mutationCommonDTO = new MutationCommonDTO();
        mutationCommonDTO.setSymbol(mutation.getSymbol());
        mutationCommonDTO.setDescription(mutation.getDescription());
        mutationCommonDTO.setMgiAlleleSymbolRequiresConstruction(mutation.getMgiAlleleSymbolRequiresConstruction());
        mutationCommonDTO.setGeneticMutationTypeName(mutation.getGeneticMutationType().getName());
        if (mutation.getMolecularMutationType() != null) {
            mutationCommonDTO.setMolecularMutationTypeName(mutation.getMolecularMutationType().getName());
        }
        mutationCommonDTO.setAlleleConfirmed(mutation.getAlleleConfirmed());
        mutationCommonDTO.setMutationQCResultDTOs(
            mutationQCResultMapper.toDtos(mutation.getMutationQcResults()));
        mutationCommonDTO.setMutationSequenceDTOS(
            mutationSequenceMapper.toDtos(mutation.getMutationSequences()));
        mutationCommonDTO.setMutationCategorizationDTOS(
            mutationCategorizationMapper.toDtos(mutation.getMutationCategorizations()));
        return mutationCommonDTO;
    }

    @Override
    public Mutation toEntity(MutationCommonDTO mutationCommonDTO)
    {
        Mutation mutation = new Mutation();
        if (mutationCommonDTO != null)
        {
            mutation.setSymbol(mutationCommonDTO.getSymbol());
            mutation.setAlleleConfirmed(mutationCommonDTO.getAlleleConfirmed());
            mutation.setMgiAlleleSymbolRequiresConstruction(
                mutationCommonDTO.getMgiAlleleSymbolRequiresConstruction());
            mutation.setDescription(mutationCommonDTO.getDescription());
            setGeneticMutationType(mutation, mutationCommonDTO);
            setMolecularMutationType(mutation, mutationCommonDTO);
            setMutationQcResults(mutation, mutationCommonDTO);
            setMutationSequences(mutation, mutationCommonDTO);
            mutation.setMutationCategorizations(
                new HashSet<>(mutationCategorizationMapper.toEntities(
                    mutationCommonDTO.getMutationCategorizationDTOS())));
        }
        return mutation;
    }

    private void setMutationQcResults(Mutation mutation, MutationCommonDTO mutationCommonDTO)
    {
        List<MutationQCResultDTO> mutationQCResultDTOS = mutationCommonDTO.getMutationQCResultDTOs();
        if (mutationQCResultDTOS != null)
        {
            Set<MutationQcResult> mutationQcResults =
                new HashSet<>(mutationQCResultMapper.toEntities(mutationQCResultDTOS));
            mutationQcResults.forEach(x -> x.setMutation(mutation));
            mutation.setMutationQcResults(mutationQcResults);
        }
    }

    private void setGeneticMutationType(Mutation mutation, MutationCommonDTO mutationCommonDTO)
    {
        String geneticMutationTypeName =  mutationCommonDTO.getGeneticMutationTypeName();
        if (geneticMutationTypeName != null)
        {
            GeneticMutationType geneticMutationType =
                geneticMutationTypeMapper.toEntity(geneticMutationTypeName);
            mutation.setGeneticMutationType(geneticMutationType);
        }
    }

    private void setMolecularMutationType(Mutation mutation, MutationCommonDTO mutationCommonDTO)
    {
        String molecularMutationTypeName =  mutationCommonDTO.getMolecularMutationTypeName();
        if (molecularMutationTypeName != null)
        {
            MolecularMutationType molecularMutationType =
                molecularMutationTypeMapper.toEntity(molecularMutationTypeName);
            mutation.setMolecularMutationType(molecularMutationType);
        }
    }

    private void setMutationSequences(Mutation mutation, MutationCommonDTO mutationCommonDTO)
    {
        List<MutationSequenceDTO> mutationSequenceDTOS = mutationCommonDTO.getMutationSequenceDTOS();
        if (mutationSequenceDTOS != null)
        {
            Set<MutationSequence> mutationSequences =
                new HashSet<>(
                    mutationSequenceMapper.toEntities(mutationCommonDTO.getMutationSequenceDTOS()));
            mutationSequences.forEach(x -> x.setMutation(mutation));
            mutation.setMutationSequences(mutationSequences);
        }
    }
}
