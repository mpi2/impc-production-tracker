package org.gentar.report.collection.mgi_modification_allele.modification_colony;

import org.gentar.biology.gene.Gene;
import org.gentar.report.collection.mgi_modification_allele.mutation.MgiModificationAlleleReportMutationServiceImpl;
import org.gentar.report.collection.mgi_modification_allele.outcome.MgiModificationAlleleReportOutcomeMutationProjection;
import org.gentar.report.collection.mgi_modification_allele.outcome.MgiModificationAlleleReportOutcomeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.gentar.mockdata.MockData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class MgiModificationAlleleReportColonyServiceImplTest {

    @Mock
    private MgiModificationAlleleReportColonyRepository mgiModificationAlleleReportColonyRepository;
    @Mock
    private MgiModificationAlleleReportOutcomeService
        mgiModificationAlleleReportOutcomeService;
    @Mock
    private MgiModificationAlleleReportMutationServiceImpl mutationReportService;


    MgiModificationAlleleReportColonyServiceImpl testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new MgiModificationAlleleReportColonyServiceImpl(
            mgiModificationAlleleReportColonyRepository,
            mgiModificationAlleleReportOutcomeService,
            mutationReportService);
    }

    @Test
    void getAllMgiModificationAlleleReportColonyProjections() {

        lenient().when(mgiModificationAlleleReportColonyRepository
            .findMgiModificationAlleleReportProjections())
            .thenReturn(List.of(mgiModificationAlleleReportColonyProjectionMockData()));

        List<MgiModificationAlleleReportColonyProjection>
            mgiModificationAlleleReportColonyProjections =
            testInstance.getAllMgiModificationAlleleReportColonyProjections();

        assertEquals(mgiModificationAlleleReportColonyProjections.getFirst().getModificationOutcomeId(),
            1L);
    }

    @Test
    void getMutationMap() {

        lenient().when(mgiModificationAlleleReportColonyRepository
            .findMgiModificationAlleleReportProjections())
            .thenReturn(List.of(mgiModificationAlleleReportColonyProjectionMockData()));
        getAllMgiModificationAlleleReportColonyProjections();

        lenient().when(
            mgiModificationAlleleReportOutcomeService.getSelectedOutcomeMutationProjections(any()))
            .thenReturn(List.of(mgiModificationAlleleReportOutcomeMutationProjectionMockData()));

        Map<Long, MgiModificationAlleleReportOutcomeMutationProjection> mutationMap =
            testInstance.getMutationMap();

        assertEquals(mutationMap.size(), 1);
    }

    @Test
    void getProductionMutationMap() {

        lenient().when(mgiModificationAlleleReportColonyRepository
            .findMgiModificationAlleleReportProjections())
            .thenReturn(List.of(mgiModificationAlleleReportColonyProjectionMockData()));
        getAllMgiModificationAlleleReportColonyProjections();

        lenient().when(
            mgiModificationAlleleReportOutcomeService.getSelectedOutcomeMutationProjections(any()))
            .thenReturn(List.of(mgiModificationAlleleReportOutcomeMutationProjectionMockData()));


        Map<Long, MgiModificationAlleleReportOutcomeMutationProjection>
            mgiModificationAlleleReportOutcomeMutationProjectionMap =
            testInstance.getProductionMutationMap();

        assertEquals(mgiModificationAlleleReportOutcomeMutationProjectionMap.size(), 1);
    }

    @Test
    void getGeneMap() {
        lenient().when(mgiModificationAlleleReportColonyRepository
            .findMgiModificationAlleleReportProjections())
            .thenReturn(List.of(mgiModificationAlleleReportColonyProjectionMockData()));
        getAllMgiModificationAlleleReportColonyProjections();

        lenient().when(
            mgiModificationAlleleReportOutcomeService.getSelectedOutcomeMutationProjections(any()))
            .thenReturn(List.of(mgiModificationAlleleReportOutcomeMutationProjectionMockData()));

        lenient().when(
            mutationReportService.getSelectedMutationGeneProjections(any()))
            .thenReturn(List.of(mgiModificationAlleleReportMutationGeneProjectionMockData()));

        Map<Long, Gene> geneMap = testInstance.getGeneMap();
        assertEquals(geneMap.size(), 1);
    }

    @Test
    void getAlleleCategoryMap() {
        lenient().when(mgiModificationAlleleReportColonyRepository
            .findMgiModificationAlleleReportProjections())
            .thenReturn(List.of(mgiModificationAlleleReportColonyProjectionMockData()));

        getAllMgiModificationAlleleReportColonyProjections();

        lenient().when(
            mgiModificationAlleleReportOutcomeService.getSelectedOutcomeMutationProjections(any()))
            .thenReturn(List.of(mgiModificationAlleleReportOutcomeMutationProjectionMockData()));

        lenient().when(
            mutationReportService.getSelectedMutationGeneProjections(any()))
            .thenReturn(List.of(mgiModificationAlleleReportMutationGeneProjectionMockData()));

        lenient().when(
            mutationReportService.getSelectedEsCellMutationTypeProjections(any()))
            .thenReturn(List.of(mgiModificationAlleleReportEsCellMutationTypeProjection()));

        Map<Long, String> categoryMap = new HashMap<>();
        categoryMap.put(1L, "categoryMap");
        lenient().when(
            mutationReportService.assignAlleleCategories(any()))
            .thenReturn(categoryMap);

        Map<Long, String> alleleCategoryMap = testInstance.getAlleleCategoryMap();
        assertEquals(alleleCategoryMap.size(), 1);
    }
}