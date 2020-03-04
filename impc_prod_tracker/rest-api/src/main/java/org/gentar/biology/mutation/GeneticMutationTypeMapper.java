package org.gentar.biology.mutation;

import org.gentar.biology.mutation.genetic_type.GeneticMutationType;
import org.gentar.biology.mutation.genetic_type.GeneticMutationTypeService;
import org.springframework.stereotype.Component;

@Component
public class GeneticMutationTypeMapper
{
    private GeneticMutationTypeService geneticMutationTypeService;

    public GeneticMutationTypeMapper(GeneticMutationTypeService geneticMutationTypeService)
    {
        this.geneticMutationTypeService = geneticMutationTypeService;
    }

    public GeneticMutationType toEntity(String geneticMutationTypeName)
    {
        return geneticMutationTypeService.getGeneticMutationTypeByName(geneticMutationTypeName);
    }
}
