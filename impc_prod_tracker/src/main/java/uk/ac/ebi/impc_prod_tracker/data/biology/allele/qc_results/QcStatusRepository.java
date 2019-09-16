package uk.ac.ebi.impc_prod_tracker.data.biology.allele.qc_results;

import org.springframework.data.repository.CrudRepository;

public interface QcStatusRepository extends CrudRepository<QcStatus, Long> {
}
