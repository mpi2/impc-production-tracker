package org.gentar.report.collection.gene_interest.phenotyping;

import static org.gentar.mockdata.MockData.geneInterestReportOutcomeMutationProjectionMockData;
import static org.gentar.mockdata.MockData.geneMockData;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.gentar.biology.gene.Gene;
import org.gentar.mockdata.MockData;
import org.gentar.report.collection.gene_interest.outcome.GeneInterestReportOutcomeMutationProjection;
import org.gentar.report.utils.status.GeneStatusSummaryHelperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class GeneInterestReportPhenotypingTest {

    @Mock
    private GeneInterestReportPhenotypingServiceImpl phenotypingService;
    @Mock
    private GeneStatusSummaryHelperImpl geneStatusSummaryHelper;


    GeneInterestReportPhenotyping testInstance;

    @BeforeEach
    void setUp() {
        testInstance =
            new GeneInterestReportPhenotyping(phenotypingService, geneStatusSummaryHelper);
    }

    @Test
    void summariseData() {

        lenient().when(phenotypingService.getGeneInterestReportPhenotypingAttemptProjections())
            .thenReturn(List.of(MockData.geneInterestReportPhenotypingAttemptProjectionMockData()));


        Map<Long, Set<GeneInterestReportOutcomeMutationProjection>> mutationMap = new HashMap<>();
        mutationMap.put(1L,Set.of(geneInterestReportOutcomeMutationProjectionMockData()));

        lenient().when(phenotypingService.getMutationMap())
            .thenReturn(mutationMap);

        Map<Long, GeneInterestReportOutcomeMutationProjection> geneMutationMap= new HashMap<>();
        geneMutationMap.put(1L,geneInterestReportOutcomeMutationProjectionMockData());
        lenient().when(phenotypingService.getFilteredMutationMap(mutationMap))
            .thenReturn(geneMutationMap);

        Map<Long, Set<Gene>> geneSetMap = new HashMap<>();
        geneSetMap.put(1L,Set.of(geneMockData()));
        lenient().when(phenotypingService.getGeneMap())
            .thenReturn(geneSetMap);

        Map<Long, Gene> genMap = new HashMap<>();
        genMap.put(1L,geneMockData());
        lenient().when(phenotypingService.getFilteredGeneMap(geneSetMap))
            .thenReturn(genMap);
        assertDoesNotThrow(() ->
        testInstance.summariseData(Set.of(1L))
        );

        assertEquals(testInstance.getGeneIdToSymbolMap().size(),1);

        assertEquals(testInstance.getGeneIdToEarlyAdultPhenotypingStageStatusStatusMap().size(),0);

    }
}