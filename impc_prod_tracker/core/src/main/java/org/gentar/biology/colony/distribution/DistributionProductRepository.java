package org.gentar.biology.colony.distribution;

import org.gentar.biology.colony.distribution.distribution_network.DistributionNetwork;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DistributionProductRepository extends CrudRepository<DistributionProduct, Long> {

    List<DistributionProduct> findByDistributionIdentifierNotNullAndDistributionNetwork(DistributionNetwork distributionNetwork);
}
