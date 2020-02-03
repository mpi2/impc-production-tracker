package org.gentar.biology.mutation;

import org.gentar.Mapper;
import org.springframework.stereotype.Component;

@Component
public class MutationMapper implements Mapper<Mutation, MutationDTO>
{
    @Override
    public MutationDTO toDto(Mutation entity)
    {
        MutationDTO mutationDTO = new MutationDTO();
        mutationDTO.setId(entity.getId());
        return mutationDTO;
    }

    @Override
    public Mutation toEntity(MutationDTO dto)
    {
        Mutation mutation = new Mutation();
        return mutation;
    }
}
