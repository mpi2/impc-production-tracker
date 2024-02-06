package org.gentar.biology.mutation;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.mutation.categorizarion.MutationCategorization;
import org.gentar.biology.mutation.categorizarion.MutationCategorizationService;
import org.springframework.stereotype.Component;

@Component
public class MutationCategorizationMapper implements Mapper<MutationCategorization, MutationCategorizationDTO>
{
    private final EntityMapper entityMapper;
    private final MutationCategorizationService mutationCategorizationService;

    public MutationCategorizationMapper(
        EntityMapper entityMapper, MutationCategorizationService mutationCategorizationService)
    {
        this.entityMapper = entityMapper;
        this.mutationCategorizationService = mutationCategorizationService;
    }

    public MutationCategorizationDTO toDto(MutationCategorization mutationCategorization)
    {
        return entityMapper.toTarget(mutationCategorization, MutationCategorizationDTO.class);
    }

    public MutationCategorization toEntity(MutationCategorizationDTO mutationCategorizationDTO)
    {
        if (mutationCategorizationDTO.getMutationCategorizationTypeName() == null) {
            return mutationCategorizationService.getMutationCategorizationByNameFailingWhenNull(
                    mutationCategorizationDTO.getName());
        } else {
            return mutationCategorizationService.getMutationCategorizationByNameAndTypeFailingWhenNull(
                    mutationCategorizationDTO.getName(),
                    mutationCategorizationDTO.getMutationCategorizationTypeName());
        }
    }
}
