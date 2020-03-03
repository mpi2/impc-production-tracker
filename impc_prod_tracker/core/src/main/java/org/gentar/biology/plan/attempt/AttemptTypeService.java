package org.gentar.biology.plan.attempt;

import org.springframework.cache.annotation.Cacheable;

public interface AttemptTypeService
{
    @Cacheable("attemptTypes")
    AttemptType getAttemptTypeByName(String attemptTypeName);
}
