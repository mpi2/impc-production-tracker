package org.gentar.biology.colony.distribution.distribution_network;

import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DistributionNetworkService {
    private DistributionNetworkRepository distributionNetworkRepository;

    public DistributionNetworkService(DistributionNetworkRepository distributionNetworkRepository) {
        this.distributionNetworkRepository = distributionNetworkRepository;
    }

    public DistributionNetwork getDistributionNetworkByName(String name) {
        return distributionNetworkRepository.findByNameIgnoreCase(name);
    }

    public DistributionNetwork getDistributionNetworkByNameFailIfNull(String name) {
        DistributionNetwork distributionNetwork = getDistributionNetworkByName(name);
        if (distributionNetwork == null) {
            throw new UserOperationFailedException("Distribution Network " + name + " does not exist");
        }
        return distributionNetwork;
    }

    public List<DistributionNetwork> getAllDistributionNetworks() {
        return distributionNetworkRepository.findAll();

    }
}
