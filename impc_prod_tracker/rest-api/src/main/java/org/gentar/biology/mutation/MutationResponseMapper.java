package org.gentar.biology.mutation;

import org.gentar.Mapper;
import org.springframework.stereotype.Component;

@Component
public class MutationResponseMapper implements Mapper<Mutation, MutationResponseDTO>
{
    private MutationCommonMapper mutationCommonMapper;

    public MutationResponseMapper(MutationCommonMapper mutationCommonMapper)
    {
        this.mutationCommonMapper = mutationCommonMapper;
    }

    @Override
    public MutationResponseDTO toDto(Mutation mutation)
    {
        MutationResponseDTO mutationResponseDTO = new MutationResponseDTO();
        MutationCommonDTO mutationCommonDTO = mutationCommonMapper.toDto(mutation);
        mutationResponseDTO.setMutationCommonDTO(mutationCommonDTO);
        mutationResponseDTO.setId(mutation.getId());
        mutationResponseDTO.setMgiAlleleId(mutation.getMgiAlleleId());
        mutationResponseDTO.setMgiAlleleSymbol(mutation.getMgiAlleleSymbol());
        mutationResponseDTO.setMgiAlleleSymbolWithoutImpcAbbreviation(
            mutation.getMgiAlleleSymbolWithoutImpcAbbreviation());
        mutationResponseDTO.setDescription(mutation.getDescription());
        mutationResponseDTO.setAutoDescription(mutation.getAutoDescription());
        return mutationResponseDTO;
    }

    @Override
    public Mutation toEntity(MutationResponseDTO mutationResponseDTO)
    {
        // We don't have to make an entity from a response.
        return null;
    }
}
