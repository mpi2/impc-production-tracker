package org.gentar.report.collection.gene_interest.production_type;

import static org.junit.jupiter.api.Assertions.*;

import org.gentar.report.collection.gene_interest.GeneInterestReportProjectionMergeHelperImpl;
import org.gentar.report.collection.gene_interest.gene.GeneInterestReportGeneServiceImpl;
import org.gentar.report.collection.gene_interest.mutation.GeneInterestReportMutationServiceImpl;
import org.gentar.report.collection.gene_interest.project.GeneInterestReportProjectServiceImpl;
import org.gentar.report.utils.assignment.GeneAssignmentStatusHelperImpl;
import org.gentar.report.utils.status.GeneStatusSummaryHelperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GeneInterestReportEsCellProductionTest {

    @Mock
    private GeneInterestReportProjectServiceImpl projectService;
    @Mock
    private GeneInterestReportGeneServiceImpl geneService;
    @Mock
    private GeneInterestReportMutationServiceImpl mutationService;
    @Mock
    private GeneStatusSummaryHelperImpl geneStatusSummaryHelper;
    @Mock
    private GeneAssignmentStatusHelperImpl geneAssignmentStatusHelper;
    @Mock
    private GeneInterestReportProjectionMergeHelperImpl projectionMergeHelper;

    GeneInterestReportEsCellProduction testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new GeneInterestReportEsCellProduction(projectService,
            geneService,
            mutationService,
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