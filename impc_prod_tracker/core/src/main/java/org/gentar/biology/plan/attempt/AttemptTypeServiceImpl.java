package org.gentar.biology.plan.attempt;

import org.gentar.biology.plan.type.PlanTypeName;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AttemptTypeServiceImpl implements AttemptTypeService
{
    private final AttemptTypeRepository attemptTypeRepository;

    public AttemptTypeServiceImpl (AttemptTypeRepository attemptTypeRepository)
    {
        this.attemptTypeRepository = attemptTypeRepository;
    }

    @Override
    @Cacheable("attemptTypes")
    public AttemptType getAttemptTypeByName(String attemptTypeName)
    {
        return attemptTypeRepository.findByNameIgnoreCase(attemptTypeName);
    }

    @Override
    public List<AttemptTypesName> getAttemptTypesByPlanTypeName(PlanTypeName planTypeName)
    {
        List<AttemptTypesName> attemptTypesNames = new ArrayList<>();
        if (PlanTypeName.PRODUCTION.equals(planTypeName))
        {
            attemptTypesNames.add(AttemptTypesName.CRISPR);
            attemptTypesNames.add(AttemptTypesName.HAPLOESSENTIAL_CRISPR);
        }
        else if (PlanTypeName.PHENOTYPING.equals(planTypeName))
        {
            attemptTypesNames.add(AttemptTypesName.ADULT_PHENOTYPING);
            attemptTypesNames.add(AttemptTypesName.HAPLOESSENTIAL_PHENOTYPING);
        }
        return attemptTypesNames;
    }
}
