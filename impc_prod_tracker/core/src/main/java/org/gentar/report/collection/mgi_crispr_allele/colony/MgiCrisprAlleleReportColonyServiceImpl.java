package org.gentar.report.collection.mgi_crispr_allele.colony;

import org.gentar.biology.gene.Gene;
import org.gentar.report.collection.mgi_crispr_allele.mutation.MgiCrisprAlleleReportMutationGeneProjection;
import org.gentar.report.collection.mgi_crispr_allele.mutation.MgiCrisprAlleleReportMutationServiceImpl;
import org.gentar.report.collection.mgi_crispr_allele.outcome.MgiCrisprAlleleReportOutcomeMutationProjection;
import org.gentar.report.collection.mgi_crispr_allele.outcome.MgiCrisprAlleleReportOutcomeServiceImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MgiCrisprAlleleReportColonyServiceImpl implements MgiCrisprAlleleReportColonyService {

    private final MgiCrisprAlleleReportColonyRepository mgiCrisprAlleleReportColonyRepository;
    private final MgiCrisprAlleleReportOutcomeServiceImpl outcomeReportService;
    private final MgiCrisprAlleleReportMutationServiceImpl mutationReportService;


    private List<MgiCrisprAlleleReportColonyProjection> cp;
    private Map<Long, MgiCrisprAlleleReportOutcomeMutationProjection> filteredOutcomeMutationMap;


    public MgiCrisprAlleleReportColonyServiceImpl(MgiCrisprAlleleReportColonyRepository mgiCrisprAlleleReportColonyRepository,
                                                  MgiCrisprAlleleReportOutcomeServiceImpl outcomeReportService,
                                                  MgiCrisprAlleleReportMutationServiceImpl mutationReportService) {
        this.mgiCrisprAlleleReportColonyRepository = mgiCrisprAlleleReportColonyRepository;
        this.outcomeReportService = outcomeReportService;
        this.mutationReportService = mutationReportService;
    }

    @Override
    public List<MgiCrisprAlleleReportColonyProjection> getAllColonyReportProjections() {
        cp = mgiCrisprAlleleReportColonyRepository.findAllColonyReportProjections();
        return cp;
    }

    @Override
    public Map<Long, MgiCrisprAlleleReportOutcomeMutationProjection> getMutationMap() {
        List<Long> outcomeIds = getOutcomeIds();

        List<MgiCrisprAlleleReportOutcomeMutationProjection> omp = outcomeReportService.getSelectedOutcomeMutationCrisprReportProjections(outcomeIds);
        Map<Long, Set<MgiCrisprAlleleReportOutcomeMutationProjection>> outcomeMutationMap = omp
                .stream()
                .collect(Collectors.groupingBy(
                        MgiCrisprAlleleReportOutcomeMutationProjection::getOutcomeId,
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
        List<MgiCrisprAlleleReportMutationGeneProjection> mgp =
                mutationReportService.getSelectedMutationGeneProjections(filteredMutationIds);

        Map<Long, Set<Gene>> mutationGeneMap = mgp
                .stream()
                .collect(Collectors.groupingBy(
                        MgiCrisprAlleleReportMutationGeneProjection::getMutationId,
                        Collectors.mapping(MgiCrisprAlleleReportMutationGeneProjection::getGene, Collectors.toSet())));

        // select mutations associated with only one gene - compatible with existing MGI iMits report
        return mutationGeneMap
                .entrySet()
                .stream()
                .filter(e -> e.getValue().size() == 1)
                .collect(Collectors.toMap(map -> map.getKey(), map -> List.copyOf(map.getValue()).get(0)));
    }

    private List<Long> getOutcomeIds() {
        return cp.stream()
                .map(x -> x.getOutcomeId())
                .collect(Collectors.toList());
    }

    private List<Long> getFilteredMutationIds() {
        return filteredOutcomeMutationMap
                .entrySet()
                .stream()
                .map(e -> e.getValue().getMutationId())
                .collect(Collectors.toList());
    }

}
