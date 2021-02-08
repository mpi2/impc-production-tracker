package org.gentar.report.collection.common.phenotyping;

import org.gentar.biology.gene.Gene;
import org.gentar.report.collection.common.phenotyping.mutation.CommonPhenotypingColonyReportMutationGeneProjection;
import org.gentar.report.collection.common.phenotyping.mutation.CommonPhenotypingColonyReportMutationServiceImpl;
import org.gentar.report.collection.common.phenotyping.outcome.CommonPhenotypingColonyReportOutcomeMutationProjection;
import org.gentar.report.collection.common.phenotyping.outcome.CommonPhenotypingColonyReportOutcomeServiceImpl;
import org.gentar.report.collection.common.phenotyping.phenotyping_attempt.CommonPhenotypingColonyReportPhenotypingAttemptProjection;
import org.gentar.report.collection.common.phenotyping.phenotyping_attempt.CommonPhenotypingColonyReportPhenotypingAttemptServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CommonPhenotypingColonyReportServiceImpl implements CommonPhenotypingColonyReportService {
    private final CommonPhenotypingColonyReportPhenotypingAttemptServiceImpl phenotypingAttemptService;
    private final CommonPhenotypingColonyReportOutcomeServiceImpl outcomeReportService;
    private final CommonPhenotypingColonyReportMutationServiceImpl mutationReportService;

    List<CommonPhenotypingColonyReportPhenotypingAttemptProjection> pap;
    Map<Long, CommonPhenotypingColonyReportOutcomeMutationProjection> filteredOutcomeMutationMap;

    public CommonPhenotypingColonyReportServiceImpl(CommonPhenotypingColonyReportPhenotypingAttemptServiceImpl phenotypingAttemptService,
                                                    CommonPhenotypingColonyReportOutcomeServiceImpl outcomeReportService,
                                                    CommonPhenotypingColonyReportMutationServiceImpl mutationReportService)
    {
        this.phenotypingAttemptService = phenotypingAttemptService;
        this.outcomeReportService = outcomeReportService;
        this.mutationReportService = mutationReportService;
    }

    @Override
    public Map<Long, CommonPhenotypingColonyReportOutcomeMutationProjection> getMutationMap() {
        List<Long> outcomeIds = getOutcomeIds();
        List<CommonPhenotypingColonyReportOutcomeMutationProjection> omp = outcomeReportService.getSelectedOutcomeMutationProjections(outcomeIds);
        Map<Long, Set<CommonPhenotypingColonyReportOutcomeMutationProjection>> outcomeMutationMap =
                omp
                        .stream()
                        .collect(Collectors.groupingBy(
                                CommonPhenotypingColonyReportOutcomeMutationProjection::getOutcomeId,
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
        List<CommonPhenotypingColonyReportMutationGeneProjection> mgp =
                mutationReportService.getSelectedMutationGeneProjections(filteredMutationIds);

        Map<Long, Set<Gene>> mutationGeneMap = mgp
                .stream()
                .collect(Collectors.groupingBy(
                        CommonPhenotypingColonyReportMutationGeneProjection::getMutationId,
                        Collectors.mapping(CommonPhenotypingColonyReportMutationGeneProjection::getGene, Collectors.toSet())));

        // select mutations associated with only one gene - compatible with existing MGI iMits report
        return mutationGeneMap
                .entrySet()
                .stream()
                .filter(e -> e.getValue().size() == 1)
                .collect(Collectors.toMap(map -> map.getKey(), map -> List.copyOf(map.getValue()).get(0)));
    }

    @Override
    public List<CommonPhenotypingColonyReportPhenotypingAttemptProjection> getPhenotypingColonyReportPhenotypingAttemptProjections() {
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
