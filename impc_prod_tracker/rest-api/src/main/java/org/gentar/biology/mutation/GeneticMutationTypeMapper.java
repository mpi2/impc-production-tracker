package org.gentar.biology.mutation;

import org.gentar.Mapper;
import org.gentar.biology.mutation.genetic_type.GeneticMutationType;
import org.gentar.biology.mutation.genetic_type.GeneticMutationTypeService;
import org.springframework.stereotype.Component;

@Component
public class GeneticMutationTypeMapper implements Mapper<GeneticMutationType, String>
{
    private GeneticMutationTypeService geneticMutationTypeService;

    public GeneticMutationTypeMapper(GeneticMutationTypeService geneticMutationTypeService)
    {
        this.geneticMutationTypeService = geneticMutationTypeService;
    }

    public String toDto(GeneticMutationType entity) {
        String name = null;
        if (entity != null)
        {
            name = entity.getName();
        }
        return name;
    }

    public GeneticMutationType toEntity(String geneticMutationTypeName)
    {
        return geneticMutationTypeService.getGeneticMutationTypeByName(geneticMutationTypeName);
    }
}
