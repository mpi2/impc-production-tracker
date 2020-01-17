package org.gentar.biology.mutation;

import org.springframework.stereotype.Component;

@Component
public class GeneticMutationTypeMapper
{
    private MutationService mutationService;

    public GeneticMutationTypeMapper(MutationService mutationService)
    {
        this.mutationService = mutationService;
    }

    public GeneticMutationType toEntity(String geneticMutationTypeName)
    {
        return mutationService.getGeneticMutationTypeByName(geneticMutationTypeName);
    }
}
