package org.gentar.report.collection.mgi_modification_allele.modification_colony;

import org.gentar.report.collection.mgi_crispr_allele.colony.MgiCrisprAlleleReportColonyProjection;
import org.gentar.report.collection.mgi_modification_allele.outcome.MgiModificationAlleleReportOutcomeMutationProjection;
import org.gentar.report.collection.mgi_modification_allele.outcome.MgiModificationAlleleReportOutcomeService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
class MgiModificationAlleleReportColonyServiceImpl implements MgiModificationAlleleReportColonyService{

    private final MgiModificationAlleleReportColonyRepository mgiModificationAlleleReportColonyRepository;
    private final MgiModificationAlleleReportOutcomeService mgiModificationAlleleReportOutcomeService;

    private List<MgiModificationAlleleReportColonyProjection> cp;
    private Map<Long, MgiModificationAlleleReportOutcomeMutationProjection> filteredOutcomeMutationMap;

    MgiModificationAlleleReportColonyServiceImpl(MgiModificationAlleleReportColonyRepository mgiModificationAlleleReportColonyRepository,
                                                 MgiModificationAlleleReportOutcomeService mgiModificationAlleleReportOutcomeService) {
        this.mgiModificationAlleleReportColonyRepository = mgiModificationAlleleReportColonyRepository;
        this.mgiModificationAlleleReportOutcomeService = mgiModificationAlleleReportOutcomeService;
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

        List<MgiModificationAlleleReportOutcomeMutationProjection> omp =
                mgiModificationAlleleReportOutcomeService.getSelectedOutcomeMutationProjections(outcomeIds);
        Map<Long, Set<MgiModificationAlleleReportOutcomeMutationProjection>> outcomeMutationMap = omp
                .stream()
                .collect(Collectors.groupingBy(
                        MgiModificationAlleleReportOutcomeMutationProjection::getOutcomeId,
                        Collectors.mapping(entry -> entry, Collectors.toSet())));

        // select outcomes associated with only one mutation - compatible with existing MGI iMits report
        filteredOutcomeMutationMap = outcomeMutationMap
                .entrySet()
                .stream()
                .filter(e -> e.getValue().size() == 1)
                .collect(Collectors.toMap(Map.Entry::getKey, e -> List.copyOf(e.getValue()).get(0)));

        return filteredOutcomeMutationMap;
    }
    private List<Long> getOutcomeIds() {
        return cp.stream()
                .map(MgiModificationAlleleReportColonyProjection::getModificationOutcomeId)
                .collect(Collectors.toList());
    }

}
