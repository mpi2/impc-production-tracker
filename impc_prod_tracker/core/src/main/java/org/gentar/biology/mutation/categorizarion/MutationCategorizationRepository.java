package org.gentar.biology.mutation.categorizarion;

import org.springframework.data.repository.CrudRepository;

public interface MutationCategorizationRepository extends CrudRepository<MutationCategorization, Long>
{
    MutationCategorization findByNameAndType(String name, String type);
}
