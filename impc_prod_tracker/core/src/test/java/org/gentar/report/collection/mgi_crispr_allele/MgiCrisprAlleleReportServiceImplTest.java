package org.gentar.report.collection.mgi_crispr_allele;

import org.gentar.biology.gene.Gene;
import org.gentar.report.ReportService;
import org.gentar.report.collection.mgi_crispr_allele.colony.MgiCrisprAlleleReportColonyService;
import org.gentar.report.collection.mgi_crispr_allele.genotype_primer.MgiCrisprAlleleReportGenotypePrimerProjection;
import org.gentar.report.collection.mgi_crispr_allele.guide.MgiCrisprAlleleReportGuideProjection;
import org.gentar.report.collection.mgi_crispr_allele.mutagenesis_donor.MgiCrisprAlleleReportMutagenesisDonorProjection;
import org.gentar.report.collection.mgi_crispr_allele.mutation_characterization.MgiCrisprAlleleReportMutationCategorizationProjection;
import org.gentar.report.collection.mgi_crispr_allele.nuclease.MgiCrisprAlleleReportNucleaseProjection;
import org.gentar.report.collection.mgi_crispr_allele.outcome.MgiCrisprAlleleReportOutcomeMutationProjection;
import org.gentar.report.collection.mgi_crispr_allele.sequence.MgiCrisprAlleleReportMutationSequenceProjection;
import org.gentar.report.utils.genotype_primer.MgiGenotypePrimerFormatHelper;
import org.gentar.report.utils.guide.MgiGuideFormatHelper;
import org.gentar.report.utils.mutagenesis_donor.MgiMutagenesisDonorFormatHelper;
import org.gentar.report.utils.mutation_categorization.MgiMutationCategorizationFormatHelper;
import org.gentar.report.utils.nuclease.MgiNucleaseFormatHelper;
import org.gentar.report.utils.sequence.MgiMutationSequenceFormatHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.gentar.mockdata.MockData.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class MgiCrisprAlleleReportServiceImplTest {

    @Mock
    private MgiCrisprAlleleReportColonyService colonyReportService;
    @Mock
    private ReportService reportService;

    @Mock
    private MgiGuideFormatHelper mgiGuideFormatHelper;
    @Mock
    private MgiNucleaseFormatHelper mgiNucleaseFormatHelper;
    @Mock
    private MgiMutationSequenceFormatHelper mgiMutationSeqeunceFormatHelper;
    @Mock
    private MgiMutationCategorizationFormatHelper mgiMutationCategorizationFormatHelper;
    @Mock
    private MgiMutagenesisDonorFormatHelper mgiMutagenesisDonorFormatHelper;
    @Mock
    private MgiGenotypePrimerFormatHelper mgiGenotypePrimerFormatHelper;


    MgiCrisprAlleleReportServiceImpl testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new MgiCrisprAlleleReportServiceImpl(colonyReportService,
            reportService,
            mgiGuideFormatHelper,
            mgiNucleaseFormatHelper,
            mgiMutationSeqeunceFormatHelper,
            mgiMutationCategorizationFormatHelper,
            mgiMutagenesisDonorFormatHelper,
            mgiGenotypePrimerFormatHelper);
    }

    @Test
    void generateMgiCrisprAlleleReport() {

        lenient().when(colonyReportService.getAllColonyReportProjections())
            .thenReturn(List.of(mgiCrisprAlleleReportColonyProjectionMockData()));

        Map<Long, Set<MgiCrisprAlleleReportGuideProjection>> guideMap = new HashMap<>();
        guideMap.put(1L, Set.of(mgiCrisprAlleleReportGuideProjectionMockData()));

        lenient().when(colonyReportService.getGuideMap())
            .thenReturn(guideMap);

        Map<Long, Set<MgiCrisprAlleleReportNucleaseProjection>> nucleaseMap = new HashMap<>();
        nucleaseMap.put(1L, Set.of(mgiCrisprAlleleReportNucleaseProjectionMockData()));
        lenient().when(colonyReportService.getNucleaseMap())
            .thenReturn(nucleaseMap);

        Map<Long, Set<MgiCrisprAlleleReportMutagenesisDonorProjection>> mutagenesisDonorMap =
            new HashMap<>();
        mutagenesisDonorMap
            .put(1L, Set.of(mgiCrisprAlleleReportMutagenesisDonorProjectionMockData()));
        lenient().when(colonyReportService.getMutagenesisDonorMap())
            .thenReturn(mutagenesisDonorMap);

        Map<Long, Set<MgiCrisprAlleleReportGenotypePrimerProjection>> genotypePrimerMap =
            new HashMap<>();
        genotypePrimerMap.put(1L, Set.of(mgiCrisprAlleleReportGenotypePrimerProjectionMockData()));
        lenient().when(colonyReportService.getGenotypePrimerMap())
            .thenReturn(genotypePrimerMap);

        Map<Long, MgiCrisprAlleleReportOutcomeMutationProjection> filteredOutcomeMutationMap =
            new HashMap<>();
        filteredOutcomeMutationMap
            .put(1L, mgiCrisprAlleleReportOutcomeMutationProjectionMockData());
        lenient().when(colonyReportService.getMutationMap())
            .thenReturn(filteredOutcomeMutationMap);

        Map<Long, Gene> filteredMutationGeneMap = new HashMap<>();
        filteredMutationGeneMap.put(1L, geneMockData());
        lenient().when(colonyReportService.getGeneMap())
            .thenReturn(filteredMutationGeneMap);

        Map<Long, Set<MgiCrisprAlleleReportMutationSequenceProjection>> sequenceMap =
            new HashMap<>();
        sequenceMap.put(1L, Set.of(mgiCrisprAlleleReportMutationSequenceProjectionMockData()));
        lenient().when(colonyReportService.getMutationSequenceMap())
            .thenReturn(sequenceMap);

        Map<Long, Set<MgiCrisprAlleleReportMutationCategorizationProjection>> categorizationMap =
            new HashMap<>();
        categorizationMap
            .put(1L, Set.of(mgiCrisprAlleleReportMutationCategorizationProjectionMockData()));
        lenient().when(colonyReportService.getMutationCategorizationMap())
            .thenReturn(categorizationMap);

        assertDoesNotThrow(() ->
            testInstance.generateMgiCrisprAlleleReport()
        );
    }
}