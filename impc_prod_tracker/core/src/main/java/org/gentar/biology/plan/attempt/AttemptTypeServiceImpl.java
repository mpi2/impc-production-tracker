package org.gentar.biology.plan.attempt;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class AttemptTypeServiceImpl implements AttemptTypeService
{
    private AttemptTypeRepository attemptTypeRepository;

    public AttemptTypeServiceImpl(AttemptTypeRepository attemptTypeRepository)
    {
        this.attemptTypeRepository = attemptTypeRepository;
    }

    @Cacheable("attemptTypes")
    public AttemptType getAttemptTypeByName(String attemptTypeName)
    {
        return attemptTypeRepository.getFirstByNameIgnoreCase(attemptTypeName);
    }
}
