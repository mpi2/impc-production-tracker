package org.gentar.report.collection.gene_interest;

import static org.gentar.mockdata.MockData.MGI_00000001;
import static org.gentar.mockdata.MockData.TEST_IDENTIFICATION_NUMBER;
import static org.gentar.mockdata.MockData.TEST_SUMMARY_STATUS;
import static org.gentar.mockdata.MockData.geneInterestReportGeneProjectionMockData;
import static org.gentar.mockdata.MockData.geneInterestReportProjectProjectionMockData;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GeneInterestReportProjectionMergeHelperImplTest {

    GeneInterestReportProjectionMergeHelperImpl testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new GeneInterestReportProjectionMergeHelperImpl();
    }

    @Test
    void getGenes() {
        Map<String, String> genes =
            testInstance.getGenes(List.of(geneInterestReportProjectProjectionMockData()),
                List.of(geneInterestReportGeneProjectionMockData()));
        assertEquals(genes.get(MGI_00000001), MGI_00000001);
    }

    @Test
    void getStatusByPlan() {

        Map<String, String> genes =
            testInstance.getStatusByPlan(List.of(geneInterestReportProjectProjectionMockData()),
                List.of(geneInterestReportGeneProjectionMockData()));
        assertEquals(genes.get(TEST_IDENTIFICATION_NUMBER), TEST_SUMMARY_STATUS);
    }

    @Test
    void getProjectsByGene() {

        Map<String, List<String>> genes =
            testInstance.getProjectsByGene(List.of(geneInterestReportProjectProjectionMockData()),
                List.of(geneInterestReportGeneProjectionMockData()));
        assertEquals(genes.get(MGI_00000001).size(), 1);
    }

    @Test
    void getPlansByProject() {

        Map<String, List<String>> genes =
            testInstance.getPlansByProject(List.of(geneInterestReportProjectProjectionMockData()),
                List.of(geneInterestReportGeneProjectionMockData()));
        assertEquals(genes.size(), 1);
    }

    @Test
    void getAssignmentByProject() {

        Map<String, String> genes =
            testInstance
                .getAssignmentByProject(List.of(geneInterestReportProjectProjectionMockData()),
                    List.of(geneInterestReportGeneProjectionMockData()));
        assertEquals(genes.size(), 1);
    }

    @Test
    void getProjectsByGeneFilteredForNullTargeting() {
        Map<String, String> mutationToAlleleCategory = new HashMap<>();
        mutationToAlleleCategory.put(TEST_IDENTIFICATION_NUMBER, TEST_IDENTIFICATION_NUMBER);
        Map<String, List<String>> genes =
            testInstance.getProjectsByGeneFilteredForNullTargeting(
                List.of(geneInterestReportProjectProjectionMockData()),
                List.of(geneInterestReportGeneProjectionMockData()), mutationToAlleleCategory);
        assertEquals(genes.size(), 1);
    }

    @Test
    void getProjectsByGeneFilteredForConditionalTargeting() {

        Map<String, String> mutationToAlleleCategory = new HashMap<>();
        mutationToAlleleCategory.put("conditional", "conditional");
        Map<String, List<String>> genes =
            testInstance.getProjectsByGeneFilteredForConditionalTargeting(
                List.of(geneInterestReportGeneProjectionMockData()), mutationToAlleleCategory);
        assertEquals(genes.size(), 0);
    }

    @Test
    void getPlansByProjectFilteredForNullTargeting() {
        Map<String, String> mutationToAlleleCategory = new HashMap<>();
        mutationToAlleleCategory.put("null", "null");
        Map<String, List<String>> genes =
            testInstance.getPlansByProjectFilteredForNullTargeting(
                List.of(geneInterestReportProjectProjectionMockData()),
                List.of(geneInterestReportGeneProjectionMockData()), mutationToAlleleCategory);
        assertEquals(genes.size(), 1);
    }

    @Test
    void getPlansByProjectFilteredForConditionalTargeting() {
        Map<String, String> mutationToAlleleCategory = new HashMap<>();
        mutationToAlleleCategory.put("conditional", "conditional");
        Map<String, List<String>> genes =
            testInstance.getPlansByProjectFilteredForConditionalTargeting(
                List.of(geneInterestReportGeneProjectionMockData()), mutationToAlleleCategory);
        assertEquals(genes.size(), 0);
    }

    @Test
    void getStatusByPlanFilteredForNullTargeting() {
        Map<String, String> mutationToAlleleCategory = new HashMap<>();
        mutationToAlleleCategory.put(TEST_IDENTIFICATION_NUMBER, TEST_IDENTIFICATION_NUMBER);
        Map<String, String> genes =
            testInstance
                .getStatusByPlanFilteredForNullTargeting(
                    List.of(geneInterestReportProjectProjectionMockData()),
                    List.of(geneInterestReportGeneProjectionMockData()), mutationToAlleleCategory);
        assertEquals(genes.size(), 1);
    }

    @Test
    void getStatusByPlanFilteredForConditionalTargeting() {
        Map<String, String> mutationToAlleleCategory = new HashMap<>();
        mutationToAlleleCategory.put(TEST_IDENTIFICATION_NUMBER, TEST_IDENTIFICATION_NUMBER);
        Map<String, String> genes =
            testInstance
                .getStatusByPlanFilteredForConditionalTargeting(
                    List.of(geneInterestReportGeneProjectionMockData()), mutationToAlleleCategory);
        assertEquals(genes.size(), 0);
    }
}