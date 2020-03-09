package org.gentar.biology.mutation.categorizarion;

public interface MutationCategorizationService
{
    MutationCategorization getMutationCategorizationByNameAndType(String name, String type);

    MutationCategorization getMutationCategorizationByNameAndTypeFailingWhenNull(String name, String type);
}
