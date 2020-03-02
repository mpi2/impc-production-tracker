package org.gentar.biology.mutation.categorizarion;

import org.gentar.biology.mutation.GeneticMutationType;
import org.springframework.data.repository.CrudRepository;

public interface MutationCategorizationRepository extends CrudRepository<MutationCategorization, Long>
{
    MutationCategorization findByName(String name);
}
