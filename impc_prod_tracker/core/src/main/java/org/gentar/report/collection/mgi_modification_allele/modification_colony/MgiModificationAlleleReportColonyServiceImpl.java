package org.gentar.report.collection.mgi_modification_allele.modification_colony;

import org.gentar.biology.gene.Gene;
import org.gentar.report.collection.mgi_modification_allele.mutation.MgiModificationAlleleReportEsCellMutationTypeProjection;
import org.gentar.report.collection.mgi_modification_allele.mutation.MgiModificationAlleleReportMutationGeneProjection;
import org.gentar.report.collection.mgi_modification_allele.mutation.MgiModificationAlleleReportMutationServiceImpl;
import org.gentar.report.collection.mgi_modification_allele.outcome.MgiModificationAlleleReportOutcomeMutationProjection;
import org.gentar.report.collection.mgi_modification_allele.outcome.MgiModificationAlleleReportOutcomeService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MgiModificationAlleleReportColonyServiceImpl implements MgiModificationAlleleReportColonyService{

    private final MgiModificationAlleleReportColonyRepository mgiModificationAlleleReportColonyRepository;
    private final MgiModificationAlleleReportOutcomeService mgiModificationAlleleReportOutcomeService;
    private final MgiModificationAlleleReportMutationServiceImpl mutationReportService;


    private List<MgiModificationAlleleReportColonyProjection> cp;
    private Map<Long, MgiModificationAlleleReportOutcomeMutationProjection> filteredOutcomeMutationMap;
    private Map<Long, MgiModificationAlleleReportOutcomeMutationProjection> filteredProductionOutcomeMutationMap;

    MgiModificationAlleleReportColonyServiceImpl(MgiModificationAlleleReportColonyRepository mgiModificationAlleleReportColonyRepository,
                                                 MgiModificationAlleleReportOutcomeService mgiModificationAlleleReportOutcomeService,
                                                 MgiModificationAlleleReportMutationServiceImpl mutationReportService) {
        this.mgiModificationAlleleReportColonyRepository = mgiModificationAlleleReportColonyRepository;
        this.mgiModificationAlleleReportOutcomeService = mgiModificationAlleleReportOutcomeService;
        this.mutationReportService = mutationReportService;
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

    @Override
    public Map<Long, MgiModificationAlleleReportOutcomeMutationProjection> getProductionMutationMap() {

        if (filteredProductionOutcomeMutationMap != null) {
            return filteredProductionOutcomeMutationMap;
        }

        List<Long> outcomeIds = getProductionOutcomeIds();

        List<MgiModificationAlleleReportOutcomeMutationProjection> omp =
                mgiModificationAlleleReportOutcomeService.getSelectedOutcomeMutationProjections(outcomeIds);

        Map<Long, Set<MgiModificationAlleleReportOutcomeMutationProjection>> outcomeMutationMap = omp
                .stream()
                .collect(Collectors.groupingBy(
                        MgiModificationAlleleReportOutcomeMutationProjection::getOutcomeId,
                        Collectors.mapping(entry -> entry, Collectors.toSet())));

        // select outcomes associated with only one mutation - compatible with existing MGI iMits report
        filteredProductionOutcomeMutationMap = outcomeMutationMap
                .entrySet()
                .stream()
                .filter(e -> e.getValue().size() == 1)
                .collect(Collectors.toMap(Map.Entry::getKey, e -> List.copyOf(e.getValue()).get(0)));

        return filteredProductionOutcomeMutationMap;
    }

    @Override
    public Map<Long, Gene> getGeneMap() {
        getMutationMap();
        List<Long> filteredMutationIds = getFilteredMutationIds();
        List<MgiModificationAlleleReportMutationGeneProjection> mgp =
                mutationReportService.getSelectedMutationGeneProjections(filteredMutationIds);

        Map<Long, Set<Gene>> mutationGeneMap = mgp
                .stream()
                .collect(Collectors.groupingBy(
                        MgiModificationAlleleReportMutationGeneProjection::getMutationId,
                        Collectors.mapping(MgiModificationAlleleReportMutationGeneProjection::getGene, Collectors.toSet())));

        // select mutations associated with only one gene - compatible with existing MGI iMits report
        return mutationGeneMap
                .entrySet()
                .stream()
                .filter(e -> e.getValue().size() == 1)
                .collect(Collectors.toMap(map -> map.getKey(), map -> List.copyOf(map.getValue()).get(0)));
    }

    @Override
    public Map<Long, String> getAlleleCategoryMap() {
        getMutationMap();
        List<Long> filteredMutationIds = getFilteredMutationIds();
        List<MgiModificationAlleleReportMutationGeneProjection> mgp =
                mutationReportService.getSelectedMutationGeneProjections(filteredMutationIds);
        List<MgiModificationAlleleReportEsCellMutationTypeProjection> mtp =
                mutationReportService.getSelectedEsCellMutationTypeProjections(mgp);
        // mtp.stream().forEach(x -> System.out.println(x.getMutationIdentificationNumber() + "\t" + x.getMutationCategorizationName()));
        return mutationReportService.assignAlleleCategories(mtp);
    }


    private List<Long> getOutcomeIds() {
        return cp.stream()
                .map(MgiModificationAlleleReportColonyProjection::getModificationOutcomeId)
                .collect(Collectors.toList());
    }


    private List<Long> getProductionOutcomeIds() {
        return cp.stream()
                .map(MgiModificationAlleleReportColonyProjection::getProductionOutcomeId)
                .collect(Collectors.toList());
    }

    private List<Long> getFilteredMutationIds() {
        return filteredOutcomeMutationMap
                .entrySet()
                .stream()
                .map(e -> e.getValue().getMutationId())
                .collect(Collectors.toList());
    }

    private List<Long> getFilteredProductionMutationIds() {
        return filteredProductionOutcomeMutationMap
                .entrySet()
                .stream()
                .map(e -> e.getValue().getMutationId())
                .collect(Collectors.toList());
    }

}
