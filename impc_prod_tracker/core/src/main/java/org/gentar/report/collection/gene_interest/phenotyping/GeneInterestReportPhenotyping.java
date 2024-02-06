package org.gentar.report.collection.gene_interest.phenotyping;

import org.gentar.biology.gene.Gene;
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
    private Map<Long, Set<GeneInterestReportOutcomeMutationProjection>> completeOutcomeMutationMap;
    private Map<Long, GeneInterestReportOutcomeMutationProjection> filteredOutcomeMutationMap;
    private Map<Long, Set<Gene>> completeMutationGeneMap;
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


    public void summariseData(Set<Long> allOutcomes) {
        fetchData(allOutcomes);
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

    private void fetchData(Set<Long> allOutcomes) {

        // The PhenotypingAttemptProjections must be linked with the Outcomes that can be viewed.
        // Some outcomes are excluded when retrieving Crispr and ES Cell gene projections, thus
        // the set of PhenotypingAttemptProjections retireved from the database are filtered based on outcomeId.
        List<GeneInterestReportPhenotypingAttemptProjection> rawPap =
                phenotypingService.getGeneInterestReportPhenotypingAttemptProjections();

        pap = rawPap
                .stream()
                .filter(projection -> (allOutcomes.contains(projection.getOutcomeId())))
                .collect(Collectors.toList());

        // filteredOutcomeMutationMap select each outcome with one single mutation associated with it.
        // Along with the map, filteredMutationGeneMap, which selects for a 1:1 match between a gene and a mutation
        // it is no longer used.
        completeOutcomeMutationMap = phenotypingService.getMutationMap();
        filteredOutcomeMutationMap = phenotypingService.getFilteredMutationMap(completeOutcomeMutationMap);
        completeMutationGeneMap = phenotypingService.getGeneMap();
        filteredMutationGeneMap = phenotypingService.getFilteredGeneMap(completeMutationGeneMap);
    }

    private Map<String, String> getGenes() {
        return completeMutationGeneMap
                .values()
                .stream()
                .flatMap(Collection::stream)
// Use to debug:
//                .peek(x -> System.out.println(x.getAccId() + ": " + x.getSymbol()))
                .collect(Collectors.toMap(Gene::getAccId,
                        Gene::getSymbol,
                        (value1, value2) -> value1));

    }

    private Map<String, List<String>> getPhenotypingPlansForProjects(){
        return pap.stream()
// Use to debug:
//                .peek(x -> System.out.println(x.getProjectTpn() + ": " + x.getPlanIdentificationNumber()))
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

                ).flatMap(Collection::stream)
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
     }

    private Map<String, Set<String>> getGenesForProjects() {
        return pap.stream()
                .map(x -> {
                            List<Map<String, GeneInterestReportOutcomeMutationProjection>> projectToMutationMapping = new ArrayList<>();
                            for (GeneInterestReportOutcomeMutationProjection mutationMapping: completeOutcomeMutationMap.get(x.getOutcomeId())){
                                Map<String, GeneInterestReportOutcomeMutationProjection> entry = new HashMap<>();
                                entry.put(x.getProjectTpn(), mutationMapping);
                                projectToMutationMapping.add(entry);
                            }
                            return projectToMutationMapping;
                        })
                .flatMap(Collection::stream)

                // unpack each map in the stream of maps in a second flatMap operation
                .flatMap(projectMutationMap -> projectMutationMap.entrySet().stream())
                .map(y -> {
                    List<Map<String, Gene>> projectGenePairs = new ArrayList<>();
                    for (Gene gene: completeMutationGeneMap.get(y.getValue().getMutationId())){
                        Map<String, Gene> geneMapping = new HashMap<>();
                        geneMapping.put(y.getKey(), gene);
                        projectGenePairs.add(geneMapping);
                    }
                    return projectGenePairs;
                })
                .flatMap(Collection::stream)

                // unpack each map in the stream of maps in a second flatMap operation
                .flatMap(projectToGenesMapping -> projectToGenesMapping.entrySet().stream())
//
//              Alternative way to generate the output - Collectors.groupingBy is used below instead
//                .collect(Collectors.toMap(x -> x.getKey(),
//                        x -> new HashSet<>(Arrays.asList(x.getValue().getAccId())),
//                        (x,y)->{x.addAll(y);return x;} ));
//
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.mapping(x -> x.getValue().getAccId(), Collectors.toSet())));



        // old version
//        return pap
//                .stream()
//                .peek(e -> System.out.println(e.getOutcomeId() + ": " + e.getProjectTpn() + ": " + fetchGenesByOutcomeId(e.getOutcomeId())))
////                .collect(Collectors.toMap(GeneInterestReportPhenotypingAttemptProjection::getProjectTpn,
////                        e -> fetchGenesByOutcomeId(e.getOutcomeId())));
//        .collect(Collectors.groupingBy(
//                GeneInterestReportPhenotypingAttemptProjection::getProjectTpn,
//                Collectors.mapping(e -> fetchGenesByOutcomeId(e.getOutcomeId()), Collectors.toSet())));

    }

    private String fetchGenesByOutcomeId(Long outcomeId) {
        // Currently only selects for 1:1 mappings (selection happens when producing the filtered Maps)
//        System.out.println("outcomeId: " + outcomeId);
        GeneInterestReportOutcomeMutationProjection omp = filteredOutcomeMutationMap.get(outcomeId);
//        System.out.println("omp: " + omp);
//        System.out.println("omp.getMutationId(): " + omp.getMutationId());
//        System.out.println("filteredMutationGeneMap.get(omp.getMutationId()): " + filteredMutationGeneMap.get(omp.getMutationId()));
//        System.out.println("filteredMutationGeneMap.get(omp.getMutationId()).getAccId(): " + filteredMutationGeneMap.get(omp.getMutationId()).getAccId());
        return filteredMutationGeneMap.get(omp.getMutationId()).getAccId();
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
