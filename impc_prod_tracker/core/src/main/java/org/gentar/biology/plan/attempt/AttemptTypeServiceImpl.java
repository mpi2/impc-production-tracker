package org.gentar.biology.plan.attempt;

import org.gentar.biology.plan.type.PlanTypeName;
import org.gentar.biology.plan.type.PlanTypeRepository;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AttemptTypeServiceImpl implements AttemptTypeService
{
    private final AttemptTypeRepository attemptTypeRepository;
    private final PlanTypeRepository planTypeRepository;

    private static final String ATTEMPT_TYPE_NOT_FOUND_ERROR = "Attempt type '%s' does not exist.";

    public AttemptTypeServiceImpl(
        AttemptTypeRepository attemptTypeRepository, PlanTypeRepository planTypeRepository)
    {
        this.attemptTypeRepository = attemptTypeRepository;
        this.planTypeRepository = planTypeRepository;
    }

    @Override
    public List<AttemptType> getAll()
    {
        return attemptTypeRepository.findAll();
    }

    @Override
    @Cacheable("attemptTypes")
    public AttemptType getAttemptTypeByName(String attemptTypeName)
    {
        return attemptTypeRepository.findByNameIgnoreCase(attemptTypeName);
    }

    @Override
    public AttemptType getAttemptTypeByNameFailsIfNull(String attemptTypeName)
    {
        AttemptType attemptType = getAttemptTypeByName(attemptTypeName);
        if (attemptType == null)
        {
            throw new UserOperationFailedException(
                String.format(ATTEMPT_TYPE_NOT_FOUND_ERROR, attemptTypeName));
        }
        return attemptType;
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

    @Override
    public Map<String, List<String>> getAttemptTypesByPlanTypeNameMap()
    {
        Map<String, List<String>> map = new HashMap<>();
        var planTypes = planTypeRepository.findAll();
        planTypes.forEach(planType -> {
            PlanTypeName planTypeName = PlanTypeName.valueOfLabel(planType.getName());
            List<AttemptTypesName> attemptTypesNames =
                getAttemptTypesByPlanTypeName(planTypeName);
            List<String> attemptTypesNamesValues = new ArrayList<>();
            attemptTypesNames.forEach(x -> attemptTypesNamesValues.add(x.getLabel()));
            map.put(planTypeName.getLabel(), attemptTypesNamesValues);
        });
        return map;
    }
}
