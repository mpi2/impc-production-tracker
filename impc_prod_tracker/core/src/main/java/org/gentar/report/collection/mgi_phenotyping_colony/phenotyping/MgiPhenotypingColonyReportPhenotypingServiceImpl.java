package org.gentar.report.collection.mgi_phenotyping_colony.phenotyping;

import org.gentar.biology.gene.Gene;
import org.gentar.report.collection.mgi_phenotyping_colony.phenotyping.mutation.MgiPhenotypingColonyReportMutationGeneProjection;
import org.gentar.report.collection.mgi_phenotyping_colony.phenotyping.mutation.MgiPhenotypingColonyReportMutationServiceImpl;
import org.gentar.report.collection.mgi_phenotyping_colony.phenotyping.outcome.MgiPhenotypingColonyReportOutcomeMutationProjection;
import org.gentar.report.collection.mgi_phenotyping_colony.phenotyping.outcome.MgiPhenotypingColonyReportOutcomeServiceImpl;
import org.gentar.report.collection.mgi_phenotyping_colony.phenotyping.phenotyping_attempt.MgiPhenotypingColonyReportPhenotypingAttemptProjection;
import org.gentar.report.collection.mgi_phenotyping_colony.phenotyping.phenotyping_attempt.MgiPhenotypingColonyReportPhenotypingAttemptServiceImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MgiPhenotypingColonyReportPhenotypingServiceImpl implements MgiPhenotypingColonyReportPhenotypingService{

    private final MgiPhenotypingColonyReportPhenotypingAttemptServiceImpl phenotypingAttemptService;
    private final MgiPhenotypingColonyReportOutcomeServiceImpl outcomeReportService;
    private final MgiPhenotypingColonyReportMutationServiceImpl mutationReportService;

    List<MgiPhenotypingColonyReportPhenotypingAttemptProjection> pap;
    Map<Long, MgiPhenotypingColonyReportOutcomeMutationProjection> filteredOutcomeMutationMap;

    MgiPhenotypingColonyReportPhenotypingServiceImpl(
            MgiPhenotypingColonyReportPhenotypingAttemptServiceImpl phenotypingAttemptService,
            MgiPhenotypingColonyReportOutcomeServiceImpl outcomeReportService,
            MgiPhenotypingColonyReportMutationServiceImpl mutationReportService)
    {
        this.phenotypingAttemptService = phenotypingAttemptService;
        this.outcomeReportService = outcomeReportService;
        this.mutationReportService = mutationReportService;
    }

    @Override
    public Map<Long, MgiPhenotypingColonyReportOutcomeMutationProjection> getMutationMap() {
        List<Long> outcomeIds = getOutcomeIds();
        List<MgiPhenotypingColonyReportOutcomeMutationProjection> omp =
                outcomeReportService.getSelectedOutcomeMutationProjections(outcomeIds);

        Map<Long, Set<MgiPhenotypingColonyReportOutcomeMutationProjection>> outcomeMutationMap =
                omp
                        .stream()
                        .collect(Collectors.groupingBy(
                                MgiPhenotypingColonyReportOutcomeMutationProjection::getOutcomeId,
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
        List<MgiPhenotypingColonyReportMutationGeneProjection> mgp =
                mutationReportService.getSelectedMutationGeneProjections(filteredMutationIds);

        Map<Long, Set<Gene>> mutationGeneMap = mgp
                .stream()
                .collect(Collectors.groupingBy(
                        MgiPhenotypingColonyReportMutationGeneProjection::getMutationId,
                        Collectors.mapping(MgiPhenotypingColonyReportMutationGeneProjection::getGene, Collectors.toSet())));

        // select mutations associated with only one gene - compatible with existing MGI iMits report
        return mutationGeneMap
                .entrySet()
                .stream()
                .filter(e -> e.getValue().size() == 1)
                .collect(Collectors.toMap(map -> map.getKey(), map -> List.copyOf(map.getValue()).get(0)));
    }

    @Override
    public List<MgiPhenotypingColonyReportPhenotypingAttemptProjection> getPhenotypingColonyReportPhenotypingAttemptProjections() {
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
