package org.gentar.biology.mutation.categorizarion.type;

import org.gentar.biology.plan.attempt.crispr.assay.AssayType;
import org.springframework.data.repository.CrudRepository;

public interface MutationCategorizationTypeRepository extends CrudRepository<MutationCategorizationType, Long>
{
    MutationCategorizationType findByNameIgnoreCase(String name);
}
