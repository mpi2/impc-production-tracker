package org.gentar.biology.mutation;

import org.gentar.biology.mutation.categorizarion.MutationCategorization;
import org.gentar.biology.mutation.categorizarion.MutationCategorizationRepository;
import org.springframework.stereotype.Component;

@Component
public class MutationService
{
    private GeneticMutationTypeRepository geneticMutationTypeRepository;
    private MutationCategorizationRepository mutationCategorizationRepository;

    public MutationService(GeneticMutationTypeRepository geneticMutationTypeRepository, MutationCategorizationRepository mutationCategorizationRepository)
    {
        this.geneticMutationTypeRepository = geneticMutationTypeRepository;
        this.mutationCategorizationRepository = mutationCategorizationRepository;
    }

    public GeneticMutationType getGeneticMutationTypeByName(String geneticMutationTypeName)
    {
        return geneticMutationTypeRepository.findFirstByNameIgnoreCase(geneticMutationTypeName);
    }

    public MutationCategorization getMutationCategorizationByName(String name)
    {
        return mutationCategorizationRepository.findByName(name);
    }
}
