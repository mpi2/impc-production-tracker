package org.gentar.report.collection.mgi_crispr_allele.colony;

import static org.gentar.mockdata.MockData.mgiCrisprAlleleReportColonyProjectionMockData;
import static org.gentar.mockdata.MockData.mgiCrisprAlleleReportGenotypePrimerProjectionMockData;
import static org.gentar.mockdata.MockData.mgiCrisprAlleleReportGuideProjectionMockData;
import static org.gentar.mockdata.MockData.mgiCrisprAlleleReportMutagenesisDonorProjectionMockData;
import static org.gentar.mockdata.MockData.mgiCrisprAlleleReportMutationCategorizationProjectionMockData;
import static org.gentar.mockdata.MockData.mgiCrisprAlleleReportMutationGeneProjectionMockData;
import static org.gentar.mockdata.MockData.mgiCrisprAlleleReportMutationSequenceProjectionMockData;
import static org.gentar.mockdata.MockData.mgiCrisprAlleleReportNucleaseProjectionMockData;
import static org.gentar.mockdata.MockData.mgiCrisprAlleleReportOutcomeMutationProjectionMockData;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.gentar.biology.gene.Gene;
import org.gentar.report.collection.mgi_crispr_allele.genotype_primer.MgiCrisprAlleleReportGenotypePrimerProjection;
import org.gentar.report.collection.mgi_crispr_allele.genotype_primer.MgiCrisprAlleleReportGenotypePrimerService;
import org.gentar.report.collection.mgi_crispr_allele.guide.MgiCrisprAlleleReportGuideProjection;
import org.gentar.report.collection.mgi_crispr_allele.guide.MgiCrisprAlleleReportGuideServiceImpl;
import org.gentar.report.collection.mgi_crispr_allele.mutagenesis_donor.MgiCrisprAlleleReportMutagenesisDonorProjection;
import org.gentar.report.collection.mgi_crispr_allele.mutagenesis_donor.MgiCrisprAlleleReportMutagenesisDonorService;
import org.gentar.report.collection.mgi_crispr_allele.mutation.MgiCrisprAlleleReportMutationServiceImpl;
import org.gentar.report.collection.mgi_crispr_allele.mutation_characterization.MgiCrisprAlleleReportMutationCategorizationProjection;
import org.gentar.report.collection.mgi_crispr_allele.mutation_characterization.MgiCrisprAlleleReportMutationCategorizationServiceImpl;
import org.gentar.report.collection.mgi_crispr_allele.nuclease.MgiCrisprAlleleReportNucleaseProjection;
import org.gentar.report.collection.mgi_crispr_allele.nuclease.MgiCrisprAlleleReportNucleaseServiceImpl;
import org.gentar.report.collection.mgi_crispr_allele.outcome.MgiCrisprAlleleReportOutcomeMutationProjection;
import org.gentar.report.collection.mgi_crispr_allele.outcome.MgiCrisprAlleleReportOutcomeServiceImpl;
import org.gentar.report.collection.mgi_crispr_allele.sequence.MgiCrisprAlleleReportMutationSequenceProjection;
import org.gentar.report.collection.mgi_crispr_allele.sequence.MgiCrisprAlleleReportMutationSequenceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MgiCrisprAlleleReportDistributionProductServiceImplTest {

    @Mock
    private MgiCrisprAlleleReportColonyRepository mgiCrisprAlleleReportColonyRepository;
    @Mock
    private MgiCrisprAlleleReportOutcomeServiceImpl outcomeReportService;
    @Mock
    private MgiCrisprAlleleReportMutationServiceImpl mutationReportService;
    @Mock
    private MgiCrisprAlleleReportGuideServiceImpl guideReportService;
    @Mock
    private MgiCrisprAlleleReportNucleaseServiceImpl nucleaseReportService;
    @Mock
    private MgiCrisprAlleleReportMutagenesisDonorService mutagenesisDonorReportService;
    @Mock
    private MgiCrisprAlleleReportGenotypePrimerService genotypePrimerReportService;
    @Mock
    private MgiCrisprAlleleReportMutationSequenceServiceImpl mutationSequenceReportService;
    @Mock
    private MgiCrisprAlleleReportMutationCategorizationServiceImpl
        mutationCategorizationReportService;


    MgiCrisprAlleleReportColonyServiceImpl testInstance;

    @BeforeEach
    void setUp() {
        testInstance =
            new MgiCrisprAlleleReportColonyServiceImpl(mgiCrisprAlleleReportColonyRepository,
                outcomeReportService,
                mutationReportService,
                guideReportService,
                nucleaseReportService,
                mutagenesisDonorReportService,
                genotypePrimerReportService,
                mutationSequenceReportService,
                mutationCategorizationReportService);

        lenient().when(mgiCrisprAlleleReportColonyRepository.findAllColonyReportProjections())
            .thenReturn(List.of(mgiCrisprAlleleReportColonyProjectionMockData()));
        testInstance.getAllColonyReportProjections();
    }

    @Test
    void getAllColonyReportProjections() {

        List<MgiCrisprAlleleReportColonyProjection> mgiCrisprAlleleReportColonyProjections =
            testInstance.getAllColonyReportProjections();

        assertEquals(mgiCrisprAlleleReportColonyProjections.size(), 1);
    }

    @Test
    void getMutationMap() {

        lenient()
            .when(outcomeReportService.getSelectedOutcomeMutationCrisprReportProjections(any()))
            .thenReturn(List.of(mgiCrisprAlleleReportOutcomeMutationProjectionMockData()));
        testInstance.getAllColonyReportProjections();

        Map<Long, MgiCrisprAlleleReportOutcomeMutationProjection>
            mgiCrisprAlleleReportOutcomeMutationProjectionMap = testInstance.getMutationMap();
        assertEquals(mgiCrisprAlleleReportOutcomeMutationProjectionMap.size(), 1);
    }

    @Test
    void getGuideMap() {

        testInstance.getAllColonyReportProjections();

        lenient().when(guideReportService.getSelectedGuideProjections(any()))
            .thenReturn(List.of(mgiCrisprAlleleReportGuideProjectionMockData()));

        Map<Long, Set<MgiCrisprAlleleReportGuideProjection>> mgiCrisprAlleleReportGuideProjection =
            testInstance.getGuideMap();
        assertEquals(mgiCrisprAlleleReportGuideProjection.size(), 1);
    }

    @Test
    void getNucleaseMap() {

        lenient().when(nucleaseReportService.getSelectedNucleaseProjections(any()))
            .thenReturn(List.of(mgiCrisprAlleleReportNucleaseProjectionMockData()));

        Map<Long, Set<MgiCrisprAlleleReportNucleaseProjection>>
            mgiCrisprAlleleReportNucleaseProjection =
            testInstance.getNucleaseMap();
        assertEquals(mgiCrisprAlleleReportNucleaseProjection.size(), 1);
    }

    @Test
    void getMutagenesisDonorMap() {

        lenient().when(mutagenesisDonorReportService.getSelectedMutagenesisDonorProjections(any()))
            .thenReturn(List.of(mgiCrisprAlleleReportMutagenesisDonorProjectionMockData()));

        Map<Long, Set<MgiCrisprAlleleReportMutagenesisDonorProjection>>
            mgiCrisprAlleleReportMutagenesisDonorProjection =
            testInstance.getMutagenesisDonorMap();
        assertEquals(mgiCrisprAlleleReportMutagenesisDonorProjection.size(), 1);
    }

    @Test
    void getGenotypePrimerMap() {

        lenient().when(genotypePrimerReportService.getSelectedGenotypePrimerProjections(any()))
            .thenReturn(List.of(mgiCrisprAlleleReportGenotypePrimerProjectionMockData()));

        Map<Long, Set<MgiCrisprAlleleReportGenotypePrimerProjection>>
            mgiCrisprAlleleReportGenotypePrimerProjection =
            testInstance.getGenotypePrimerMap();
        assertEquals(mgiCrisprAlleleReportGenotypePrimerProjection.size(), 1);
    }

    @Test
    void getMutationSequenceMap() {


        lenient().when(mutationSequenceReportService.getSelectedMutationSequenceProjections(any()))
            .thenReturn(List.of(mgiCrisprAlleleReportMutationSequenceProjectionMockData()));

        Map<Long, Set<MgiCrisprAlleleReportMutationSequenceProjection>>
            mgiCrisprAlleleReportMutationSequenceProjection =
            testInstance.getMutationSequenceMap();
        assertEquals(mgiCrisprAlleleReportMutationSequenceProjection.size(), 1);
    }

    @Test
    void getMutationCategorizationMap() {


        lenient().when( mutationCategorizationReportService
            .getSelectedMutationCategorizationProjections(any()))
            .thenReturn(List.of(mgiCrisprAlleleReportMutationCategorizationProjectionMockData()));

        Map<Long, Set<MgiCrisprAlleleReportMutationCategorizationProjection>>
            mgiCrisprAlleleReportMutationCategorizationProjection =
            testInstance.getMutationCategorizationMap();
        assertEquals(mgiCrisprAlleleReportMutationCategorizationProjection.size(), 1);
    }

    @Test
    void getGeneMap() {


        lenient().when(mutationReportService.getSelectedMutationGeneProjections(any()))
            .thenReturn(List.of(mgiCrisprAlleleReportMutationGeneProjectionMockData()));

        Map<Long, Gene> gene =
            testInstance.getGeneMap();
        assertEquals(gene.size(), 1);
    }
}