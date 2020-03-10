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
    private GeneMapper geneMapper;

    public MutationMapper(
        EntityMapper entityMapper, MutationQCResultMapper mutationQCResultMapper, GeneMapper geneMapper)
    {
        this.entityMapper = entityMapper;
        this.mutationQCResultMapper = mutationQCResultMapper;
        this.geneMapper = geneMapper;
    }

    @Override
    public MutationDTO toDto(Mutation entity)
    {
        MutationDTO mutationDTO = entityMapper.toTarget(entity, MutationDTO.class);
        mutationDTO.setMutationQCResultDTOs(mutationQCResultMapper.toDtos(entity.getMutationQcResults()));
        mutationDTO.setGeneDTOS(geneMapper.toDtos(entity.getGenes()));
        return mutationDTO;
    }

    @Override
    public Mutation toEntity(MutationDTO dto)
    {
        Mutation mutation = new Mutation();
        return mutation;
    }
}
