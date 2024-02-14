package org.gentar.biology.colony.distribution;

import jakarta.servlet.http.HttpServletResponse;
import org.gentar.biology.colony.distribution.distribution_network.DistributionNetwork;

import java.io.IOException;
import java.util.List;

public interface DistributionProductService {
    List<DistributionProduct> getDisributionProductByDistributionNetworkId(DistributionNetwork distributionNetworkId);

   void generateDistributionProductReport(HttpServletResponse response,
                                       List<DistributionProduct> distributionProducts) throws IOException;
}
