package org.gentar.biology.mutation;

import org.gentar.Mapper;
import org.springframework.stereotype.Component;

@Component
public class MutationUpdateMapper implements Mapper<Mutation, MutationUpdateDTO>
{
    private MutationCommonMapper mutationCommonMapper;

    public MutationUpdateMapper(MutationCommonMapper mutationCommonMapper)
    {
        this.mutationCommonMapper = mutationCommonMapper;
    }

    @Override
    public MutationUpdateDTO toDto(Mutation mutation)
    {
        return null;
    }

    @Override
    public Mutation toEntity(MutationUpdateDTO mutationUpdateDTO)
    {
        Mutation mutation = new Mutation();
        if (mutationUpdateDTO.getMutationCommonDTO() != null)
        {
            mutation = mutationCommonMapper.toEntity(mutationUpdateDTO.getMutationCommonDTO());
        }
        return mutation;
    }
}
