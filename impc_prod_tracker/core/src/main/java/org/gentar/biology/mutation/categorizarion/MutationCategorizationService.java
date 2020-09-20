package org.gentar.biology.mutation.categorizarion;

import org.gentar.biology.mutation.categorizarion.type.MutationCategorizationType;

import java.util.List;
import java.util.Map;

//TODO: This needs to change to reflect the separate mutation categorization type information.
public interface MutationCategorizationService
{
    MutationCategorization getMutationCategorizationByNameAndType(String name, String type);

    MutationCategorization getMutationCategorizationByName(String name);

    MutationCategorization getMutationCategorizationByNameAndTypeFailingWhenNull(String name, String type);

    MutationCategorization getMutationCategorizationByNameFailingWhenNull(String name);

    MutationCategorizationType getMutationCategorizationTypeByName(String name);

    MutationCategorizationType getMutationCategorizationTypeByNameFailingWhenNull(String name);

    Map<String, List<String>> getMutationCategorizationNamesByCategorizationTypesNames();

}
