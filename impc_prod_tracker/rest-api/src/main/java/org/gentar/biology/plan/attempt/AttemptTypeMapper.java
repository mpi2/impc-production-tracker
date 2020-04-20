package org.gentar.biology.plan.attempt;

import org.gentar.Mapper;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Service;

@Service
public class AttemptTypeMapper implements Mapper<AttemptType, String>
{
    private AttemptTypeService attemptTypeService;

    private static final String ATTEMPT_TYPE_NOT_FOUND_ERROR = "Attempt type '%s' does not exist.";

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
            throw new UserOperationFailedException(String.format(ATTEMPT_TYPE_NOT_FOUND_ERROR, attemptType));
        }
        return attemptType;
    }
}
