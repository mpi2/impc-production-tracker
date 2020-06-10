package org.gentar.biology.mutation;

import org.gentar.Mapper;
import org.gentar.biology.mutation.categorizarion.MutationCategorizationService;
import org.springframework.stereotype.Component;
import org.gentar.biology.mutation.categorizarion.MutationCategorization;
import org.gentar.EntityMapper;

@Component
public class MutationCategorizationMapper implements Mapper<MutationCategorization, MutationCategorizationDTO>
{
    private EntityMapper entityMapper;
    private MutationCategorizationService mutationCategorizationService;

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
        return mutationCategorizationService.getMutationCategorizationByNameAndTypeFailingWhenNull(
            mutationCategorizationDTO.getName(),
            mutationCategorizationDTO.getMutationCategorizationTypeName());
    }
}
