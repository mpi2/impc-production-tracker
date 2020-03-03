package org.gentar.biology.mutation;

import org.springframework.stereotype.Component;
import org.gentar.biology.mutation.categorizarion.MutationCategorization;
import org.gentar.EntityMapper;

import java.util.*;

@Component
public class MutationCategorizationMapper
{
    private EntityMapper entityMapper;
    private MutationService mutationService;

    public MutationCategorizationMapper(EntityMapper entityMapper, MutationService mutationService)
    {
        this.entityMapper = entityMapper;
        this.mutationService = mutationService;
    }

    public MutationCategorizationDTO toDto(MutationCategorization mutationCategorization)
    {
        return entityMapper.toTarget(mutationCategorization, MutationCategorizationDTO.class);
    }

    public List<MutationCategorizationDTO> toDtos(Collection<MutationCategorization> mutationCategorizations)
    {
        return entityMapper.toTargets(mutationCategorizations, MutationCategorizationDTO.class);
    }

    public MutationCategorization toEntity(MutationCategorizationDTO mutationCategorizationDTO)
    {
        return mutationService.getMutationCategorizationByNameAndType(mutationCategorizationDTO.getName(), mutationCategorizationDTO.getType());
    }

    public Set<MutationCategorization> toEntities(Collection<MutationCategorizationDTO> mutationCategorizationDTOS)
    {
        Set<MutationCategorization> mutationCategorizations = new HashSet<>();
        if (mutationCategorizationDTOS != null)
        {
            mutationCategorizationDTOS.forEach(x -> mutationCategorizations.add(toEntity(x)));
        }
        return mutationCategorizations;
    }
}
