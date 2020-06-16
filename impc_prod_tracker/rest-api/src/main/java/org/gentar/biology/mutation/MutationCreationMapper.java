package org.gentar.biology.mutation;

import org.gentar.Mapper;
import org.springframework.stereotype.Component;

@Component
public class MutationCreationMapper implements Mapper<Mutation, MutationCreationDTO>
{
    private MutationCommonMapper mutationCommonMapper;

    public MutationCreationMapper(MutationCommonMapper mutationCommonMapper)
    {
        this.mutationCommonMapper = mutationCommonMapper;
    }

    @Override
    public MutationCreationDTO toDto(Mutation mutation)
    {
        return null;
    }

    @Override
    public Mutation toEntity(MutationCreationDTO mutationCreationDTO)
    {
        Mutation mutation = new Mutation();
        if (mutationCreationDTO.getMutationCommonDTO() != null)
        {
            mutation = mutationCommonMapper.toEntity(mutationCreationDTO.getMutationCommonDTO());
        }
        return mutation;
    }
}
