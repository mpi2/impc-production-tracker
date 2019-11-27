package org.gentar.biology.plan.attempt.crispr.assay;

import org.springframework.data.repository.CrudRepository;

public interface AssayTypeRepository extends CrudRepository<AssayType, Long>
{
    AssayType findByName(String name);
}
