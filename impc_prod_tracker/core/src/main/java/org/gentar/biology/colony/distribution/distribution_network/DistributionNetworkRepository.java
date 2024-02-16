package org.gentar.biology.colony.distribution.distribution_network;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DistributionNetworkRepository extends CrudRepository<DistributionNetwork, Long>
{
    DistributionNetwork findByNameIgnoreCase(String name);

    List<DistributionNetwork> findAll();

}
