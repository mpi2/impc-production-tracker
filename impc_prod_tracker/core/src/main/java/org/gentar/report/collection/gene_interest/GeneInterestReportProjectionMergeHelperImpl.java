package org.gentar.report.collection.gene_interest;

import org.gentar.report.collection.gene_interest.gene.GeneInterestReportGeneProjection;
import org.gentar.report.collection.gene_interest.project.GeneInterestReportProjectProjection;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class GeneInterestReportProjectionMergeHelperImpl implements GeneInterestReportProjectionMergeHelper {

    @Override
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

        return Stream.concat(
                ppsGeneMap.entrySet().stream(),gpsGeneMap.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (value1, value2) -> value1 ));
    }

    @Override
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

        return Stream.concat(
                ppsPlanStatusMap.entrySet().stream(),gpsPlanStatusMap.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (value1, value2) -> value1 ));

    }

    @Override
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

        return Stream.concat(
                ppsProjectsByGene.entrySet().stream(), gpsProjectsByGene.entrySet().stream() )
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        map -> List.copyOf(map.getValue()),
                        (set1,set2) -> List.copyOf(Stream.concat(set1.stream(), set2.stream()).collect(Collectors.toSet()))));
    }

    @Override
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

        return Stream.concat(
                ppsPlansByProject.entrySet().stream(), gpsPlansByProject.entrySet().stream() )
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        map -> List.copyOf(map.getValue()),
                        (set1,set2) -> List.copyOf(Stream.concat(set1.stream(), set2.stream()).collect(Collectors.toSet()))));

    }

    @Override
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

        return Stream.concat(
                ppsProjectAssignmentMap.entrySet().stream(),gpsProjectAssignmentMap.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (value1, value2) -> value1 ));

    }

    public Map<String, List<String>> getProjectsByGeneFilteredForNullTargeting(
            List<GeneInterestReportProjectProjection> esCellProjectProjections,
            List<GeneInterestReportGeneProjection> esCellGeneProjections,
            Map<String, String> mutationToAlleleCategory)
    {
        // filter the existing lists
        List<GeneInterestReportProjectProjection> projectProjectionsNotInGeneProjections =
                filterProjectProjections(esCellProjectProjections, esCellGeneProjections);

        List<GeneInterestReportGeneProjection> esCellGeneNullTargetingProjections =
                filterGeneProjections(esCellGeneProjections, mutationToAlleleCategory,"null");

        // combine the filtered lists
        return getProjectsByGene(projectProjectionsNotInGeneProjections, esCellGeneNullTargetingProjections);

    }

    public Map<String, List<String>> getProjectsByGeneFilteredForConditionalTargeting(
            List<GeneInterestReportGeneProjection> esCellGeneProjections,
            Map<String, String> mutationToAlleleCategory)
    {
        // Filter the gene projections
        List<GeneInterestReportGeneProjection> esCellGeneConditionalTargetingProjections =
                filterGeneProjections(esCellGeneProjections, mutationToAlleleCategory, "conditional");

        Map<String, Set<String>> gpsProjectsByGene = esCellGeneConditionalTargetingProjections.stream()
                .collect(Collectors.groupingBy(
                        GeneInterestReportGeneProjection::getGeneAccId,
                        Collectors.mapping(GeneInterestReportGeneProjection::getProjectTpn, Collectors.toSet())));

        return gpsProjectsByGene.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        map -> List.copyOf(map.getValue()),
                        (set1,set2) -> List.copyOf(Stream.concat(set1.stream(), set2.stream()).collect(Collectors.toSet()))));
    }


    public Map<String, List<String>> getPlansByProjectFilteredForNullTargeting(
            List<GeneInterestReportProjectProjection> esCellProjectProjections,
            List<GeneInterestReportGeneProjection> esCellGeneProjections,
            Map<String, String> mutationToAlleleCategory)
    {
        // filter the existing lists
        List<GeneInterestReportProjectProjection> projectProjectionsNotInGeneProjections =
                filterProjectProjections(esCellProjectProjections, esCellGeneProjections);

        List<GeneInterestReportGeneProjection> esCellGeneNullTargetingProjections =
                filterGeneProjections(esCellGeneProjections, mutationToAlleleCategory,"null");

        // combine the filtered lists
        return getPlansByProject(projectProjectionsNotInGeneProjections, esCellGeneNullTargetingProjections);

    }



    public Map<String, List<String>> getPlansByProjectFilteredForConditionalTargeting(
            List<GeneInterestReportGeneProjection> esCellGeneProjections,
            Map<String, String> mutationToAlleleCategory)
    {
        // Filter the gene projections
        List<GeneInterestReportGeneProjection> esCellGeneConditionalTargetingProjections =
                filterGeneProjections(esCellGeneProjections, mutationToAlleleCategory, "conditional");

        Map<String, Set<String>> gpsPlansByProject = esCellGeneConditionalTargetingProjections.stream()
                .collect(Collectors.groupingBy(
                        GeneInterestReportGeneProjection::getProjectTpn,
                        Collectors.mapping(GeneInterestReportGeneProjection::getPlanIdentificationNumber, Collectors.toSet())));

        return gpsPlansByProject.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        map -> List.copyOf(map.getValue()),
                        (set1,set2) -> List.copyOf(Stream.concat(set1.stream(), set2.stream()).collect(Collectors.toSet()))));

    }



    public Map<String, String> getStatusByPlanFilteredForNullTargeting(
                    List<GeneInterestReportProjectProjection> esCellProjectProjections,
                    List<GeneInterestReportGeneProjection> esCellGeneProjections,
                    Map<String, String> mutationToAlleleCategory)
    {
        // filter the existing lists
        List<GeneInterestReportProjectProjection> projectProjectionsNotInGeneProjections =
                filterProjectProjections(esCellProjectProjections, esCellGeneProjections);

        List<GeneInterestReportGeneProjection> esCellGeneNullTargetingProjections =
                filterGeneProjections(esCellGeneProjections, mutationToAlleleCategory,"null");

        // combine the filtered lists
        return getStatusByPlan(projectProjectionsNotInGeneProjections, esCellGeneNullTargetingProjections);

    }

    public Map<String, String> getStatusByPlanFilteredForConditionalTargeting(
                    List<GeneInterestReportGeneProjection> esCellGeneProjections,
                    Map<String, String> mutationToAlleleCategory)
    {
        // Filter the gene projections
        List<GeneInterestReportGeneProjection> esCellGeneConditionalTargetingProjections =
                filterGeneProjections(esCellGeneProjections, mutationToAlleleCategory, "conditional");

        Map<String, String> gpsPlanStatusMap = esCellGeneConditionalTargetingProjections.stream()
                .collect(Collectors.toMap(
                        GeneInterestReportGeneProjection::getPlanIdentificationNumber,
                        GeneInterestReportGeneProjection::getPlanSummaryStatus,
                        (value1, value2) -> value1 ));

        return gpsPlanStatusMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (value1, value2) -> value1 ));

    }

    private List<GeneInterestReportProjectProjection> filterProjectProjections(
                        List<GeneInterestReportProjectProjection> esCellProjectProjections,
                        List<GeneInterestReportGeneProjection> esCellGeneProjections) {

        Set<String> planPinFilterSet = generatePlanFilterSet(esCellGeneProjections);
        return esCellProjectProjections
                .stream()
                .filter(pp -> !(planPinFilterSet.contains(pp.getPlanIdentificationNumber())))
                .collect(Collectors.toList());
    }

    private List<GeneInterestReportGeneProjection> filterGeneProjections(
                        List<GeneInterestReportGeneProjection> esCellGeneProjections,
                        Map<String, String> mutationToAlleleCategory, String mutationClass) {

        Set<String> mutationFilterSet = generateMutationFilterSet(mutationToAlleleCategory, mutationClass);

        return esCellGeneProjections
                .stream()
                .filter(gp -> mutationFilterSet.contains(gp.getMutationIdentificationNumber()))
                .collect(Collectors.toList());
    }



    private Set<String> generatePlanFilterSet(List<GeneInterestReportGeneProjection> geneProjections){
        return geneProjections
                .stream()
                .map(GeneInterestReportGeneProjection::getPlanIdentificationNumber)
                .collect(Collectors.toSet());
    }

    private Set<String> generateMutationFilterSet(Map<String, String> mutationToAlleleCategory, String mutationClass) {
        if (!Arrays.asList("null", "conditional").contains(mutationClass)) {
            throw new IllegalArgumentException("unable to process the mutationClass provided");
        }

        return mutationToAlleleCategory
                .entrySet()
                .stream()
                .filter(e -> e.getValue().equals(mutationClass))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }
}
