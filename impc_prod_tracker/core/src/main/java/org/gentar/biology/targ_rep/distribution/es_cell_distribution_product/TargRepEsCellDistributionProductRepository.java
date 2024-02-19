package org.gentar.biology.targ_rep.distribution.es_cell_distribution_product;

import org.gentar.biology.colony.distribution.distribution_network.DistributionNetwork;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Primary
public interface TargRepEsCellDistributionProductRepository extends JpaRepository<TargRepEsCellDistributionProduct, Long> {
    List<TargRepEsCellDistributionProduct> findTargRepEsCellDistributionProductByDistributionIdentifierIgnoreCase(String distributionIdentifier);

    List<TargRepEsCellDistributionProduct> findTargRepEsCellDistributionProductByDistributionNetwork(DistributionNetwork distributionNetwork);

}
