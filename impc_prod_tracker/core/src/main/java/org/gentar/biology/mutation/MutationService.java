package org.gentar.biology.mutation;

import org.gentar.biology.mutation.categorizarion.MutationCategorization;
import org.gentar.biology.mutation.genetic_type.GeneticMutationType;
import org.springframework.stereotype.Component;

@Component
public interface MutationService
{
    GeneticMutationType getGeneticMutationTypeByName(String geneticMutationTypeName);

    MutationCategorization getMutationCategorizationByName(String name);
}
