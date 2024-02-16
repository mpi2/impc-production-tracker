package org.gentar.report.export;

import jakarta.servlet.http.HttpServletResponse;
import org.gentar.biology.colony.distribution.DistributionProduct;
import org.gentar.biology.colony.distribution.DistributionProductService;
import org.gentar.biology.colony.distribution.distribution_network.DistributionNetwork;
import org.gentar.biology.colony.distribution.distribution_network.DistributionNetworkService;
import org.gentar.biology.targ_rep.distribution.es_cell_distribution_product.TargRepEsCellDistributionProduct;
import org.gentar.biology.targ_rep.distribution.es_cell_distribution_product.TargRepEsCellDistributionProductService;
import org.gentar.biology.targ_rep.distribution.targeting_vector_distribution_product.TargRepTargetingVectorDistributionProduct;
import org.gentar.biology.targ_rep.distribution.targeting_vector_distribution_product.TargRepTargetingVectorDistributionProductService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class DistributionIdReportExportController {

    private final DistributionProductService distributionProductService;

    private final DistributionNetworkService distributionNetworkService;
    private final TargRepEsCellDistributionProductService targRepEsCellDistributionProductService;
    private final TargRepTargetingVectorDistributionProductService targRepTargetingVectorDistributionProductService;

    public DistributionIdReportExportController(DistributionProductService distributionProductService, DistributionNetworkService distributionNetworkService, TargRepEsCellDistributionProductService targRepEsCellDistributionProductService, TargRepTargetingVectorDistributionProductService targRepTargetingVectorDistributionProductService) {
        this.distributionProductService = distributionProductService;
        this.distributionNetworkService = distributionNetworkService;

        this.targRepEsCellDistributionProductService = targRepEsCellDistributionProductService;
        this.targRepTargetingVectorDistributionProductService = targRepTargetingVectorDistributionProductService;
    }

    @GetMapping("/distribution")
    @Transactional(readOnly = true)
    public void export(HttpServletResponse response,
                       @RequestParam(value = "networkname")
                       String networkName,
                       @RequestParam(value = "reporttype")
                       String reporttype) throws Exception {

        DistributionNetwork distributionNetwork = distributionNetworkService.getDistributionNetworkByName(networkName);
        if (distributionNetwork == null) {
            throw new Exception("distributionNetwork can not be found");
        }
        switch (reporttype) {
            case "colony" -> {
                List<DistributionProduct> distributionProducts = distributionProductService.getDisributionProductByDistributionNetworkId(distributionNetwork);
                distributionProductService.generateDistributionProductReport(response, distributionProducts);
            }
            case "targetingVector", "targetingvector" -> {
                List<TargRepTargetingVectorDistributionProduct> targRepTargetingVectorDistributionProducts = targRepTargetingVectorDistributionProductService.getByDistributionNetwork(distributionNetwork);
                targRepTargetingVectorDistributionProductService.generateDistributionProductReport(response, targRepTargetingVectorDistributionProducts);
            }
            case "esCell", "escell" -> {
                List<TargRepEsCellDistributionProduct> targRepEsCellDistributionProducts = targRepEsCellDistributionProductService.getByDistributionNetwork(distributionNetwork);
                targRepEsCellDistributionProductService.generateDistributionProductReport(response, targRepEsCellDistributionProducts);
            }
        }
    }

}