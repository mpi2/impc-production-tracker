package org.gentar.biology.plan.attempt;

import org.gentar.Mapper;
import org.springframework.stereotype.Component;

@Component
public class AttemptTypeMapper implements Mapper<AttemptType, String>
{
    private final AttemptTypeService attemptTypeService;

    public AttemptTypeMapper(AttemptTypeService attemptTypeService)
    {
        this.attemptTypeService = attemptTypeService;
    }

    @Override
    public String toDto(AttemptType entity)
    {
        return entity.getName();
    }

    @Override
    public AttemptType toEntity(String attemptTypeName)
    {
        return attemptTypeService.getAttemptTypeByNameFailsIfNull(attemptTypeName);
    }
}
