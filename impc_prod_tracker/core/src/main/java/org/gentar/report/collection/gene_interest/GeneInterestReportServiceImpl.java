package org.gentar.report.collection.gene_interest;

import org.gentar.report.ReportServiceImpl;
import org.gentar.report.ReportTypeName;
import org.gentar.report.collection.gene_interest.phenotyping.GeneInterestReportPhenotyping;
import org.gentar.report.collection.gene_interest.production_type.GeneInterestReportCrisprProduction;
import org.gentar.report.collection.gene_interest.production_type.GeneInterestReportEsCellProduction;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class GeneInterestReportServiceImpl implements GeneInterestReportService
{
    private final ReportServiceImpl reportService;
    private final GeneInterestReportCrisprProduction crisprProduction;
    private final GeneInterestReportEsCellProduction esCellProduction;
    private final GeneInterestReportPhenotyping phenotyping;
    private final GeneInterestReportMergeHelperImpl mergeHelper;


    private Map<String, String> crisprGenes;
    private Set<Long> crisprOutcomes;
    private Map<String, String> summaryAssignmentForCrisprGenes;
    private Map<String, String> summaryProductionPlanStatusForCrisprGenes;


    private Map<String, String> esCellGenes;
    private Set<Long> esCellOutcomes;
    private Map<String, String> summaryAssignmentForEsCellGenes;
    private Map<String, String> summaryProductionPlanStatusForEsCellGenes;
    private Map<String, String> summaryProductionPlanStatusForEsCellNullTargetedGenes;
    private Map<String, String> summaryProductionPlanStatusForEsCellConditionalTargetedGenes;

    private Map<String, String> phenotypingGenes;
    private Map<String, String> summaryEarlyAdultPhenotypingStageStatusForPhenotypingGenes;

    private Map<String, String> summaryAssignmentForGenes;
    private Map<String, String> allGenes;

    public GeneInterestReportServiceImpl(ReportServiceImpl reportService,
                                         GeneInterestReportCrisprProduction crisprProduction,
                                         GeneInterestReportEsCellProduction esCellProduction,
                                         GeneInterestReportPhenotyping phenotyping,
                                         GeneInterestReportMergeHelperImpl mergeHelper)
    {
        this.reportService = reportService;
        this.crisprProduction = crisprProduction;
        this.esCellProduction = esCellProduction;
        this.phenotyping = phenotyping;
        this.mergeHelper = mergeHelper;
    }

    @Override
    public void generateGeneInterestReport()
    {

        processCrisprData();
        processEsCellData();
        processPhenotypingData();

        // will require methods to aggregate ES and Crispr gene assignment data
        // and generate a list of all Genes.

        // This is not required for the phenotyping data,
        // because processCrisprData should have identified all the projects and genes.

        summaryAssignmentForGenes = mergeHelper.mergeSummaryAssignmentForGenes(
                summaryAssignmentForCrisprGenes,summaryAssignmentForEsCellGenes);

        allGenes = mergeHelper.mergeGenes(crisprGenes,esCellGenes);

        saveReport();
    }

    private void processCrisprData() {

        crisprProduction.summariseData();

        crisprOutcomes = crisprProduction.getCrisprOutcomeSet();
        crisprGenes = crisprProduction.getGeneIdToSymbolMap();
        summaryAssignmentForCrisprGenes = crisprProduction.getGeneIdToAssignmentMap();
        summaryProductionPlanStatusForCrisprGenes = crisprProduction.getGeneIdToProductionPlanStatusMap();
    }

    private void processEsCellData() {

        esCellProduction.summariseData();

        esCellOutcomes = esCellProduction.getESCellOutcomeSet();
        esCellGenes = esCellProduction.getGeneIdToSymbolMap();
        summaryAssignmentForEsCellGenes = esCellProduction.getGeneIdToAssignmentMap();
        summaryProductionPlanStatusForEsCellGenes = esCellProduction.getGeneIdToProductionPlanStatusMap();
        summaryProductionPlanStatusForEsCellNullTargetedGenes =
                esCellProduction.getGeneIdToProductionPlanStatusMapForNullTargetedGenes();
        summaryProductionPlanStatusForEsCellConditionalTargetedGenes =
                esCellProduction.getGeneIdToProductionPlanStatusMapForConditionalTargetedGenes();

    }

    private void processPhenotypingData() {

        Set<Long> allOutcomes = new HashSet<>(crisprOutcomes);
        allOutcomes.addAll(esCellOutcomes);

        phenotyping.summariseData(allOutcomes);

        phenotypingGenes = phenotyping.getGeneIdToSymbolMap();
        summaryEarlyAdultPhenotypingStageStatusForPhenotypingGenes = phenotyping.getGeneIdToEarlyAdultPhenotypingStageStatusStatusMap();
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
                summaryProductionPlanStatusForEsCellNullTargetedGenes.getOrDefault(e.getKey(), ""),
                summaryProductionPlanStatusForEsCellConditionalTargetedGenes.getOrDefault(e.getKey(), ""),
                summaryProductionPlanStatusForCrisprGenes.getOrDefault(e.getKey(), ""),
                summaryEarlyAdultPhenotypingStageStatusForPhenotypingGenes.getOrDefault(e.getKey(), "")
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
                "Crispr Production Status",
                "Early Adult Phenotyping Status"
        );

        String headerString =   headers
                .stream()
                .collect(Collectors.joining("\t"));

        return headerString;

    }
}
