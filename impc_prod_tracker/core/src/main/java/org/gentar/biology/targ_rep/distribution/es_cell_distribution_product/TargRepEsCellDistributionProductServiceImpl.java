package org.gentar.biology.targ_rep.distribution.es_cell_distribution_product;

import jakarta.servlet.http.HttpServletResponse;
import org.gentar.biology.colony.distribution.distribution_network.DistributionNetwork;
import org.gentar.report.Report;
import org.gentar.report.ReportService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


/**
 * TargRepEsCellServiceImpl.
 */
@Component
public class TargRepEsCellDistributionProductServiceImpl implements TargRepEsCellDistributionProductService {

    private final TargRepEsCellDistributionProductRepository targRepEsCellDistributionProductRepository;

    private final ReportService reportService;

    public TargRepEsCellDistributionProductServiceImpl(TargRepEsCellDistributionProductRepository targRepEsCellDistributionProductRepository1, ReportService reportService) {

        this.targRepEsCellDistributionProductRepository = targRepEsCellDistributionProductRepository1;
        this.reportService = reportService;
    }

    @Override
    public List<TargRepEsCellDistributionProduct> getByDistributionIdentifier(String distributionIdentifier) {
        return targRepEsCellDistributionProductRepository.findTargRepEsCellDistributionProductByDistributionIdentifierIgnoreCase(distributionIdentifier);
    }

    @Override
    public List<TargRepEsCellDistributionProduct> getByDistributionNetwork(DistributionNetwork distributionNetwork) {
        return targRepEsCellDistributionProductRepository.findTargRepEsCellDistributionProductByDistributionNetwork(distributionNetwork);
    }

    @Override
    public void generateDistributionProductReport(HttpServletResponse response, List<TargRepEsCellDistributionProduct> targRepEsCellDistributionProducts) throws IOException {
        StringBuilder reportText = new StringBuilder();
        reportText.append("Es Cell Name\tDistribution Network\tStart Date\tEnd Date\tDistribution Identifier\n");


        for (TargRepEsCellDistributionProduct targRepEsCellDistributionProduct : targRepEsCellDistributionProducts) {
            reportText.append(targRepEsCellDistributionProduct.getTargRepEsCell().getName()).append("\t");
            reportText.append(targRepEsCellDistributionProduct.getDistributionNetwork().getName()).append("\t");
            reportText.append(targRepEsCellDistributionProduct.getStartDate()).append("\t");
            reportText.append(targRepEsCellDistributionProduct.getEndDate()).append("\t");
            reportText.append(targRepEsCellDistributionProduct.getDistributionIdentifier()).append("\n");
        }

        Report report = new Report();
        report.setReport(reportText.toString());
        report.setCreatedAt(LocalDateTime.now());
        reportService
                .writeReport(response, "Es_Cell_Distribution_Idnetifier",
                        report);
    }
}
