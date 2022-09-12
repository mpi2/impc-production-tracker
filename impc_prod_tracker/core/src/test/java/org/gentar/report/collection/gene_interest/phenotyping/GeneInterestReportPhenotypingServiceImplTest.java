package org.gentar.report.collection.gene_interest.phenotyping;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.gentar.biology.gene.Gene;
import org.gentar.mockdata.MockData;
import org.gentar.report.collection.gene_interest.mutation.GeneInterestReportMutationServiceImpl;
import org.gentar.report.collection.gene_interest.outcome.GeneInterestReportOutcomeMutationProjection;
import org.gentar.report.collection.gene_interest.outcome.GeneInterestReportOutcomeServiceImpl;
import org.gentar.report.collection.gene_interest.phenotyping_attempt.GeneInterestReportPhenotypingAttemptProjection;
import org.gentar.report.collection.gene_interest.phenotyping_attempt.GeneInterestReportPhenotypingAttemptServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class GeneInterestReportPhenotypingServiceImplTest {


    @Mock
    private GeneInterestReportPhenotypingAttemptServiceImpl phenotypingAttemptService;
    @Mock
    private GeneInterestReportOutcomeServiceImpl outcomeReportService;
    @Mock
    private GeneInterestReportMutationServiceImpl mutationReportService;
    @Mock
    private List<GeneInterestReportPhenotypingAttemptProjection> pap;
    @Mock
    private Map<Long, Set<GeneInterestReportOutcomeMutationProjection>> completeOutcomeMutationMap;
    @Mock
    private Map<Long, GeneInterestReportOutcomeMutationProjection> filteredOutcomeMutationMap;

    GeneInterestReportPhenotypingServiceImpl testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new GeneInterestReportPhenotypingServiceImpl(phenotypingAttemptService,
            outcomeReportService, mutationReportService);
    }


    @Test
    void getMutationMap() {

        lenient().when(phenotypingAttemptService.getPhenotypingAttemptProjections())
            .thenReturn(List.of(MockData.geneInterestReportPhenotypingAttemptProjectionMockData()));

        lenient().when(outcomeReportService.getSelectedOutcomeMutationProjections(List.of(1L)))
            .thenReturn(List.of(MockData.geneInterestReportOutcomeMutationProjectionMockData()));

        testInstance.getGeneInterestReportPhenotypingAttemptProjections();

        Map<Long, Set<GeneInterestReportOutcomeMutationProjection>> mutationMap =
            testInstance.getMutationMap();

        assertEquals(mutationMap.size(), 1);
    }

    @Test
    void getFilteredMutationMap() {

        Map<Long, Set<GeneInterestReportOutcomeMutationProjection>> outcomeMutationMap =
            new HashMap<>();

        outcomeMutationMap
            .put(1L, Set.of(MockData.geneInterestReportOutcomeMutationProjectionMockData()));

        Map<Long, GeneInterestReportOutcomeMutationProjection> mutationMap =
            testInstance.getFilteredMutationMap(outcomeMutationMap);

        assertEquals(mutationMap.size(), 1);
    }

    @Test
    void getGeneMap() {

        lenient().when(phenotypingAttemptService.getPhenotypingAttemptProjections())
            .thenReturn(List.of(MockData.geneInterestReportPhenotypingAttemptProjectionMockData()));

        lenient().when(outcomeReportService.getSelectedOutcomeMutationProjections(List.of(1L)))
            .thenReturn(List.of(MockData.geneInterestReportOutcomeMutationProjectionMockData()));

        lenient().when(mutationReportService.getSelectedMutationGeneProjections(List.of(1L)))
            .thenReturn(List.of(MockData.geneInterestReportMutationGeneProjectionMockData()));

        testInstance.getGeneInterestReportPhenotypingAttemptProjections();

        Map<Long, Set<Gene>> geneMap = testInstance.getGeneMap();

        assertEquals(geneMap.size(), 1);
    }

    @Test
    void getFilteredGeneMap() {

        lenient().when(phenotypingAttemptService.getPhenotypingAttemptProjections())
            .thenReturn(List.of(MockData.geneInterestReportPhenotypingAttemptProjectionMockData()));

        lenient().when(outcomeReportService.getSelectedOutcomeMutationProjections(List.of(1L)))
            .thenReturn(List.of(MockData.geneInterestReportOutcomeMutationProjectionMockData()));

        lenient().when(mutationReportService.getSelectedMutationGeneProjections(List.of(1L)))
            .thenReturn(List.of(MockData.geneInterestReportMutationGeneProjectionMockData()));

        testInstance.getGeneInterestReportPhenotypingAttemptProjections();

        Map<Long, Set<Gene>> geneMap = testInstance.getGeneMap();

        Map<Long, Gene> genes = testInstance.getFilteredGeneMap(geneMap);
        assertEquals(genes.size(), 1L);
    }

}