package org.gentar.biology.mutation;

import org.gentar.biology.mutation.categorizarion.MutationCategorization;
import org.gentar.biology.mutation.genetic_type.GeneticMutationType;
import org.gentar.biology.mutation.molecular_type.MolecularMutationType;
import org.springframework.stereotype.Component;

@Component
public interface MutationService
{
    GeneticMutationType getGeneticMutationTypeByName(String geneticMutationTypeName);

    MutationCategorization getMutationCategorizationByNameAndType(String name, String type);

    MutationCategorization getMutationCategorizationByNameAndTypeFailingWhenNull(String name, String type);

    MolecularMutationType getMolecularMutationTypeByName(String name);

    MolecularMutationType getMolecularMutationTypeByNameFailingWhenNull(String name);
}
