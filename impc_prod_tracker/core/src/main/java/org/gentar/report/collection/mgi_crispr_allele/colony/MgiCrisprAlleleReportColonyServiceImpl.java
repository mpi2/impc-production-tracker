package org.gentar.report.collection.mgi_crispr_allele.colony;

import org.gentar.biology.gene.Gene;
import org.gentar.report.collection.mgi_crispr_allele.genotype_primer.MgiCrisprAlleleReportGenotypePrimerProjection;
import org.gentar.report.collection.mgi_crispr_allele.genotype_primer.MgiCrisprAlleleReportGenotypePrimerService;
import org.gentar.report.collection.mgi_crispr_allele.guide.MgiCrisprAlleleReportGuideProjection;
import org.gentar.report.collection.mgi_crispr_allele.guide.MgiCrisprAlleleReportGuideServiceImpl;
import org.gentar.report.collection.mgi_crispr_allele.mutagenesis_donor.MgiCrisprAlleleReportMutagenesisDonorProjection;
import org.gentar.report.collection.mgi_crispr_allele.mutagenesis_donor.MgiCrisprAlleleReportMutagenesisDonorService;
import org.gentar.report.collection.mgi_crispr_allele.mutation.MgiCrisprAlleleReportMutationGeneProjection;
import org.gentar.report.collection.mgi_crispr_allele.mutation.MgiCrisprAlleleReportMutationServiceImpl;
import org.gentar.report.collection.mgi_crispr_allele.mutation_characterization.MgiCrisprAlleleReportMutationCategorizationProjection;
import org.gentar.report.collection.mgi_crispr_allele.mutation_characterization.MgiCrisprAlleleReportMutationCategorizationServiceImpl;
import org.gentar.report.collection.mgi_crispr_allele.nuclease.MgiCrisprAlleleReportNucleaseProjection;
import org.gentar.report.collection.mgi_crispr_allele.nuclease.MgiCrisprAlleleReportNucleaseServiceImpl;
import org.gentar.report.collection.mgi_crispr_allele.outcome.MgiCrisprAlleleReportOutcomeMutationProjection;
import org.gentar.report.collection.mgi_crispr_allele.outcome.MgiCrisprAlleleReportOutcomeServiceImpl;
import org.gentar.report.collection.mgi_crispr_allele.sequence.MgiCrisprAlleleReportMutationSequenceProjection;
import org.gentar.report.collection.mgi_crispr_allele.sequence.MgiCrisprAlleleReportMutationSequenceServiceImpl;
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
    private final MgiCrisprAlleleReportGuideServiceImpl guideReportService;
    private final MgiCrisprAlleleReportNucleaseServiceImpl nucleaseReportService;
    private final MgiCrisprAlleleReportMutagenesisDonorService mutagenesisDonorReportService;
    private final MgiCrisprAlleleReportGenotypePrimerService genotypePrimerReportService;
    private final MgiCrisprAlleleReportMutationSequenceServiceImpl mutationSequenceReportService;
    private final MgiCrisprAlleleReportMutationCategorizationServiceImpl mutationCategorizationReportService;

    private List<MgiCrisprAlleleReportColonyProjection> cp;
    private Map<Long, MgiCrisprAlleleReportOutcomeMutationProjection> filteredOutcomeMutationMap;


    public MgiCrisprAlleleReportColonyServiceImpl(MgiCrisprAlleleReportColonyRepository mgiCrisprAlleleReportColonyRepository,
                                                  MgiCrisprAlleleReportOutcomeServiceImpl outcomeReportService,
                                                  MgiCrisprAlleleReportMutationServiceImpl mutationReportService,
                                                  MgiCrisprAlleleReportGuideServiceImpl guideReportService,
                                                  MgiCrisprAlleleReportNucleaseServiceImpl nucleaseReportService,
                                                  MgiCrisprAlleleReportMutagenesisDonorService mutagenesisDonorReportService,
                                                  MgiCrisprAlleleReportGenotypePrimerService genotypePrimerReportService,
                                                  MgiCrisprAlleleReportMutationSequenceServiceImpl mutationSequenceReportService,
                                                  MgiCrisprAlleleReportMutationCategorizationServiceImpl mutationCategorizationReportService)
    {
        this.mgiCrisprAlleleReportColonyRepository = mgiCrisprAlleleReportColonyRepository;
        this.outcomeReportService = outcomeReportService;
        this.mutationReportService = mutationReportService;
        this.guideReportService = guideReportService;
        this.nucleaseReportService = nucleaseReportService;
        this.mutagenesisDonorReportService = mutagenesisDonorReportService;
        this.genotypePrimerReportService = genotypePrimerReportService;
        this.mutationSequenceReportService = mutationSequenceReportService;
        this.mutationCategorizationReportService = mutationCategorizationReportService;
    }

    @Override
    public List<MgiCrisprAlleleReportColonyProjection> getAllColonyReportProjections() {
        cp = mgiCrisprAlleleReportColonyRepository.findAllColonyReportProjections();
        return cp;
    }

    @Override
    public Map<Long, MgiCrisprAlleleReportOutcomeMutationProjection> getMutationMap() {

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
                .collect(Collectors.toMap(Map.Entry::getKey, e -> List.copyOf(e.getValue()).getFirst()));

        return filteredOutcomeMutationMap;
    }

    @Override
    public Map<Long, Set<MgiCrisprAlleleReportGuideProjection>> getGuideMap() {
        List<Long> planIds = getPlanIds();

        List<MgiCrisprAlleleReportGuideProjection> guideProjections = guideReportService.getSelectedGuideProjections(planIds);

        return guideProjections
                .stream()
                .collect(Collectors.groupingBy(
                        MgiCrisprAlleleReportGuideProjection::getPlanId,
                        Collectors.mapping(entry -> entry, Collectors.toSet())));
    }

    @Override
    public Map<Long, Set<MgiCrisprAlleleReportNucleaseProjection>> getNucleaseMap() {
        List<Long> planIds = getPlanIds();

        List<MgiCrisprAlleleReportNucleaseProjection> nucleaseProjections = nucleaseReportService.getSelectedNucleaseProjections(planIds);

        return nucleaseProjections
                .stream()
                .collect(Collectors.groupingBy(
                        MgiCrisprAlleleReportNucleaseProjection::getPlanId,
                        Collectors.mapping(entry -> entry, Collectors.toSet())));
    }

    @Override
    public Map<Long, Set<MgiCrisprAlleleReportMutagenesisDonorProjection>> getMutagenesisDonorMap() {
        List<Long> planIds = getPlanIds();

        List<MgiCrisprAlleleReportMutagenesisDonorProjection> mutagenesisDonorProjections =
                mutagenesisDonorReportService.getSelectedMutagenesisDonorProjections(planIds);

       return mutagenesisDonorProjections
                .stream()
                .collect(Collectors.groupingBy(
                        MgiCrisprAlleleReportMutagenesisDonorProjection::getPlanId,
                        Collectors.mapping(entry -> entry, Collectors.toSet())));

    }

    @Override
    public Map<Long, Set<MgiCrisprAlleleReportGenotypePrimerProjection>> getGenotypePrimerMap() {
        List<Long> planIds = getPlanIds();

        List<MgiCrisprAlleleReportGenotypePrimerProjection> genotypePrimerProjections =
                genotypePrimerReportService.getSelectedGenotypePrimerProjections(planIds);


              return  genotypePrimerProjections
                        .stream()
                        .collect(Collectors.groupingBy(
                                MgiCrisprAlleleReportGenotypePrimerProjection::getPlanId,
                                Collectors.mapping(entry -> entry, Collectors.toSet())));

    }

    @Override
    public Map<Long, Set<MgiCrisprAlleleReportMutationSequenceProjection>> getMutationSequenceMap() {
        getMutationMap();
        List<Long> filteredMutationIds = getFilteredMutationIds();

        List<MgiCrisprAlleleReportMutationSequenceProjection> mutationSequenceProjections =
                mutationSequenceReportService.getSelectedMutationSequenceProjections(filteredMutationIds);

        return mutationSequenceProjections
                .stream()
                .collect(Collectors.groupingBy(
                        MgiCrisprAlleleReportMutationSequenceProjection::getMutationId,
                        Collectors.mapping(entry -> entry, Collectors.toSet())));

    }

    @Override
    public Map<Long, Set<MgiCrisprAlleleReportMutationCategorizationProjection>> getMutationCategorizationMap() {
        getMutationMap();
        List<Long> filteredMutationIds = getFilteredMutationIds();

        List<MgiCrisprAlleleReportMutationCategorizationProjection> mutationCategorizationProjections =
                mutationCategorizationReportService
                        .getSelectedMutationCategorizationProjections(filteredMutationIds);

              return  mutationCategorizationProjections
                .stream()
                .collect(Collectors.groupingBy(
                        MgiCrisprAlleleReportMutationCategorizationProjection::getMutationId,
                        Collectors.mapping(entry -> entry, Collectors.toSet())));

    }

    @Override
    public Map<Long, Gene> getGeneMap() {
        getMutationMap();
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
                .collect(Collectors.toMap(Map.Entry::getKey, map -> List.copyOf(map.getValue()).getFirst()));
    }

    private List<Long> getOutcomeIds() {
        return cp.stream()
                .map(MgiCrisprAlleleReportColonyProjection::getOutcomeId)
                .collect(Collectors.toList());
    }

    private List<Long> getPlanIds() {
        return cp.stream()
                .map(MgiCrisprAlleleReportColonyProjection::getPlanId)
                .collect(Collectors.toList());
    }

    private List<Long> getFilteredMutationIds() {
        return filteredOutcomeMutationMap
                .values()
                .stream()
                .map(MgiCrisprAlleleReportOutcomeMutationProjection::getMutationId)
                .collect(Collectors.toList());
    }

}
