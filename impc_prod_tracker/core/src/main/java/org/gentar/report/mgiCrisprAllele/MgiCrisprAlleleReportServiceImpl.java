package org.gentar.report.mgiCrisprAllele;

import org.gentar.biology.gene.Gene;
import org.gentar.report.mgiCrisprAllele.colony.MgiCrisprAlleleReportColonyProjection;
import org.gentar.report.mgiCrisprAllele.colony.MgiCrisprAlleleReportColonyServiceImpl;
import org.gentar.report.mgiCrisprAllele.mutation.MgiCrisprAlleleReportMutationGeneProjection;
import org.gentar.report.mgiCrisprAllele.mutation.MgiCrisprAlleleReportMutationServiceImpl;
import org.gentar.report.mgiCrisprAllele.outcome.MgiCrisprAlleleReportOutcomeMutationProjection;
import org.gentar.report.mgiCrisprAllele.outcome.MgiCrisprAlleleReportOutcomeServiceImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MgiCrisprAlleleReportServiceImpl implements MgiCrisprAlleleReportService {

    private final MgiCrisprAlleleReportColonyServiceImpl colonyReportService;
    private final MgiCrisprAlleleReportOutcomeServiceImpl outcomeReportService;
    private final MgiCrisprAlleleReportMutationServiceImpl mutationReportService;

    public MgiCrisprAlleleReportServiceImpl( MgiCrisprAlleleReportColonyServiceImpl colonyReportService,
                                             MgiCrisprAlleleReportOutcomeServiceImpl outcomeReportService,
                                             MgiCrisprAlleleReportMutationServiceImpl mutationReportService ) {
        this.colonyReportService = colonyReportService;
        this.outcomeReportService = outcomeReportService;
        this.mutationReportService = mutationReportService;
    }

    public void generateMgiCrisprAlleleReport() {


        List<MgiCrisprAlleleReportColonyProjection> cp = colonyReportService.getAllColonyReportProjections();
        List<Long> outcomeIds = cp.stream().map(x -> x.getOutcomeId()).collect(Collectors.toList());

        List<MgiCrisprAlleleReportOutcomeMutationProjection> omp = outcomeReportService.getSelectedOutcomeMutationCrisprReportProjections(outcomeIds);
        Map<Long, Set<MgiCrisprAlleleReportOutcomeMutationProjection>> outcomeMutationMap = omp.stream().collect(Collectors.groupingBy(MgiCrisprAlleleReportOutcomeMutationProjection::getOutcomeId, Collectors.mapping(entry -> entry, Collectors.toSet())));

        // select outcomes associated with only one mutation - compatible with existing MGI iMits report
        Map<Long, MgiCrisprAlleleReportOutcomeMutationProjection> filteredOutcomeMutationMap = outcomeMutationMap.entrySet().stream().filter(e -> e.getValue().size() == 1).collect(Collectors.toMap(Map.Entry::getKey, e -> List.copyOf(e.getValue()).get(0)));
        List<Long> filteredMutationIds = filteredOutcomeMutationMap.entrySet().stream().map(e -> e.getValue().getMutationId()).collect(Collectors.toList());

        // Check filteredOutcomeMutationMap contents
        // filteredOutcomeMutationMap.entrySet().stream().forEach(e -> System.out.println(e.getKey() + "\t" + e.getValue().getSymbol() + "\t" + e.getValue().getMutationType()));

        List<MgiCrisprAlleleReportMutationGeneProjection> mgp = mutationReportService.getSelectedMutationGeneProjections(filteredMutationIds);
        Map<Long, Set<Gene>> mutationGeneMap = mgp.stream().collect(Collectors.groupingBy(MgiCrisprAlleleReportMutationGeneProjection::getMutationId, Collectors.mapping(MgiCrisprAlleleReportMutationGeneProjection::getGene, Collectors.toSet())));

        // select mutations associated with only one gene - compatible with existing MGI iMits report
        Map<Long, Gene> filteredMutationGeneMap = mutationGeneMap.entrySet().stream().filter(e -> e.getValue().size() == 1).collect(Collectors.toMap(map -> map.getKey(), map -> List.copyOf(map.getValue()).get(0)));


        cp.stream()
                .filter(x -> filteredOutcomeMutationMap.containsKey(x.getOutcomeId()))
                .filter(x -> filteredMutationGeneMap.containsKey(filteredOutcomeMutationMap.get(x.getOutcomeId()).getMutationId()))
                .forEach(x -> {
                    MgiCrisprAlleleReportOutcomeMutationProjection m = filteredOutcomeMutationMap.get(x.getOutcomeId());
                    Gene g = filteredMutationGeneMap.get(filteredOutcomeMutationMap.get(x.getOutcomeId()).getMutationId());
                    System.out.println(
                            g.getSymbol() + "\t" +
                                    g.getAccId() + "\t" +
                                    x.getStrainName() + "\t" +
                                    x.getColonyName() + "\t" +
                                    x.getStrainName() + "\t" +
                                    "IMPC" + "\t" +
                                    x.getProductionWorkUnit() + "\t" +
                                    "endonuclease-mediated" + "\t" +
                                    m.getMutationType() + "\t" +
                                    m.getMutationType() + "\t" +
                                    "description" + "\t" +
                                    "allele symbol without gene symbol" + "\t" +
                                    m.getMgiAlleleAccId()


                    );
                });
    }
}
