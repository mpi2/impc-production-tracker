package uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.assay.assay_type;

import org.springframework.data.repository.CrudRepository;

public interface AssayTypeRepository extends CrudRepository<AssayType, Long>
{
    AssayType findByName(String name);
}
