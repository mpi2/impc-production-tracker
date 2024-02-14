package org.gentar.biology.targ_rep.distribution.targeting_vector_distribution_product;

import jakarta.servlet.http.HttpServletResponse;
import org.gentar.biology.colony.distribution.distribution_network.DistributionNetwork;
import org.gentar.report.Report;
import org.gentar.report.ReportService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;



@Component
public class TargRepTargetingVectorDistributionProductServiceImpl implements TargRepTargetingVectorDistributionProductService {

    private final TargRepTargetingVectorDistributionProductRepository targRepTargetingVectorDistributionProductRepository;

    private final ReportService reportService;

    public TargRepTargetingVectorDistributionProductServiceImpl(TargRepTargetingVectorDistributionProductRepository targRepTargetingVectorDistributionProductRepository, ReportService reportService) {

        this.targRepTargetingVectorDistributionProductRepository = targRepTargetingVectorDistributionProductRepository;
        this.reportService = reportService;
    }

    @Override
    public List<TargRepTargetingVectorDistributionProduct> getByDistributionIdentifier(String distributionIdentifier) {
        return targRepTargetingVectorDistributionProductRepository.findTargRepTargetingVectorDistributionProductByDistributionIdentifierIgnoreCase(distributionIdentifier);
    }

    @Override
    public List<TargRepTargetingVectorDistributionProduct> getByDistributionNetwork(DistributionNetwork distributionNetwork) {
        return targRepTargetingVectorDistributionProductRepository.findTargRepTargetingVectorDistributionProductByDistributionNetwork(distributionNetwork);
    }

    @Override
    public void generateDistributionProductReport(HttpServletResponse response, List<TargRepTargetingVectorDistributionProduct> targRepTargetingVectorDistributionProducts) throws IOException {
        StringBuilder reportText = new StringBuilder();
        reportText.append("Targeting Vector Name\tDistribution Network\tStart Date\tEnd Date\tDistribution Identifier\n");


        for (TargRepTargetingVectorDistributionProduct targRepTargetingVectorDistributionProduct : targRepTargetingVectorDistributionProducts) {
            reportText.append(targRepTargetingVectorDistributionProduct.getTargRepTargetingVector().getName()).append("\t");
            reportText.append(targRepTargetingVectorDistributionProduct.getDistributionNetwork().getName()).append("\t");
            reportText.append(targRepTargetingVectorDistributionProduct.getStartDate()).append("\t");
            reportText.append(targRepTargetingVectorDistributionProduct.getEndDate()).append("\t");
            reportText.append(targRepTargetingVectorDistributionProduct.getDistributionIdentifier()).append("\n");
        }
        Report report = new Report();
        report.setReport(reportText.toString());
        report.setCreatedAt(LocalDateTime.now());
        reportService
                .writeReport(response, "Targeting_Vector_Distribution_Idnetifier",
                        report);
    }
}
