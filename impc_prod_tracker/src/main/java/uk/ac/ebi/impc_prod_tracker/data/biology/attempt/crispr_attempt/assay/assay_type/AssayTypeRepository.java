package uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.assay.assay_type;

import org.springframework.data.repository.CrudRepository;

public interface AssayTypeRepository extends CrudRepository<AssayType, Long>
{
    AssayType findByName(String name);
}
