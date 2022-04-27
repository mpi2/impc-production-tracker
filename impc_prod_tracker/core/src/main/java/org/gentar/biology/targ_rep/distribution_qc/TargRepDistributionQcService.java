package org.gentar.biology.targ_rep.distribution_qc;


import java.util.List;

/**
 * TargRepEsCellService.
 */
public interface TargRepDistributionQcService {
    List<TargRepDistributionQc> getTargRepDistributionQcByEsCellId(Long id);
}
