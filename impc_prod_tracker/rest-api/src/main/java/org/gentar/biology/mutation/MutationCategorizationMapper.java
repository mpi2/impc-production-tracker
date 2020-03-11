package org.gentar.biology.mutation;

import org.gentar.Mapper;
import org.gentar.biology.mutation.categorizarion.MutationCategorizationService;
import org.gentar.biology.mutation.categorizarion.type.MutationCategorizationType;
import org.springframework.stereotype.Component;
import org.gentar.biology.mutation.categorizarion.MutationCategorization;
import org.gentar.EntityMapper;

import java.util.*;

@Component
public class MutationCategorizationMapper implements Mapper<MutationCategorization, MutationCategorizationDTO>
{
    private EntityMapper entityMapper;
    private MutationCategorizationService mutationCategorizationService;

    public MutationCategorizationMapper(EntityMapper entityMapper, MutationCategorizationService mutationCategorizationService)
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
        MutationCategorization mutationCategorization = entityMapper.toTarget(mutationCategorizationDTO, MutationCategorization.class);
        String mutationCategorizationTypeName = mutationCategorizationDTO.getMutationCategorizationTypeName();
        if (mutationCategorizationTypeName != null)
        {
            MutationCategorizationType mutationCategorizationType = mutationCategorizationService.getMutationCategorizationTypeByName(mutationCategorizationTypeName);
            mutationCategorization.setMutationCategorizationType(mutationCategorizationType);
        }
        return  mutationCategorization;
    }
}
