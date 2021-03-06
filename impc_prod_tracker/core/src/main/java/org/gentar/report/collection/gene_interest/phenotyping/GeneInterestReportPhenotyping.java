package org.gentar.report.collection.gene_interest.phenotyping;

import org.gentar.biology.gene.Gene;
import org.gentar.report.collection.common.phenotyping.mutation.CommonPhenotypingColonyReportMutationGeneProjection;
import org.gentar.report.collection.gene_interest.outcome.GeneInterestReportOutcomeMutationProjection;
import org.gentar.report.collection.gene_interest.phenotyping_attempt.GeneInterestReportPhenotypingAttemptProjection;
import org.gentar.report.utils.status.GeneStatusSummaryHelperImpl;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class GeneInterestReportPhenotyping {

    private final GeneInterestReportPhenotypingServiceImpl phenotypingService;

    private final GeneStatusSummaryHelperImpl geneStatusSummaryHelper;

    private List<GeneInterestReportPhenotypingAttemptProjection> pap;
    private Map<Long, GeneInterestReportOutcomeMutationProjection> filteredOutcomeMutationMap;
    private Map<Long, Gene> filteredMutationGeneMap;

    private Map<String, String> phenotypingGenes;
    private Map<String, List<String>> projectsForPhenotypingGenes;

    private Map<String, List<String>> phenotypingPlansForProjects;
    private Map<String, String> phenotypingStageStatusForPhenotypingPlans;
    private Map<String, String> summaryEarlyAdultPhenotypingStageStatusForPhenotypingGenes;

    public GeneInterestReportPhenotyping(GeneInterestReportPhenotypingServiceImpl phenotypingService,
                                         GeneStatusSummaryHelperImpl geneStatusSummaryHelper) {
        this.phenotypingService = phenotypingService;
        this.geneStatusSummaryHelper = geneStatusSummaryHelper;
    }


    public void summariseData() {
        fetchData();
        phenotypingGenes = getGenes();
        phenotypingPlansForProjects = getPhenotypingPlansForProjects();
        phenotypingStageStatusForPhenotypingPlans = getPhenotypingStageStatusForPhenotypingPlans();
        projectsForPhenotypingGenes = getProjectsForGenes();
        setGeneEarlyAdultPhenotypingStageSummaryStatuses();
    }

    public Map<String, String> getGeneIdToSymbolMap(){
        return phenotypingGenes;
    }

    public Map<String, String> getGeneIdToEarlyAdultPhenotypingStageStatusStatusMap(){
        return summaryEarlyAdultPhenotypingStageStatusForPhenotypingGenes;
    }

    private void fetchData() {
        pap = phenotypingService.getGeneInterestReportPhenotypingAttemptProjections();
        filteredOutcomeMutationMap = phenotypingService.getMutationMap();
        filteredMutationGeneMap = phenotypingService.getGeneMap();
    }

    private Map<String, String> getGenes() {
        return filteredMutationGeneMap
                .values()
                .stream()
                .collect(Collectors.toMap(Gene::getAccId,
                        Gene::getSymbol,
                        (value1, value2) -> value1));

    }

    private Map<String, List<String>> getPhenotypingPlansForProjects(){
        return pap.stream()
                .collect(Collectors.groupingBy(GeneInterestReportPhenotypingAttemptProjection::getProjectTpn,
                        Collectors.mapping(GeneInterestReportPhenotypingAttemptProjection::getPlanIdentificationNumber, Collectors.toList())));
    }

    private Map<String, String> getPhenotypingStageStatusForPhenotypingPlans() {
        return pap.stream()
                .collect(Collectors.toMap(GeneInterestReportPhenotypingAttemptProjection::getPlanIdentificationNumber,
                        GeneInterestReportPhenotypingAttemptProjection::getPhenotypingStageStatus));
    }

    private Map<String, List<String>> getProjectsForGenes() {
        Map<String, Set<String>> genesForProjects = getGenesForProjects();

        // This following stream operation converts a projectTpnToGeneIds Map into a GeneIdToProjectTpns Map
        // The map step unpacks the List of GeneIds associated with each projectTpn
        // The flatMap then streams the resulting collection (Stream<Map<String, String>>)
        // The stream of maps then requires each map to be unpacked in the second flatMap operation
        // into a Stream<Map<K,V>.Entry<String,String>> before being collected.
        // see: https://stackoverflow.com/questions/51932232/java-8-group-map-by-key

        return genesForProjects
                .entrySet()
                .stream()
                .map(x -> {
                    List<Map<String, String>> pairs = new ArrayList<>();
                    for (String value:x.getValue()){
                        Map<String,String> item = new HashMap<>();
                        item.put(value, x.getKey());
                        pairs.add(item);
                    }
                    return pairs;
                }

                ).flatMap(x -> x.stream())
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
     }

    private Map<String, Set<String>> getGenesForProjects() {
        return pap
                .stream()
//                .collect(Collectors.toMap(GeneInterestReportPhenotypingAttemptProjection::getProjectTpn,
//                        e -> fetchGenesByOutcomeId(e.getOutcomeId())));
        .collect(Collectors.groupingBy(
                GeneInterestReportPhenotypingAttemptProjection::getProjectTpn,
                Collectors.mapping(e -> fetchGenesByOutcomeId(e.getOutcomeId()), Collectors.toSet())));

    }

    private String fetchGenesByOutcomeId(Long outcomeId) {
        // Currently only selects for 1:1 mappings (selection happens when producing the filtered Maps)
        GeneInterestReportOutcomeMutationProjection omp = filteredOutcomeMutationMap.get(outcomeId);
        String gene = filteredMutationGeneMap.get(omp.getMutationId()).getAccId();
        return gene;
    }


    private void setGeneEarlyAdultPhenotypingStageSummaryStatuses(){
        summaryEarlyAdultPhenotypingStageStatusForPhenotypingGenes =
                geneStatusSummaryHelper
                        .calculateGenePlanSummaryStatus(
                                projectsForPhenotypingGenes,
                                phenotypingPlansForProjects,
                                phenotypingStageStatusForPhenotypingPlans);

    }

}
