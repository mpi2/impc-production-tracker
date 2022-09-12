package org.gentar.report.collection.mgi_modification_allele;

import static org.gentar.mockdata.MockData.geneMockData;
import static org.gentar.mockdata.MockData.mgiModificationAlleleReportColonyProjectionMockData;
import static org.gentar.mockdata.MockData.mgiModificationAlleleReportOutcomeMutationProjectionMockData;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.lenient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.gentar.biology.gene.Gene;
import org.gentar.report.ReportServiceImpl;
import org.gentar.report.collection.mgi_modification_allele.modification_colony.MgiModificationAlleleReportColonyProjection;
import org.gentar.report.collection.mgi_modification_allele.modification_colony.MgiModificationAlleleReportColonyService;
import org.gentar.report.collection.mgi_modification_allele.outcome.MgiModificationAlleleReportOutcomeMutationProjection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MgiModificationAlleleServiceImplTest {


    @Mock
    private MgiModificationAlleleReportColonyService colonyReportService;
    @Mock
    private ReportServiceImpl reportService;

    MgiModificationAlleleServiceImpl testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new MgiModificationAlleleServiceImpl(colonyReportService, reportService);
    }

    @Test
    void generateMgiModificationAlleleReport() {

        List<MgiModificationAlleleReportColonyProjection> cp = new ArrayList<>();
        cp.add(mgiModificationAlleleReportColonyProjectionMockData());
        lenient().when(colonyReportService.getAllMgiModificationAlleleReportColonyProjections())
            .thenReturn(cp);


        Map<Long, MgiModificationAlleleReportOutcomeMutationProjection> filteredOutcomeMutationMap =
            new HashMap<>();
        filteredOutcomeMutationMap
            .put(1L, mgiModificationAlleleReportOutcomeMutationProjectionMockData());
        lenient().when(colonyReportService.getMutationMap())
            .thenReturn(filteredOutcomeMutationMap);

        Map<Long, MgiModificationAlleleReportOutcomeMutationProjection>
            filteredProductionOutcomeMutationMap = new HashMap<>();
        filteredProductionOutcomeMutationMap
            .put(1L, mgiModificationAlleleReportOutcomeMutationProjectionMockData());
        lenient().when(colonyReportService.getProductionMutationMap())
            .thenReturn(filteredProductionOutcomeMutationMap);

        Map<Long, Gene> filteredMutationGeneMap = new HashMap<>();
        filteredMutationGeneMap.put(1L, geneMockData());
        lenient().when(colonyReportService.getGeneMap())
            .thenReturn(filteredMutationGeneMap);

        Map<Long, String> filteredAlleleCategoryMap = new HashMap<>();
        filteredAlleleCategoryMap.put(1L, "");
        lenient().when(colonyReportService.getAlleleCategoryMap())
            .thenReturn(filteredAlleleCategoryMap);


        assertDoesNotThrow(() ->
            testInstance.generateMgiModificationAlleleReport()
        );
    }
}