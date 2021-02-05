package org.gentar.report.collection.gene_interest;

import org.gentar.report.ReportServiceImpl;
import org.gentar.report.ReportTypeName;
import org.gentar.report.collection.gene_interest.production_type.GeneInterestReportCrisprProduction;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class GeneInterestReportServiceImpl implements GeneInterestReportService
{
    private final ReportServiceImpl reportService;
    private final GeneInterestReportCrisprProduction crisprProduction;


    private Map<String, String> crisprGenes;
    private Map<String, String> summaryAssignmentForCrisprGenes;
    private Map<String, String> summaryProductionPlanStatusForCrisprGenes;

    private Map<String, String> summaryAssignmentForGenes;
    private Map<String, String> allGenes;

    public GeneInterestReportServiceImpl(ReportServiceImpl reportService,
                                         GeneInterestReportCrisprProduction crisprProduction)
    {
        this.reportService = reportService;
        this.crisprProduction = crisprProduction;
    }

    @Override
    public void generateGeneInterestReport()
    {

        processCrisprData();

        // will require methods to aggregate ES and Crispr gene assignment data
        // and generate a list of all Genes.
        summaryAssignmentForGenes = summaryAssignmentForCrisprGenes;
        allGenes = crisprGenes;

        saveReport();
    }

    private void processCrisprData() {

        crisprProduction.summariseData();

        crisprGenes = crisprProduction.getGeneIdToSymbolMap();
        summaryAssignmentForCrisprGenes = crisprProduction.getGeneIdToAssignmentMap();
        summaryProductionPlanStatusForCrisprGenes = crisprProduction.getGeneIdToProductionPlanStatusMap();
    }


    private void saveReport() {

        String report = assembleReport();
        reportService.saveReport(ReportTypeName.GENE_INTEREST, report);
    }

    private String assembleReport() {

        String header = generateReportHeaders();

        String report = allGenes
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .map(e -> construcReportRow(e))
                .collect(Collectors.joining("\n"));

        return Arrays.asList(header,report).stream().collect(Collectors.joining("\n"));

    }

    private String construcReportRow( Map.Entry<String, String> e ) {
        List<String> content = Arrays.asList(
                e.getValue(),
                e.getKey(),
                summaryAssignmentForGenes.get(e.getKey()),
                "",
                "",
                summaryProductionPlanStatusForCrisprGenes.getOrDefault(e.getKey(), "")
        );

        return content
                .stream()
                .collect(Collectors.joining("\t"));
    }

    private String generateReportHeaders() {
        List<String> headers = Arrays.asList(
                "Gene Symbol",
                "MGI ID",
                "Assignment Status",
                "ES Null Production Status",
                "ES Conditional Production Status",
                "Crispr Production Status"
        );

        String headerString =   headers
                .stream()
                .collect(Collectors.joining("\t"));

        return headerString;

    }
}
