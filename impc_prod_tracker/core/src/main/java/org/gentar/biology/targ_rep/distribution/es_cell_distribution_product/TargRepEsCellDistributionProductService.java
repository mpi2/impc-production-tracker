package org.gentar.biology.targ_rep.distribution.es_cell_distribution_product;

import jakarta.servlet.http.HttpServletResponse;
import org.gentar.biology.colony.distribution.distribution_network.DistributionNetwork;

import java.io.IOException;
import java.util.List;


/**
 * TargRepEsCellService.
 */
public interface TargRepEsCellDistributionProductService {
    List<TargRepEsCellDistributionProduct> getByDistributionIdentifier(String distributionIdentifier);
    List<TargRepEsCellDistributionProduct> getByDistributionNetwork(DistributionNetwork distributionNetwork);

    void generateDistributionProductReport(HttpServletResponse response, List<TargRepEsCellDistributionProduct> targRepEsCellDistributionProducts) throws IOException;
}
