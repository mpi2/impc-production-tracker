package org.gentar.report.collection.gene_interest.phenotyping;

import org.gentar.biology.gene.Gene;
import org.gentar.report.collection.gene_interest.mutation.GeneInterestReportMutationGeneProjection;
import org.gentar.report.collection.gene_interest.mutation.GeneInterestReportMutationServiceImpl;
import org.gentar.report.collection.gene_interest.outcome.GeneInterestReportOutcomeMutationProjection;
import org.gentar.report.collection.gene_interest.outcome.GeneInterestReportOutcomeServiceImpl;
import org.gentar.report.collection.gene_interest.phenotyping_attempt.GeneInterestReportPhenotypingAttemptProjection;
import org.gentar.report.collection.gene_interest.phenotyping_attempt.GeneInterestReportPhenotypingAttemptServiceImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GeneInterestReportPhenotypingServiceImpl implements GeneInterestReportPhenotypingService{

    private final GeneInterestReportPhenotypingAttemptServiceImpl phenotypingAttemptService;
    private final GeneInterestReportOutcomeServiceImpl outcomeReportService;
    private final GeneInterestReportMutationServiceImpl mutationReportService;

    private List<GeneInterestReportPhenotypingAttemptProjection> pap;
    private Map<Long, GeneInterestReportOutcomeMutationProjection> filteredOutcomeMutationMap;

    public GeneInterestReportPhenotypingServiceImpl(GeneInterestReportPhenotypingAttemptServiceImpl geneInterestReportPhenotypingAttemptService,
                                                    GeneInterestReportOutcomeServiceImpl geneInterestReportOutcomeService,
                                                    GeneInterestReportMutationServiceImpl geneInterestReportMutationService) {
        this.phenotypingAttemptService = geneInterestReportPhenotypingAttemptService;
        this.outcomeReportService = geneInterestReportOutcomeService;
        this.mutationReportService = geneInterestReportMutationService;
    }

    @Override
    public Map<Long, GeneInterestReportOutcomeMutationProjection> getMutationMap() {
        List<Long> outcomeIds = getOutcomeIds();
        List<GeneInterestReportOutcomeMutationProjection> omp = outcomeReportService.getSelectedOutcomeMutationProjections(outcomeIds);
        Map<Long, Set<GeneInterestReportOutcomeMutationProjection>> outcomeMutationMap =
                omp
                        .stream()
                        .collect(Collectors.groupingBy(
                                GeneInterestReportOutcomeMutationProjection::getOutcomeId,
                                Collectors.mapping(entry -> entry, Collectors.toSet())));

        // select outcomes associated with only one mutation - compatible with existing MGI iMits report
        return outcomeMutationMap
                .entrySet()
                .stream()
                .filter(e -> e.getValue().size() == 1)
                .collect(Collectors.toMap(Map.Entry::getKey, e -> List.copyOf(e.getValue()).get(0)));
    }

    @Override
    public Map<Long, Gene> getGeneMap() {
        filteredOutcomeMutationMap = getMutationMap();
        List<Long> filteredMutationIds = getFilteredMutationIds();
        List<GeneInterestReportMutationGeneProjection> mgp =
                mutationReportService.getSelectedMutationGeneProjections(filteredMutationIds);

        Map<Long, Set<Gene>> mutationGeneMap = mgp
                .stream()
                .collect(Collectors.groupingBy(
                        GeneInterestReportMutationGeneProjection::getMutationId,
                        Collectors.mapping(GeneInterestReportMutationGeneProjection::getGene, Collectors.toSet())));

        // select mutations associated with only one gene - compatible with existing MGI iMits report
        return mutationGeneMap
                .entrySet()
                .stream()
                .filter(e -> e.getValue().size() == 1)
                .collect(Collectors.toMap(map -> map.getKey(), map -> List.copyOf(map.getValue()).get(0)));
    }

    @Override
    public List<GeneInterestReportPhenotypingAttemptProjection> getGeneInterestReportPhenotypingAttemptProjections() {
        pap = phenotypingAttemptService.getPhenotypingAttemptProjections();
        return pap;
    }

    private List<Long> getFilteredMutationIds() {
        return filteredOutcomeMutationMap
                .entrySet()
                .stream()
                .map(e -> e.getValue().getMutationId())
                .collect(Collectors.toList());
    }


    private List<Long> getOutcomeIds() {
        List<Long> outcomeIds = pap.stream().map(x -> x.getOutcomeId()).collect(Collectors.toList());
        return outcomeIds;
    }
}


