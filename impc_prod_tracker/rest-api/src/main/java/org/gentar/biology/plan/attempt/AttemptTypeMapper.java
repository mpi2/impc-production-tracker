package org.gentar.biology.plan.attempt;

import org.gentar.Mapper;
import org.springframework.stereotype.Service;

@Service
public class AttemptTypeMapper implements Mapper<AttemptType, String>
{
    private AttemptTypeService attemptTypeService;

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
        AttemptType attemptType = attemptTypeService.getAttemptTypeByName(attemptTypeName);
        if (attemptType == null)
        {
            attemptType = new AttemptType();
            attemptType.setName(attemptTypeName);

        }

        return attemptType;
    }
}
