package org.gentar.biology.mutation;

import org.gentar.biology.mutation.categorizarion.MutationCategorization;
import org.gentar.biology.mutation.categorizarion.MutationCategorizationRepository;
import org.gentar.biology.mutation.genetic_type.GeneticMutationType;
import org.gentar.biology.mutation.genetic_type.GeneticMutationTypeRepository;
import org.springframework.stereotype.Component;

@Component
public class MutationServiceImpl implements MutationService
{
    private GeneticMutationTypeRepository geneticMutationTypeRepository;
    private MutationCategorizationRepository mutationCategorizationRepository;

    public MutationServiceImpl(GeneticMutationTypeRepository geneticMutationTypeRepository, MutationCategorizationRepository mutationCategorizationRepository)
    {
        this.geneticMutationTypeRepository = geneticMutationTypeRepository;
        this.mutationCategorizationRepository = mutationCategorizationRepository;
    }

    public GeneticMutationType getGeneticMutationTypeByName(String geneticMutationTypeName)
    {
        return geneticMutationTypeRepository.findFirstByNameIgnoreCase(geneticMutationTypeName);
    }

    public MutationCategorization getMutationCategorizationByNameAndType(String name, String type)
    {
        return mutationCategorizationRepository.findByNameAndType(name, type);
    }
}
