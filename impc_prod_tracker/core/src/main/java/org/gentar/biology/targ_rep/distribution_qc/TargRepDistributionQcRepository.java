package org.gentar.biology.targ_rep.distribution_qc;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 * TargRepDistributionQcRepository.
 */
public interface TargRepDistributionQcRepository
    extends CrudRepository<TargRepDistributionQc, Long> {

    List<TargRepDistributionQc> findTargRepDistributionQcByEsCellId(Long id);
}
