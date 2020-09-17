package org.gentar.biology.mutation.categorizarion;

import org.gentar.biology.mutation.categorizarion.type.MutationCategorizationType;
import org.gentar.biology.mutation.categorizarion.type.MutationCategorizationTypeRepository;
import org.gentar.biology.plan.attempt.AttemptTypesName;
import org.gentar.biology.plan.attempt.phenotyping.stage.type.PhenotypingStageTypeName;
import org.gentar.biology.plan.type.PlanTypeName;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MutationCategorizationServiceImpl implements MutationCategorizationService
{
    private final MutationCategorizationRepository mutationCategorizationRepository;
    private final MutationCategorizationTypeRepository mutationCategorizationTypeRepository;

    public MutationCategorizationServiceImpl(
        MutationCategorizationRepository mutationCategorizationRepository,
        MutationCategorizationTypeRepository mutationCategorizationTypeRepository)
    {
        this.mutationCategorizationRepository = mutationCategorizationRepository;
        this.mutationCategorizationTypeRepository = mutationCategorizationTypeRepository;
    }

    @Override
    public MutationCategorization getMutationCategorizationByNameAndType(String name, String type)
    {
        return mutationCategorizationRepository
            .findByNameAndMutationCategorizationTypeNameIgnoreCase(name, type);
    }

    @Override
    public MutationCategorization getMutationCategorizationByNameAndTypeFailingWhenNull(
        String name, String type)
    {
        MutationCategorization mutationCategorization =
            getMutationCategorizationByNameAndType(name, type);
        if (mutationCategorization == null)
        {
            throw new UserOperationFailedException(
                "Mutation Categorization name '" + name + "' or type '" + type + "' do not exist.");
        }
        return mutationCategorization;
    }

    @Override
    public MutationCategorizationType getMutationCategorizationTypeByName(String name)
    {
        return this.mutationCategorizationTypeRepository.findByNameIgnoreCase(name);
    }

    @Override
    public MutationCategorizationType getMutationCategorizationTypeByNameFailingWhenNull(String name)
    {
        MutationCategorizationType mutationCategorizationType =
            mutationCategorizationTypeRepository.findByNameIgnoreCase(name);
        if (mutationCategorizationType == null)
        {
            throw new UserOperationFailedException(
                "The Mutation Categorization Type: '" + name + "' does not exist.");
        }
        return mutationCategorizationType;
    }

    @Override
    public Map<String, List<String>> getMutationCategorizationNamesByCategorizationTypesNames()
    {
        Map<String, List<String>> map = new HashMap<>();
        List<Object> mutationCategorizations = new ArrayList<>();
        List<MutationCategorization> allMutationCategorization = new ArrayList<>();
        mutationCategorizationRepository.findAll().forEach(allMutationCategorization::add);
        allMutationCategorization.forEach( x -> {
            String key = x.getMutationCategorizationType().getName();
            map.computeIfAbsent(key, k -> new ArrayList<>());
            var list = map.get(key);
            list.add(x.getName());
        });
        mutationCategorizationRepository.findAll().forEach(p -> mutationCategorizations.add(p.getName()));
        return map;
    }
}
