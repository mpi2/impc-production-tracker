package org.gentar.report.collection.mgi_modification_allele.modification_colony;

import org.gentar.report.collection.mgi_crispr_allele.colony.MgiCrisprAlleleReportColonyProjection;
import org.gentar.report.collection.mgi_crispr_allele.outcome.MgiCrisprAlleleReportOutcomeMutationProjection;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
class MgiModificationAlleleReportColonyServiceImpl implements MgiModificationAlleleReportColonyService{

    private final MgiModificationAlleleReportColonyRepository mgiModificationAlleleReportColonyRepository;

    private List<MgiModificationAlleleReportColonyProjection> cp;


    MgiModificationAlleleReportColonyServiceImpl(MgiModificationAlleleReportColonyRepository mgiModificationAlleleReportColonyRepository) {
        this.mgiModificationAlleleReportColonyRepository = mgiModificationAlleleReportColonyRepository;
    }

    @Override
    public List<MgiModificationAlleleReportColonyProjection> getAllMgiModificationAlleleReportColonyProjections() {
        cp = mgiModificationAlleleReportColonyRepository.findMgiModificationAlleleReportProjections();
        return cp;
    }

    @Override
    public Map<Long, MgiModificationAlleleReportOutcomeMutationProjection> getMutationMap() {

        if (filteredOutcomeMutationMap != null) {
            return filteredOutcomeMutationMap;
        }

        List<Long> outcomeIds = getOutcomeIds();

        List<MgiCrisprAlleleReportOutcomeMutationProjection> omp = outcomeReportService.getSelectedOutcomeMutationCrisprReportProjections(outcomeIds);
        Map<Long, Set<MgiCrisprAlleleReportOutcomeMutationProjection>> outcomeMutationMap = omp
                .stream()
                .collect(Collectors.groupingBy(
                        MgiCrisprAlleleReportOutcomeMutationProjection::getOutcomeId,
                        Collectors.mapping(entry -> entry, Collectors.toSet())));

        // select outcomes associated with only one mutation - compatible with existing MGI iMits report
        filteredOutcomeMutationMap = outcomeMutationMap
                .entrySet()
                .stream()
                .filter(e -> e.getValue().size() == 1)
                .collect(Collectors.toMap(Map.Entry::getKey, e -> List.copyOf(e.getValue()).get(0)));

        return filteredOutcomeMutationMap;
    }
}
