package org.gentar.report.geneInterest;

import org.gentar.report.geneInterest.gene.GeneInterestReportGeneProjection;
import org.gentar.report.geneInterest.gene.GeneInterestReportGeneServiceImpl;
import org.gentar.report.geneInterest.project.GeneInterestReportProjectProjection;
import org.gentar.report.geneInterest.project.GeneInterestReportProjectServiceImpl;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class GeneInterestReportServiceImpl implements GeneInterestReportService
{
    private final GeneInterestReportProjectServiceImpl projectService;
    private final GeneInterestReportGeneServiceImpl geneService;

    public GeneInterestReportServiceImpl( GeneInterestReportProjectServiceImpl projectService,
                                          GeneInterestReportGeneServiceImpl geneService )
    {
        this.projectService = projectService;
        this.geneService = geneService;
    }

    public void generateGeneInterestReport()
    {
        // Will need to pull a separate set of data later for ES Cell based mutagenesis
        // (Note: should report null and conditional data separately for ES Cell based mutagenesis)
        List<GeneInterestReportProjectProjection> projectProjections = projectService.getGeneInterestReportCrisprProjectProjections();
        List<GeneInterestReportGeneProjection> geneProjections = geneService.getGeneInterestReportCrisprGeneProjections();

        Map<String, String> crisprGenes = getGenes(projectProjections, geneProjections);
        Map<String, List<String>> projectsForCrisprGenes = getProjectsByGene(projectProjections, geneProjections);

        // Could aggregate the ES Cell data with Crispr data here because
        // the assignment summary status for a gene should reflect both types of project
        Map<String, String> assignmentForProjects = getAssignmentByProject(projectProjections, geneProjections);

        // The production status will be reported by class of project
        Map<String, List<String>> plansForProjects = getPlansByProject(projectProjections, geneProjections);
        Map<String, String> statusForPlans = getStatusByPlan(projectProjections, geneProjections);

        calculateGeneAssignmentSummaryStatus(projectsForCrisprGenes, assignmentForProjects);




        printProjectsForGenes(projectsForCrisprGenes);
//        printGenes(genes);
//        writeReport(projectProjections, geneProjections);
    }

    private Map<String, String> calculateGeneAssignmentSummaryStatus(Map<String, List<String>> geneToProjects,
                                                                     Map<String, String> projectToAssignment)
    {
        Map<String, String> geneAssignments = geneToProjects
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> summariseGeneAssignment(e.getValue(), projectToAssignment)
                ));

        return geneAssignments;
    }

    private String summariseGeneAssignment(List<String> projectListForGene, Map<String, String> projectToAssignment)
    {
        String assignment = "";
        if (projectListForGene.size() == 1){
            assignment = projectToAssignment.get(projectListForGene.get(0));
        }
        else {

        }
        return assignment;

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

    private Map<String, String> getAssignmentByProject ( List<GeneInterestReportProjectProjection> pps,
                                                         List<GeneInterestReportGeneProjection> gps)
    {
        Map<String, String> ppsProjectAssignmentMap = pps.stream()
                .collect(Collectors.toMap(
                        GeneInterestReportProjectProjection::getProjectTpn,
                        GeneInterestReportProjectProjection::getAssignmentName,
                        (value1, value2) -> value1 ));

        Map<String, String> gpsProjectAssignmentMap = gps.stream()
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

    private void printGenes(Map<String, String> genes)
    {
        genes.entrySet().stream().forEach(e -> System.out.println(e.getKey() + "\t" + e.getValue()));
    }

    private void printProjectsForGenes(Map<String, List<String>> projectsForGenes)
    {
        projectsForGenes.entrySet().stream().forEach(e -> System.out.println(e.getKey() + "\t" + e.getValue()));
    }

    private void writeReport(List<GeneInterestReportProjectProjection> pps,
                             List<GeneInterestReportGeneProjection> gps)
    {
        pps.stream().forEach(x -> System.out.println(
                x.getProjectTpn() + "\t" +
                        x.getAssignmentName() + "\t" +
                        x.getGeneAccId() + "\t" +
                        x.getGeneSymbol() + "\t" +
                        x.getPlanIdentificationNumber() + "\t" +
                        x.getPlanSummaryStatus()
        ));
//
//        System.out.println();
//        System.out.println("===================================");
//        System.out.println();

//        gps.stream().forEach(x -> System.out.println(
//                x.getProjectTpn() + "\t" +
//                        x.getAssignmentName() + "\t" +
//                        x.getPlanIdentificationNumber() + "\t" +
//                        x.getPlanSummaryStatus() + "\t" +
//                        x.getMutationIdentificationNumber() + "\t" +
//                        x.getMutationSymbol() + "\t" +
//                        x.getGeneAccId() + "\t" +
//                        x.getGeneSymbol()));
    }
}
