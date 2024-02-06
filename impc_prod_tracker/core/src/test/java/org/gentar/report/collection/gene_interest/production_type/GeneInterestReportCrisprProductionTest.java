package org.gentar.report.collection.gene_interest.production_type;

import org.gentar.report.collection.gene_interest.GeneInterestReportProjectionMergeHelperImpl;
import org.gentar.report.collection.gene_interest.gene.GeneInterestReportGeneServiceImpl;
import org.gentar.report.collection.gene_interest.project.GeneInterestReportProjectServiceImpl;
import org.gentar.report.utils.assignment.GeneAssignmentStatusHelperImpl;
import org.gentar.report.utils.status.GeneStatusSummaryHelperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class GeneInterestReportCrisprProductionTest {


    @Mock
    private GeneInterestReportProjectServiceImpl projectService;
    @Mock
    private GeneInterestReportGeneServiceImpl geneService;
    @Mock
    private GeneStatusSummaryHelperImpl geneStatusSummaryHelper;
    @Mock
    private GeneAssignmentStatusHelperImpl geneAssignmentStatusHelper;
    @Mock
    private GeneInterestReportProjectionMergeHelperImpl projectionMergeHelper;


    GeneInterestReportCrisprProduction testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new GeneInterestReportCrisprProduction(projectService,
            geneService,
            geneStatusSummaryHelper,
            geneAssignmentStatusHelper,
            projectionMergeHelper);
    }

    @Test
    void summariseData() {
        assertDoesNotThrow(() ->
            testInstance.summariseData()
        );
    }

}