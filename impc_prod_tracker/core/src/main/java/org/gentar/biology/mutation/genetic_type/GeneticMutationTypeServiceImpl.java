package org.gentar.biology.mutation.genetic_type;

import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

@Component
public class GeneticMutationTypeServiceImpl implements GeneticMutationTypeService
{
    private GeneticMutationTypeRepository geneticMutationTypeRepository;

    public GeneticMutationTypeServiceImpl(
        GeneticMutationTypeRepository geneticMutationTypeRepository)
    {
        this.geneticMutationTypeRepository = geneticMutationTypeRepository;
    }

    public GeneticMutationType getGeneticMutationTypeByName(String name)
    {
        return geneticMutationTypeRepository.findFirstByNameIgnoreCase(name);
    }

    public GeneticMutationType getGeneticMutationTypeByNameFailsIfNull(String name)
    {
        GeneticMutationType geneticMutationType = getGeneticMutationTypeByName(name);
        if (geneticMutationType == null)
        {
            throw new UserOperationFailedException(
                "Genetic mutation type " + name + " does not exist.");
        }
        return geneticMutationType;
    }
}
