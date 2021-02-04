package org.gentar.report.collection.gene_interest;

import org.gentar.biology.project.assignment.AssignmentStatus;
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
import java.util.stream.Stream;

@Component
public class GeneInterestReportServiceImpl implements GeneInterestReportService
{
    private final GeneInterestReportProjectServiceImpl projectService;
    private final GeneInterestReportGeneServiceImpl geneService;
    private final ReportServiceImpl reportService;
    private final GeneStatusSummaryHelperImpl geneStatusSummaryHelper;
    private final GeneAssignmentStatusHelperImpl geneAssignmentStatusHelper;


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
                                         GeneAssignmentStatusHelperImpl geneAssignmentStatusHelper)
    {
        this.projectService = projectService;
        this.geneService = geneService;
        this.reportService = reportService;
        this.geneStatusSummaryHelper = geneStatusSummaryHelper;
        this.geneAssignmentStatusHelper = geneAssignmentStatusHelper;
    }

    public void generateGeneInterestReport()
    {

        // Will need to pull a separate set of data later for ES Cell based mutagenesis
        // (Note: should report null and conditional data separately for ES Cell based mutagenesis)
        crisprProjectProjections = projectService.getGeneInterestReportCrisprProjectProjections();
        crisprGeneProjections = geneService.getGeneInterestReportCrisprGeneProjections();

        crisprGenes = getGenes(crisprProjectProjections, crisprGeneProjections);
        projectsForCrisprGenes = getProjectsByGene(crisprProjectProjections, crisprGeneProjections);

        // We should aggregate the ES Cell data with Crispr data here because
        // the assignment summary status for a gene should reflect both types of project
        assignmentForProjects = getAssignmentByProject();

        // The production status will be reported by class of project
        productionPlansForCrisprProjects = getPlansByProject(crisprProjectProjections, crisprGeneProjections);

        statusForCrisprProductionPlans = getStatusByPlan(crisprProjectProjections, crisprGeneProjections);

        summaryAssignmentForCrisprGenes = geneAssignmentStatusHelper.calculateGeneAssignmentStatuses(projectsForCrisprGenes, assignmentForProjects);
        summaryProductionPlanStatusForCrisprGenes = geneStatusSummaryHelper.calculateGenePlanSummaryStatus(projectsForCrisprGenes, productionPlansForCrisprProjects, statusForCrisprProductionPlans);

        // requires a method to aggregate ES and Crispr gene data
        summaryAssignmentForGenes = summaryAssignmentForCrisprGenes;
        allGenes = crisprGenes;
        saveReport();
        //writeReport();

//        printAssignmentStatuses(assignmentStatuses);
//        printProjectsForGenes(projectsForCrisprGenes);
//        printSummaryAssignmentForGenes(summaryAssignmentForGenes);
//        printGenes(crisprGenes);
//        writeReport(crisprProjectProjections, geneProjections);
    }

    private Map<String, String> getGenes (List<GeneInterestReportProjectProjection> pps,
                                              List<GeneInterestReportGeneProjection> gps)
    {
        Map<String, String> ppsGeneMap = pps.stream()
                .collect(Collectors.toMap(
                        GeneInterestReportProjectProjection::getGeneAccId,
                        GeneInterestReportProjectProjection::getGeneSymbol,
                        (value1, value2) -> value1 ));

        Map<String, String> gpsGeneMap = gps.stream()
                .collect(Collectors.toMap(
                        GeneInterestReportGeneProjection::getGeneAccId,
                        GeneInterestReportGeneProjection::getGeneSymbol,
                        (value1, value2) -> value1 ));

        Map<String, String> geneMap = Stream.concat(
                ppsGeneMap.entrySet().stream(),gpsGeneMap.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (value1, value2) -> value1 ));
        return geneMap;
    }

    private Map<String, String> getAssignmentByProject ()
    {
        Map<String, String> ppsProjectAssignmentMap = crisprProjectProjections.stream()
                .collect(Collectors.toMap(
                        GeneInterestReportProjectProjection::getProjectTpn,
                        GeneInterestReportProjectProjection::getAssignmentName,
                        (value1, value2) -> value1 ));

        Map<String, String> gpsProjectAssignmentMap = crisprGeneProjections.stream()
                .collect(Collectors.toMap(
                        GeneInterestReportGeneProjection::getProjectTpn,
                        GeneInterestReportGeneProjection::getAssignmentName,
                        (value1, value2) -> value1 ));

        Map<String, String> projectAssignemtMap = Stream.concat(
                ppsProjectAssignmentMap.entrySet().stream(),gpsProjectAssignmentMap.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (value1, value2) -> value1 ));
        return projectAssignemtMap;

    }

    private Map<String, String> getStatusByPlan ( List<GeneInterestReportProjectProjection> pps,
                                                         List<GeneInterestReportGeneProjection> gps)
    {
        Map<String, String> ppsPlanStatusMap = pps.stream()
                .collect(Collectors.toMap(
                        GeneInterestReportProjectProjection::getPlanIdentificationNumber,
                        GeneInterestReportProjectProjection::getPlanSummaryStatus,
                        (value1, value2) -> value1 ));

        Map<String, String> gpsPlanStatusMap = gps.stream()
                .collect(Collectors.toMap(
                        GeneInterestReportGeneProjection::getPlanIdentificationNumber,
                        GeneInterestReportGeneProjection::getPlanSummaryStatus,
                        (value1, value2) -> value1 ));

        Map<String, String> planStatusMap = Stream.concat(
                ppsPlanStatusMap.entrySet().stream(),gpsPlanStatusMap.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (value1, value2) -> value1 ));
        return planStatusMap;

    }


    private Map<String, List<String>> getProjectsByGene ( List<GeneInterestReportProjectProjection> pps,
                                                                 List<GeneInterestReportGeneProjection> gps)
    {
        Map<String, Set<String>> ppsProjectsByGene = pps.stream()
                .collect(Collectors.groupingBy(
                        GeneInterestReportProjectProjection::getGeneAccId,
                        Collectors.mapping(GeneInterestReportProjectProjection::getProjectTpn, Collectors.toSet())));

        Map<String, Set<String>> gpsProjectsByGene = gps.stream()
                .collect(Collectors.groupingBy(
                        GeneInterestReportGeneProjection::getGeneAccId,
                        Collectors.mapping(GeneInterestReportGeneProjection::getProjectTpn, Collectors.toSet())));

        Map<String, List<String>> projectsByGene = Stream.concat(
                ppsProjectsByGene.entrySet().stream(), gpsProjectsByGene.entrySet().stream() )
                .collect(Collectors.toMap(
                        map -> map.getKey(),
                        map -> List.copyOf(map.getValue()),
                        (set1,set2) -> List.copyOf(Stream.concat(set1.stream(), set2.stream()).collect(Collectors.toSet()))));

        return projectsByGene;
    }

    private Map<String, List<String>> getPlansByProject ( List<GeneInterestReportProjectProjection> pps,
                                                         List<GeneInterestReportGeneProjection> gps)
    {
        Map<String, Set<String>> ppsPlansByProject = pps.stream()
                .collect(Collectors.groupingBy(
                        GeneInterestReportProjectProjection::getProjectTpn,
                        Collectors.mapping(GeneInterestReportProjectProjection::getPlanIdentificationNumber, Collectors.toSet())));

        Map<String, Set<String>> gpsPlansByProject = gps.stream()
                .collect(Collectors.groupingBy(
                        GeneInterestReportGeneProjection::getProjectTpn,
                        Collectors.mapping(GeneInterestReportGeneProjection::getPlanIdentificationNumber, Collectors.toSet())));

        Map<String, List<String>> plansByProject = Stream.concat(
                ppsPlansByProject.entrySet().stream(), gpsPlansByProject.entrySet().stream() )
                .collect(Collectors.toMap(
                        map -> map.getKey(),
                        map -> List.copyOf(map.getValue()),
                        (set1,set2) -> List.copyOf(Stream.concat(set1.stream(), set2.stream()).collect(Collectors.toSet()))));

        return plansByProject;

    }

    private void printSummaryAssignmentForGenes(Map<String, String>  summaryAssignmentStatusByGene)
    {
        writeSeparator();

        summaryAssignmentStatusByGene
                .entrySet()
                .stream()
                .forEach(a -> System.out.println(a.getKey() + "\t" + a.getValue()));
    }

    private void printAssignmentStatuses(List<AssignmentStatus> assignmentStatuses)
    {
        assignmentStatuses.stream().forEach(a -> System.out.println(a.getName() + "\t" + a.getOrdering()));
    }

    private void printGenes(Map<String, String> genes)
    {
        genes.entrySet().stream().forEach(e -> System.out.println(e.getKey() + "\t" + e.getValue()));
    }

    private void printProjectsForGenes(Map<String, List<String>> projectsForGenes)
    {
        projectsForGenes.entrySet().stream().forEach(e -> System.out.println(e.getKey() + "\t" + e.getValue()));
    }

    private void saveReport() {

        String report = assembleReport();
        reportService.saveReport(ReportTypeName.GENE_INTEREST, report);
    }

    private void writeReport()
    {

        String report = assembleReport();
        writeSeparator();

        System.out.println(report);

        writeSeparator();
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

    private void writeSeparator() {
        System.out.println();
        System.out.println("===================================");
        System.out.println();
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
