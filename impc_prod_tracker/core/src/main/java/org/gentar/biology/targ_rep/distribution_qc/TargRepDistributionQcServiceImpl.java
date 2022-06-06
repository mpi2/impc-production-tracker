package org.gentar.biology.targ_rep.distribution_qc;

import java.util.List;
import org.springframework.stereotype.Component;


/**
 * TargRepDistributionQcServiceImpl.
 */
@Component
public class TargRepDistributionQcServiceImpl implements TargRepDistributionQcService {
    private final TargRepDistributionQcRepository targRepDistributionQcRepository;


    public TargRepDistributionQcServiceImpl(TargRepDistributionQcRepository targRepDistributionQcRepository) {
        this.targRepDistributionQcRepository = targRepDistributionQcRepository;

    }

    @Override
    public List<TargRepDistributionQc> getTargRepDistributionQcByEsCellId(Long id) {
        return targRepDistributionQcRepository.findTargRepDistributionQcByEsCellId(id);
    }
}
