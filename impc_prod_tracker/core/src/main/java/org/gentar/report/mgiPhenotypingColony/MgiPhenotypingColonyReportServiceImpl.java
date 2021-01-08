package org.gentar.report.mgiPhenotypingColony;

import org.gentar.biology.gene.Gene;
import org.gentar.report.mgiPhenotypingColony.mutation.MgiPhenotypingColonyReportMutationGeneProjection;
import org.gentar.report.mgiPhenotypingColony.mutation.MgiPhenotypingColonyReportMutationServiceImpl;
import org.gentar.report.mgiPhenotypingColony.outcome.MgiPhenotypingColonyReportOutcomeMutationProjection;
import org.gentar.report.mgiPhenotypingColony.outcome.MgiPhenotypingColonyReportOutcomeServiceImpl;
import org.gentar.report.mgiPhenotypingColony.phenotypingAttempt.MgiPhenotypingColonyReportPhenotypingAttemptProjection;
import org.gentar.report.mgiPhenotypingColony.phenotypingAttempt.MgiPhenotypingColonyReportPhenotypingAttemptServiceImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MgiPhenotypingColonyReportServiceImpl implements MgiPhenotypingColonyReportService {

    private final MgiPhenotypingColonyReportPhenotypingAttemptServiceImpl phenotypingAttemptService;
    private final MgiPhenotypingColonyReportOutcomeServiceImpl outcomeReportService;
    private final MgiPhenotypingColonyReportMutationServiceImpl mutationReportService;

    public MgiPhenotypingColonyReportServiceImpl( MgiPhenotypingColonyReportPhenotypingAttemptServiceImpl phenotypingAttemptService,
                                                  MgiPhenotypingColonyReportOutcomeServiceImpl outcomeReportService,
                                                  MgiPhenotypingColonyReportMutationServiceImpl mutationReportService ) {
        this.phenotypingAttemptService = phenotypingAttemptService;
        this.outcomeReportService = outcomeReportService;
        this.mutationReportService = mutationReportService;
    }


    public void generateMgiPhenotypingColonyReport() {

        List<MgiPhenotypingColonyReportPhenotypingAttemptProjection> pap = phenotypingAttemptService.getPhenotypingAttemptProjectionsForMgi();
        List<Long> outcomeIds = pap.stream().map(x -> x.getOutcomeId()).collect(Collectors.toList());

        List<MgiPhenotypingColonyReportOutcomeMutationProjection> omp = outcomeReportService.getSelectedOutcomeMutationProjections(outcomeIds);
        Map<Long, Set<MgiPhenotypingColonyReportOutcomeMutationProjection>> outcomeMutationMap = omp.stream().collect(Collectors.groupingBy(MgiPhenotypingColonyReportOutcomeMutationProjection::getOutcomeId, Collectors.mapping(entry -> entry, Collectors.toSet())));

        // select outcomes associated with only one mutation - compatible with existing MGI iMits report
        Map<Long, MgiPhenotypingColonyReportOutcomeMutationProjection> filteredOutcomeMutationMap = outcomeMutationMap.entrySet().stream().filter(e -> e.getValue().size() == 1).collect(Collectors.toMap(Map.Entry::getKey, e -> List.copyOf(e.getValue()).get(0)));
        List<Long> filteredMutationIds = filteredOutcomeMutationMap.entrySet().stream().map(e -> e.getValue().getMutationId()).collect(Collectors.toList());

        // Check filteredOutcomeMutationMap contents
        // filteredOutcomeMutationMap.entrySet().stream().forEach(e -> System.out.println(e.getKey() + "\t" + e.getValue().getSymbol()));

        List<MgiPhenotypingColonyReportMutationGeneProjection> mgp = mutationReportService.getSelectedMutationGeneProjections(filteredMutationIds);
        Map<Long, Set<Gene>> mutationGeneMap = mgp.stream().collect(Collectors.groupingBy(MgiPhenotypingColonyReportMutationGeneProjection::getMutationId, Collectors.mapping(MgiPhenotypingColonyReportMutationGeneProjection::getGene, Collectors.toSet())));

        // select mutations associated with only one gene - compatible with existing MGI iMits report
        Map<Long, Gene> filteredMutationGeneMap = mutationGeneMap.entrySet().stream().filter(e -> e.getValue().size() == 1).collect(Collectors.toMap(map -> map.getKey(), map -> List.copyOf(map.getValue()).get(0)));


        pap.stream()
                .filter(x -> filteredOutcomeMutationMap.containsKey(x.getOutcomeId()))
                .filter(x -> filteredMutationGeneMap.containsKey(filteredOutcomeMutationMap.get(x.getOutcomeId()).getMutationId()))
                .forEach(x -> {
                    String mutationSymbol = filteredOutcomeMutationMap.get(x.getOutcomeId()).getSymbol();
                    Gene g = filteredMutationGeneMap.get(filteredOutcomeMutationMap.get(x.getOutcomeId()).getMutationId());
                    String cohortProductionWorkUnit = x.getCohortProductionWorkUnit() == null ? x.getPhenotypingWorkUnit() : x.getCohortProductionWorkUnit();
                    System.out.println(
                            g.getSymbol() + "\t" +
                                    g.getAccId() + "\t" +
                                    x.getColonyName() + "\t" +
                                    "\t" +
                                    x.getStrainName() + "\t" +
                                    x.getStrainAccId() + "\t" +
                                    x.getProductionWorkUnit() + "\t" +
                                    x.getProductionWorkGroup() + "\t" +
                                    x.getPhenotypingWorkUnit() + "\t" +
                                    x.getPhenotypingWorkGroup() + "\t" +
                                    cohortProductionWorkUnit + "\t" +
                                    mutationSymbol

                    );
                });

    }
}
