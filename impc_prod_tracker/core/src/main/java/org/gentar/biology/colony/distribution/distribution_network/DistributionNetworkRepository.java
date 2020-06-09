package org.gentar.biology.colony.distribution.distribution_network;

import org.springframework.data.repository.CrudRepository;

public interface DistributionNetworkRepository extends CrudRepository<DistributionNetwork, Long>
{
    DistributionNetwork findByNameIgnoreCase(String name);
}
