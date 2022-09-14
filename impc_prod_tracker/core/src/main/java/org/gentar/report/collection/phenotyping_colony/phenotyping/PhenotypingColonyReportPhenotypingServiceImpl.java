package org.gentar.report.collection.phenotyping_colony.phenotyping;

import org.gentar.biology.gene.Gene;
import org.gentar.report.collection.phenotyping_colony.phenotyping.mutation.PhenotypingColonyReportMutationGeneProjection;
import org.gentar.report.collection.phenotyping_colony.phenotyping.mutation.PhenotypingColonyReportMutationServiceImpl;
import org.gentar.report.collection.phenotyping_colony.phenotyping.outcome.PhenotypingColonyReportOutcomeMutationProjection;
import org.gentar.report.collection.phenotyping_colony.phenotyping.outcome.PhenotypingColonyReportOutcomeServiceImpl;
import org.gentar.report.collection.phenotyping_colony.phenotyping.phenotyping_attempt.PhenotypingColonyReportPhenotypingAttemptProjection;
import org.gentar.report.collection.phenotyping_colony.phenotyping.phenotyping_attempt.PhenotypingColonyReportPhenotypingAttemptServiceImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PhenotypingColonyReportPhenotypingServiceImpl implements PhenotypingColonyReportPhenotypingService {
    private final PhenotypingColonyReportPhenotypingAttemptServiceImpl phenotypingAttemptService;
    private final PhenotypingColonyReportOutcomeServiceImpl outcomeReportService;
    private final PhenotypingColonyReportMutationServiceImpl mutationReportService;

    List<PhenotypingColonyReportPhenotypingAttemptProjection> pap;
    Map<Long, PhenotypingColonyReportOutcomeMutationProjection> filteredOutcomeMutationMap;

    public PhenotypingColonyReportPhenotypingServiceImpl(PhenotypingColonyReportPhenotypingAttemptServiceImpl phenotypingAttemptService,
                                                         PhenotypingColonyReportOutcomeServiceImpl outcomeReportService,
                                                         PhenotypingColonyReportMutationServiceImpl mutationReportService)
    {
        this.phenotypingAttemptService = phenotypingAttemptService;
        this.outcomeReportService = outcomeReportService;
        this.mutationReportService = mutationReportService;
    }

    @Override
    public Map<Long, PhenotypingColonyReportOutcomeMutationProjection> getMutationMap() {
        List<Long> outcomeIds = getOutcomeIds();
        List<PhenotypingColonyReportOutcomeMutationProjection> omp = outcomeReportService.getSelectedOutcomeMutationProjections(outcomeIds);
        Map<Long, Set<PhenotypingColonyReportOutcomeMutationProjection>> outcomeMutationMap =
                omp
                        .stream()
                        .collect(Collectors.groupingBy(
                                PhenotypingColonyReportOutcomeMutationProjection::getOutcomeId,
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
        List<PhenotypingColonyReportMutationGeneProjection> mgp =
                mutationReportService.getSelectedMutationGeneProjections(filteredMutationIds);

        Map<Long, Set<Gene>> mutationGeneMap = mgp
                .stream()
                .collect(Collectors.groupingBy(
                        PhenotypingColonyReportMutationGeneProjection::getMutationId,
                        Collectors.mapping(PhenotypingColonyReportMutationGeneProjection::getGene, Collectors.toSet())));

        // select mutations associated with only one gene - compatible with existing MGI iMits report
        return mutationGeneMap
                .entrySet()
                .stream()
                .filter(e -> e.getValue().size() == 1)
                .collect(Collectors.toMap(map -> map.getKey(), map -> List.copyOf(map.getValue()).get(0)));
    }

    @Override
    public List<PhenotypingColonyReportPhenotypingAttemptProjection> getPhenotypingColonyReportPhenotypingAttemptProjections() {
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
