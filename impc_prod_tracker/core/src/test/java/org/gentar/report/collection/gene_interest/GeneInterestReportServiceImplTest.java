package org.gentar.report.collection.gene_interest;

import static org.junit.jupiter.api.Assertions.*;

import org.gentar.report.ReportServiceImpl;
import org.gentar.report.collection.gene_interest.phenotyping.GeneInterestReportPhenotyping;
import org.gentar.report.collection.gene_interest.production_type.GeneInterestReportCrisprProduction;
import org.gentar.report.collection.gene_interest.production_type.GeneInterestReportEsCellProduction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GeneInterestReportServiceImplTest {


    @Mock
    private  ReportServiceImpl reportService;
    @Mock
    private  GeneInterestReportCrisprProduction crisprProduction;
    @Mock
    private  GeneInterestReportEsCellProduction esCellProduction;
    @Mock
    private  GeneInterestReportPhenotyping phenotyping;
    @Mock
    private  GeneInterestReportMergeHelperImpl mergeHelper;

    GeneInterestReportServiceImpl testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new GeneInterestReportServiceImpl(reportService,
            crisprProduction,
            esCellProduction,
            phenotyping,
            mergeHelper);
    }

    @Test
    void generateGeneInterestReport() {
        assertDoesNotThrow(() ->
        testInstance.generateGeneInterestReport()
        );
    }
}