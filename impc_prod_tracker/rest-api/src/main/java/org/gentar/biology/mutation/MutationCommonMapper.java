package org.gentar.biology.mutation;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.gene.GeneMapper;
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

    public MutationCommonMapper(
        EntityMapper entityMapper,
        MutationQCResultMapper mutationQCResultMapper,
        MutationCategorizationMapper mutationCategorizationMapper,
        GeneMapper geneMapper,
        MutationSequenceMapper mutationSequenceMapper)
    {
        this.entityMapper = entityMapper;
        this.mutationQCResultMapper = mutationQCResultMapper;
        this.mutationCategorizationMapper = mutationCategorizationMapper;
        this.geneMapper = geneMapper;
        this.mutationSequenceMapper = mutationSequenceMapper;
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
        Mutation mutation = entityMapper.toTarget(mutationCommonDTO, Mutation.class);
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
}
