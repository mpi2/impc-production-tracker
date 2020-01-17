package org.gentar.biology.mutation;

import org.springframework.stereotype.Component;
import org.gentar.biology.mutation.categorizarion.MutationCategorization;
import org.gentar.EntityMapper;
import java.util.Collection;
import java.util.List;

@Component
public class MutationCategorizationMapper
{
    private EntityMapper entityMapper;

    public MutationCategorizationMapper(EntityMapper entityMapper)
    {
        this.entityMapper = entityMapper;
    }

    public MutationCategorizationDTO toDto(MutationCategorization mutationCategorization)
    {
        return entityMapper.toTarget(mutationCategorization, MutationCategorizationDTO.class);
    }

    public List<MutationCategorizationDTO> toDtos(Collection<MutationCategorization> mutationCategorizations)
    {
        return entityMapper.toTargets(mutationCategorizations, MutationCategorizationDTO.class);
    }
}
