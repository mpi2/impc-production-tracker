package org.gentar.biology.targ_rep.distribution.targeting_vector_distribution_product;

import jakarta.servlet.http.HttpServletResponse;
import org.gentar.biology.colony.distribution.distribution_network.DistributionNetwork;

import java.io.IOException;
import java.util.List;



public interface TargRepTargetingVectorDistributionProductService {
    List<TargRepTargetingVectorDistributionProduct> getByDistributionIdentifier(String distributionIdentifier);

    List<TargRepTargetingVectorDistributionProduct> getByDistributionNetwork(DistributionNetwork distributionNetwork);

    void generateDistributionProductReport(HttpServletResponse response, List<TargRepTargetingVectorDistributionProduct> targRepTargetingVectorDistributionProducts) throws IOException;
}
