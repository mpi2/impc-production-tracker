package org.gentar.report.collection.gene_interest;

import org.gentar.report.collection.gene_interest.gene.GeneInterestReportGeneProjection;
import org.gentar.report.collection.gene_interest.project.GeneInterestReportProjectProjection;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class GeneInterestReportProjectionMergeHelperImpl {


    public Map<String, String> getGenes (List<GeneInterestReportProjectProjection> pps,
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


    public Map<String, String> getStatusByPlan(List<GeneInterestReportProjectProjection> pps,
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


    public Map<String, List<String>> getProjectsByGene(List<GeneInterestReportProjectProjection> pps,
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


    public Map<String, List<String>> getPlansByProject(List<GeneInterestReportProjectProjection> pps,
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

    public Map<String, String> getAssignmentByProject(List<GeneInterestReportProjectProjection> pps,
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
}
