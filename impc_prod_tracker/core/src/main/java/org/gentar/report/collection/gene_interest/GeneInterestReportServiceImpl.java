package org.gentar.report.collection.gene_interest;

import org.gentar.report.ReportServiceImpl;
import org.gentar.report.ReportTypeName;
import org.gentar.report.collection.gene_interest.gene.GeneInterestReportGeneProjection;
import org.gentar.report.collection.gene_interest.gene.GeneInterestReportGeneServiceImpl;
import org.gentar.report.collection.gene_interest.project.GeneInterestReportProjectProjection;
import org.gentar.report.collection.gene_interest.project.GeneInterestReportProjectServiceImpl;
import org.gentar.report.utils.assignment.GeneAssignmentStatusHelperImpl;
import org.gentar.report.utils.status.GeneStatusSummaryHelperImpl;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class GeneInterestReportServiceImpl implements GeneInterestReportService
{
    private final GeneInterestReportProjectServiceImpl projectService;
    private final GeneInterestReportGeneServiceImpl geneService;
    private final ReportServiceImpl reportService;
    private final GeneStatusSummaryHelperImpl geneStatusSummaryHelper;
    private final GeneAssignmentStatusHelperImpl geneAssignmentStatusHelper;
    private final GeneInterestReportProjectionMergeHelperImpl projectionMergeHelper;


    private List<GeneInterestReportProjectProjection> crisprProjectProjections;
    private List<GeneInterestReportGeneProjection> crisprGeneProjections;
    private Map<String, String> crisprGenes;
    private Map<String, String> allGenes;
    private Map<String, List<String>> projectsForCrisprGenes;
    private Map<String, String> assignmentForProjects;

    private Map<String, List<String>> productionPlansForCrisprProjects;
    private Map<String, String> statusForCrisprProductionPlans;
    private Map<String, String> summaryAssignmentForCrisprGenes;
    private Map<String, String> summaryProductionPlanStatusForCrisprGenes;

    private Map<String, String> summaryAssignmentForGenes;

    public GeneInterestReportServiceImpl(GeneInterestReportProjectServiceImpl projectService,
                                         GeneInterestReportGeneServiceImpl geneService,
                                         ReportServiceImpl reportService,
                                         GeneStatusSummaryHelperImpl geneStatusSummaryHelper,
                                         GeneAssignmentStatusHelperImpl geneAssignmentStatusHelper,
                                         GeneInterestReportProjectionMergeHelperImpl projectionMergeHelper)
    {
        this.projectService = projectService;
        this.geneService = geneService;
        this.reportService = reportService;
        this.geneStatusSummaryHelper = geneStatusSummaryHelper;
        this.geneAssignmentStatusHelper = geneAssignmentStatusHelper;
        this.projectionMergeHelper = projectionMergeHelper;
    }

    public void generateGeneInterestReport()
    {

        // Will need to pull a separate set of data later for ES Cell based mutagenesis
        // (Note: should report null and conditional data separately for ES Cell based mutagenesis)
        crisprProjectProjections = projectService.getGeneInterestReportCrisprProjectProjections();
        crisprGeneProjections = geneService.getGeneInterestReportCrisprGeneProjections();

        crisprGenes = projectionMergeHelper.getGenes(crisprProjectProjections, crisprGeneProjections);
        projectsForCrisprGenes = projectionMergeHelper.getProjectsByGene(crisprProjectProjections, crisprGeneProjections);

        // We should aggregate the ES Cell data with Crispr data here because
        // the assignment summary status for a gene should reflect both types of project
        assignmentForProjects = projectionMergeHelper.getAssignmentByProject(crisprProjectProjections, crisprGeneProjections);

        // The production status will be reported by class of project
        productionPlansForCrisprProjects = projectionMergeHelper.getPlansByProject(crisprProjectProjections, crisprGeneProjections);

        statusForCrisprProductionPlans = projectionMergeHelper.getStatusByPlan(crisprProjectProjections, crisprGeneProjections);

        summaryAssignmentForCrisprGenes = geneAssignmentStatusHelper.calculateGeneAssignmentStatuses(projectsForCrisprGenes, assignmentForProjects);
        summaryProductionPlanStatusForCrisprGenes = geneStatusSummaryHelper.calculateGenePlanSummaryStatus(projectsForCrisprGenes, productionPlansForCrisprProjects, statusForCrisprProductionPlans);

        // requires a method to aggregate ES and Crispr gene data
        summaryAssignmentForGenes = summaryAssignmentForCrisprGenes;
        allGenes = crisprGenes;
        saveReport();
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
