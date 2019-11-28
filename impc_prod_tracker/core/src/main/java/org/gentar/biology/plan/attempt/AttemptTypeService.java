package org.gentar.biology.plan.attempt;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class AttemptTypeService
{
    private AttemptTypeRepository attemptTypeRepository;

    public AttemptTypeService(AttemptTypeRepository attemptTypeRepository)
    {
        this.attemptTypeRepository = attemptTypeRepository;
    }

    @Cacheable("attemptTypes")
    public AttemptType getAttemptTypeByName(String attemptTypeName)
    {
        return attemptTypeRepository.getFirstByNameIgnoreCase(attemptTypeName);
    }
}
