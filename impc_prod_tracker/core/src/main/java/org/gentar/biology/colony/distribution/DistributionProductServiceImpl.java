package org.gentar.biology.colony.distribution;

import jakarta.servlet.http.HttpServletResponse;
import org.gentar.biology.colony.distribution.distribution_network.DistributionNetwork;
import org.gentar.report.Report;
import org.gentar.report.ReportService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DistributionProductServiceImpl implements DistributionProductService {


    private final DistributionProductRepository distributionProductRepository;

    private final ReportService reportService;

    public DistributionProductServiceImpl(DistributionProductRepository distributionProductRepository, ReportService reportService) {

        this.distributionProductRepository = distributionProductRepository;
        this.reportService = reportService;
    }

    @Override
    public List<DistributionProduct> getDisributionProductByDistributionNetworkId(DistributionNetwork distributionNetworkId) {
        return distributionProductRepository.findByDistributionIdentifierNotNullAndDistributionNetwork(distributionNetworkId);
    }


    public void generateDistributionProductReport(HttpServletResponse response,
                                                 List<DistributionProduct> distributionProducts) throws IOException {
        StringBuilder reportText = new StringBuilder();
        reportText.append("Colony Name\tWork Unit\tDistribution Network\tProduct Type\tStart Date\tEnd Date\tDistribution Identifier\n");


        for (DistributionProduct distributionProduct : distributionProducts) {
            reportText.append(distributionProduct.getColony().getName()).append("\t");
            reportText.append(distributionProduct.getDistributionCentre().getName()).append("\t");
            reportText.append(distributionProduct.getDistributionNetwork().getName()).append("\t");
            reportText.append(distributionProduct.getProductType().getName()).append("\t");
            reportText.append(distributionProduct.getStartDate()).append("\t");
            reportText.append(distributionProduct.getEndDate()).append("\t");
            reportText.append(distributionProduct.getDistributionIdentifier()).append("\n");
        }
        Report report = new Report();
        report.setReport(reportText.toString());
        report.setCreatedAt(LocalDateTime.now());
        reportService
                .writeReport(response, "Colony_Distribution_Idnetifier",
                        report);
    }
}
