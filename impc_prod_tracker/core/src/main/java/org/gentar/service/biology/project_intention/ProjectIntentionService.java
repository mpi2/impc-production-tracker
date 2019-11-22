package org.gentar.service.biology.project_intention;

import org.springframework.stereotype.Component;
import org.gentar.biology.project_intention.type.IntentionType;
import org.gentar.biology.project_intention.type.IntentionTypeRepository;

import org.springframework.cache.annotation.Cacheable;

@Component
public class ProjectIntentionService
{
    private IntentionTypeRepository intentionTypeRepository;

    public ProjectIntentionService(IntentionTypeRepository intentionTypeRepository)
    {
        this.intentionTypeRepository = intentionTypeRepository;
    }

    @Cacheable("intentionTypes")
    public IntentionType getIntentionTypeByName(String name)
    {
        return intentionTypeRepository.findFirstByNameIgnoreCase(name);
    }
}
