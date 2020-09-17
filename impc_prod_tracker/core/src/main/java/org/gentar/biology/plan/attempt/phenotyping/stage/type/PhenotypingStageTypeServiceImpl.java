package org.gentar.biology.plan.attempt.phenotyping.stage.type;

import org.gentar.biology.plan.attempt.AttemptTypesName;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PhenotypingStageTypeServiceImpl implements PhenotypingStageTypeService
{
    private final PhenotypingStageTypeRepository phenotypingStageTypeRepository;

    public PhenotypingStageTypeServiceImpl(PhenotypingStageTypeRepository phenotypingStageTypeRepository)
    {
        this.phenotypingStageTypeRepository = phenotypingStageTypeRepository;
    }

    @Override
    public PhenotypingStageType getPhenotypingStageTypeByName(String name)
    {
        return phenotypingStageTypeRepository.findByNameIgnoreCase(name);
    }

    @Override
    public List<PhenotypingStageType> getAll()
    {
        return phenotypingStageTypeRepository.findAll();
    }

    @Override
    public List<PhenotypingStageTypeName> getPhenotypingStageTypeNamesByAttemptTypeName(
        AttemptTypesName attemptTypesName)
    {
        List<PhenotypingStageTypeName> phenotypingStageTypeNamesByAttemptTypeName = new ArrayList<>();
        if (AttemptTypesName.ADULT_PHENOTYPING.equals(attemptTypesName))
        {
            phenotypingStageTypeNamesByAttemptTypeName.add(PhenotypingStageTypeName.EARLY_ADULT);
            phenotypingStageTypeNamesByAttemptTypeName.add(PhenotypingStageTypeName.LATE_ADULT);
        }
        else if (AttemptTypesName.HAPLOESSENTIAL_PHENOTYPING.equals(attemptTypesName))
        {
            phenotypingStageTypeNamesByAttemptTypeName.add(PhenotypingStageTypeName.HAPLOESSENTIAL);
            phenotypingStageTypeNamesByAttemptTypeName.add(PhenotypingStageTypeName.EMBRYO);
        }
        return phenotypingStageTypeNamesByAttemptTypeName;
    }

    @Override
    public Map<String, List<String>> getPhenotypingStageTypeNamesByAttemptTypeNamesMap()
    {
        Map<String, List<String>> map = new HashMap<>();
        List<AttemptTypesName> attemptTypesNames =
            Arrays.asList(AttemptTypesName.ADULT_PHENOTYPING, AttemptTypesName.HAPLOESSENTIAL_PHENOTYPING);
        attemptTypesNames.forEach(attemptTypeName -> {
            List<PhenotypingStageTypeName> phenotypingStageTypeNames =
                getPhenotypingStageTypeNamesByAttemptTypeName(attemptTypeName);
            List<String> phenotypingStageTypeNamesValues = new ArrayList<>();
            phenotypingStageTypeNames.forEach(x -> phenotypingStageTypeNamesValues.add(x.getLabel()));
            map.put(attemptTypeName.getLabel(), phenotypingStageTypeNamesValues);
        });
        return map;
    }
}
