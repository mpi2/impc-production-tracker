package org.gentar.biology.mutation;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.springframework.stereotype.Component;

@Component
public class MutationMapper implements Mapper<Mutation, MutationDTO>
{
    private EntityMapper entityMapper;
    private MutationQCResultMapper mutationQCResultMapper;

    public MutationMapper(EntityMapper entityMapper, MutationQCResultMapper mutationQCResultMapper)
    {
        this.entityMapper = entityMapper;
        this.mutationQCResultMapper = mutationQCResultMapper;
    }

    @Override
    public MutationDTO toDto(Mutation entity)
    {
        MutationDTO mutationDTO = entityMapper.toTarget(entity, MutationDTO.class);
        mutationDTO.setMutationQCResultDTOs(mutationQCResultMapper.toDtos(entity.getMutationQcResults()));
        return mutationDTO;
    }

    @Override
    public Mutation toEntity(MutationDTO dto)
    {
        Mutation mutation = new Mutation();
        return mutation;
    }
}
