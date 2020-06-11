package org.gentar.biology.mutation;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.gene.GeneMapper;
import org.gentar.biology.mutation.genetic_type.GeneticMutationType;
import org.gentar.biology.mutation.molecular_type.MolecularMutationType;
import org.springframework.stereotype.Component;
import java.util.HashSet;

@Component
public class MutationCommonMapper implements Mapper<Mutation, MutationCommonDTO>
{
    private EntityMapper entityMapper;
    private MutationQCResultMapper mutationQCResultMapper;
    private MutationCategorizationMapper mutationCategorizationMapper;
    private GeneMapper geneMapper;
    private MutationSequenceMapper mutationSequenceMapper;
    private GeneticMutationTypeMapper geneticMutationTypeMapper;
    private MolecularMutationTypeMapper molecularMutationTypeMapper;

    public MutationCommonMapper(
        EntityMapper entityMapper,
        MutationQCResultMapper mutationQCResultMapper,
        MutationCategorizationMapper mutationCategorizationMapper,
        GeneMapper geneMapper,
        MutationSequenceMapper mutationSequenceMapper,
        GeneticMutationTypeMapper geneticMutationTypeMapper,
        MolecularMutationTypeMapper molecularMutationTypeMapper)
    {
        this.entityMapper = entityMapper;
        this.mutationQCResultMapper = mutationQCResultMapper;
        this.mutationCategorizationMapper = mutationCategorizationMapper;
        this.geneMapper = geneMapper;
        this.mutationSequenceMapper = mutationSequenceMapper;
        this.geneticMutationTypeMapper = geneticMutationTypeMapper;
        this.molecularMutationTypeMapper = molecularMutationTypeMapper;
    }

    @Override
    public MutationCommonDTO toDto(Mutation mutation)
    {
        MutationCommonDTO mutationCommonDTO = entityMapper.toTarget(mutation, MutationCommonDTO.class);
        mutationCommonDTO.setMutationQCResultDTOs(
            mutationQCResultMapper.toDtos(mutation.getMutationQcResults()));
        mutationCommonDTO.setGeneDTOS(geneMapper.toDtos(mutation.getGenes()));
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
        mutation.setAlleleConfirmed(mutationCommonDTO.getAlleleConfirmed());
        mutation.setMgiAlleleSymbolRequiresConstruction(
            mutationCommonDTO.getMgiAlleleSymbolRequiresConstruction());
        setGeneticMutationType(mutation, mutationCommonDTO);
        setMolecularMutationType(mutation, mutationCommonDTO);
        mutation.setMutationQcResults(
            new HashSet<>(mutationQCResultMapper.toEntities(mutationCommonDTO.getMutationQCResultDTOs())));
        mutation.setGenes(new HashSet<>(geneMapper.toEntities(mutationCommonDTO.getGeneDTOS())));
        mutation.setMutationSequences(
            new HashSet<>(mutationSequenceMapper.toEntities(mutationCommonDTO.getMutationSequenceDTOS())));
        mutation.setMutationCategorizations(
            new HashSet<>(mutationCategorizationMapper.toEntities(
                mutationCommonDTO.getMutationCategorizationDTOS())));
        return mutation;
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
}
