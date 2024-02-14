package org.gentar.biology.targ_rep.distribution.targeting_vector_distribution_product;

import org.gentar.biology.colony.distribution.distribution_network.DistributionNetwork;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Primary
public interface TargRepTargetingVectorDistributionProductRepository extends JpaRepository<TargRepTargetingVectorDistributionProduct, Long> {
    List<TargRepTargetingVectorDistributionProduct> findTargRepTargetingVectorDistributionProductByDistributionIdentifierIgnoreCase(String distributionIdentifier);

    List<TargRepTargetingVectorDistributionProduct> findTargRepTargetingVectorDistributionProductByDistributionNetwork(DistributionNetwork distributionNetwork);

}
