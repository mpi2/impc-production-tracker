package org.gentar.biology.mutation.categorizarion;

import org.gentar.biology.mutation.categorizarion.type.MutationCategorizationType;

//TODO: This needs to change to reflect the separate mutation categorization type information.
public interface MutationCategorizationService
{
    MutationCategorization getMutationCategorizationByNameAndType(String name, String type);

    MutationCategorization getMutationCategorizationByNameAndTypeFailingWhenNull(String name, String type);

    MutationCategorizationType getMutationCategorizationTypeByName(String name);

    MutationCategorizationType getMutationCategorizationTypeByNameFailingWhenNull(String name);

}
