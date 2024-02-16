package org.gentar.biology.plan.attempt.breeding;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.springframework.stereotype.Component;

@Component
public class BreedingAttemptMapper implements Mapper<BreedingAttempt, BreedingAttemptDTO>
{
    private final EntityMapper entityMapper;

    public BreedingAttemptMapper(EntityMapper entityMapper)
    {
        this.entityMapper = entityMapper;
    }

    @Override
    public BreedingAttemptDTO toDto(BreedingAttempt entity)
    {
        return entityMapper.toTarget(entity, BreedingAttemptDTO.class);
    }

    @Override
    public BreedingAttempt toEntity(BreedingAttemptDTO dto)
    {
        return entityMapper.toTarget(dto, BreedingAttempt.class);
    }
}
