package org.gentar.biology.mutation;

import org.springframework.stereotype.Component;

@Component
public class MutationService
{
    private GeneticMutationTypeRepository geneticMutationTypeRepository;

    public MutationService(GeneticMutationTypeRepository geneticMutationTypeRepository)
    {
        this.geneticMutationTypeRepository = geneticMutationTypeRepository;
    }

    public GeneticMutationType getGeneticMutationTypeByName(String geneticMutationTypeName)
    {
        return geneticMutationTypeRepository.findFirstByNameIgnoreCase(geneticMutationTypeName);
    }
}
