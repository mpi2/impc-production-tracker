package org.gentar.biology.mutation.genetic_type;

import org.springframework.stereotype.Component;

@Component
public class GeneticMutationTypeServiceImpl implements GeneticMutationTypeService
{
    private GeneticMutationTypeRepository geneticMutationTypeRepository;

    public GeneticMutationTypeServiceImpl(GeneticMutationTypeRepository geneticMutationTypeRepository)
    {
        this.geneticMutationTypeRepository = geneticMutationTypeRepository;
    }
    public GeneticMutationType getGeneticMutationTypeByName(String geneticMutationTypeName)
    {
        return geneticMutationTypeRepository.findFirstByNameIgnoreCase(geneticMutationTypeName);
    }
}
