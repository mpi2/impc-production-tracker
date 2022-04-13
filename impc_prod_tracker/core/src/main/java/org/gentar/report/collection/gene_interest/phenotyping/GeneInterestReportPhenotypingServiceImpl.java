package org.gentar.report.collection.gene_interest.phenotyping;

import org.gentar.biology.gene.Gene;
import org.gentar.report.collection.gene_interest.mutation.GeneInterestReportMutationGeneProjection;
import org.gentar.report.collection.gene_interest.mutation.GeneInterestReportMutationServiceImpl;
import org.gentar.report.collection.gene_interest.outcome.GeneInterestReportOutcomeMutationProjection;
import org.gentar.report.collection.gene_interest.outcome.GeneInterestReportOutcomeServiceImpl;
import org.gentar.report.collection.gene_interest.phenotyping_attempt.GeneInterestReportPhenotypingAttemptProjection;
import org.gentar.report.collection.gene_interest.phenotyping_attempt.GeneInterestReportPhenotypingAttemptServiceImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@Component
public class GeneInterestReportPhenotypingServiceImpl implements GeneInterestReportPhenotypingService{

    private final GeneInterestReportPhenotypingAttemptServiceImpl phenotypingAttemptService;
    private final GeneInterestReportOutcomeServiceImpl outcomeReportService;
    private final GeneInterestReportMutationServiceImpl mutationReportService;

    private List<GeneInterestReportPhenotypingAttemptProjection> pap;
    private Map<Long, Set<GeneInterestReportOutcomeMutationProjection>> completeOutcomeMutationMap;
    private Map<Long, GeneInterestReportOutcomeMutationProjection> filteredOutcomeMutationMap;

    public GeneInterestReportPhenotypingServiceImpl(GeneInterestReportPhenotypingAttemptServiceImpl geneInterestReportPhenotypingAttemptService,
                                                    GeneInterestReportOutcomeServiceImpl geneInterestReportOutcomeService,
                                                    GeneInterestReportMutationServiceImpl geneInterestReportMutationService) {
        this.phenotypingAttemptService = geneInterestReportPhenotypingAttemptService;
        this.outcomeReportService = geneInterestReportOutcomeService;
        this.mutationReportService = geneInterestReportMutationService;
    }

    private static <T> List<List<T>> getBatches(List<T> collection, int batchSize) {
        return IntStream.iterate(0, i -> i < collection.size(), i -> i + batchSize)
                .mapToObj(i -> collection.subList(i, Math.min(i + batchSize, collection.size())))
                .collect(Collectors.toList());
    }

    @Override
    public Map<Long, Set<GeneInterestReportOutcomeMutationProjection>> getMutationMap() {

        // Fetch outcomeIds from the GeneInterestReportPhenotypingAttemptProjections
        List<Long> outcomeIds = getOutcomeIds();
//        System.out.println("outcomeIds");
//        outcomeIds.stream()
//                .forEach(System.out::println);

        //Split outcomeIds into batches
        List<List<Long>> groups = getBatches(outcomeIds, 1000);


        List<GeneInterestReportOutcomeMutationProjection> omp = new ArrayList<>();
        groups.forEach(groupIds -> outcomeReportService.getSelectedOutcomeMutationProjections(groupIds).stream().forEach(omp::add));

//        System.out.println("omp");
//        omp.stream()
//                .forEach(x -> System.out.println(x.getMutationId() + ": " + x.getMutationId() + ": " + x.getSymbol()));

        return omp
                        .stream()
                        .filter(m -> m.getSymbol() != null && m.getSymbol() != "")
                        .collect(Collectors.groupingBy(
                                GeneInterestReportOutcomeMutationProjection::getOutcomeId,
                                Collectors.mapping(entry -> entry, Collectors.toSet())));

    }

    @Override
    public Map<Long, GeneInterestReportOutcomeMutationProjection> getFilteredMutationMap(Map<Long, Set<GeneInterestReportOutcomeMutationProjection>> outcomeMutationMap) {
        return outcomeMutationMap
                .entrySet()
                .stream()
 //Use to debug:
//                .peek(e -> System.out.println(e.getKey() + ": " + e.getValue().size() + ": " + List.copyOf(e.getValue()).get(0).getMutationId()))
                .filter(e -> e.getValue().size() == 1)
                .collect(Collectors.toMap(Map.Entry::getKey, e -> List.copyOf(e.getValue()).get(0)));

    }

    @Override
    public Map<Long, Set<Gene>> getGeneMap() {
        completeOutcomeMutationMap = getMutationMap();
        filteredOutcomeMutationMap = getFilteredMutationMap(completeOutcomeMutationMap);
        List<Long> mutationIds = getAllMutationIds();
        List<List<Long>> groups = getBatches(mutationIds, 1000);
        List<GeneInterestReportMutationGeneProjection> mgp = new ArrayList<>();
        groups.forEach(groupIds -> mutationReportService.getSelectedMutationGeneProjections(groupIds).stream().forEach(mgp::add));

//        System.out.println("mutationGeneMap");
        return mgp
                .stream()
// Use to debug:
//                .peek(x -> System.out.println(x.getMutationId() + ": " + x.getGene().getId()))
                .collect(Collectors.groupingBy(
                        GeneInterestReportMutationGeneProjection::getMutationId,
                        Collectors.mapping(GeneInterestReportMutationGeneProjection::getGene, Collectors.toSet())));
     }

    @Override
    public Map<Long, Gene> getFilteredGeneMap(Map<Long, Set<Gene>> completeMutationGeneMap) {
        // select mutations associated with only one gene - compatible with existing MGI iMits report
        return completeMutationGeneMap
                .entrySet()
                .stream()
                .filter(e -> e.getValue().size() == 1)
// Use to debug:
//                .peek(map -> System.out.println(map.getKey() + ": " + List.copyOf(map.getValue()).get(0).getId()))
                .collect(Collectors.toMap(map -> map.getKey(), map -> List.copyOf(map.getValue()).get(0)));
    }

    @Override
    public List<GeneInterestReportPhenotypingAttemptProjection> getGeneInterestReportPhenotypingAttemptProjections() {
        pap = phenotypingAttemptService.getPhenotypingAttemptProjections();
        return pap;
    }

    private List<Long> getAllMutationIds() {
        return completeOutcomeMutationMap
                .entrySet()
                .stream()
                .map(e -> e.getValue())
                .flatMap(x -> x.stream())
                .map(i -> i.getMutationId())
                .distinct()
                .collect(Collectors.toList());
    }

    private List<Long> getFilteredMutationIds() {
        return filteredOutcomeMutationMap
                .entrySet()
                .stream()
                .map(e -> e.getValue().getMutationId())
                .distinct()
                .collect(Collectors.toList());
    }


    private List<Long> getOutcomeIds() {
        List<Long> outcomeIds = pap.stream().map(x -> x.getOutcomeId()).distinct().collect(Collectors.toList());
        return outcomeIds;
    }
}


