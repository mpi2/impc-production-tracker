package org.gentar.biology.mutation;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.gene.GeneMapper;
import org.springframework.stereotype.Component;

@Component
public class MutationMapper implements Mapper<Mutation, MutationDTO>
{
    private EntityMapper entityMapper;
    private MutationQCResultMapper mutationQCResultMapper;
    private MutationCategorizationMapper mutationCategorizationMapper;
    private GeneMapper geneMapper;

    public MutationMapper(
        EntityMapper entityMapper,
        MutationQCResultMapper mutationQCResultMapper,
        MutationCategorizationMapper mutationCategorizationMapper,
        GeneMapper geneMapper)
    {
        this.entityMapper = entityMapper;
        this.mutationQCResultMapper = mutationQCResultMapper;
        this.mutationCategorizationMapper = mutationCategorizationMapper;
        this.geneMapper = geneMapper;
    }

    @Override
    public MutationDTO toDto(Mutation entity)
    {
        MutationDTO mutationDTO = entityMapper.toTarget(entity, MutationDTO.class);
        mutationDTO.setMutationQCResultDTOs(mutationQCResultMapper.toDtos(entity.getMutationQcResults()));
        mutationDTO.setGeneDTOS(geneMapper.toDtos(entity.getGenes()));
        mutationDTO.setMutationCategorizationDTOS(
            mutationCategorizationMapper.toDtos(entity.getMutationCategorizations()));
        return mutationDTO;
    }

    @Override
    public Mutation toEntity(MutationDTO dto)
    {
        Mutation mutation = new Mutation();
        return mutation;
    }
}
