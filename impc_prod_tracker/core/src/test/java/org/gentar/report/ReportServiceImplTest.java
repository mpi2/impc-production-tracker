package org.gentar.report;

import static org.gentar.mockdata.MockData.reportMockData;
import static org.gentar.mockdata.MockData.reportTypeMockData;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.lenient;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    ReportServiceImpl testInstance;


    @Mock
    private ReportRepository reportRepository;
    @Mock
    private ReportTypeRepository reportTypeRepository;
    @Mock
    private ReportTypeServiceImpl reportTypeService;


    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        testInstance =
            new ReportServiceImpl(reportRepository, reportTypeRepository, reportTypeService);
        response = new MockHttpServletResponse();
    }

    @Test
    void saveReport() {
        lenient().when(
            reportTypeRepository.findReportTypeByNameIs(ReportTypeName.GENE_INTEREST.getLabel()))
            .thenReturn(reportTypeMockData());

        assertDoesNotThrow(() ->
            testInstance.saveReport(ReportTypeName.GENE_INTEREST, "ReportText")
        );
    }

    @Test
    void cleanAllReports() {

        lenient()
            .when(reportTypeService.reportTypeNameExists(ReportTypeName.GENE_INTEREST.getLabel()))
            .thenReturn(true);
        lenient().when(
            reportTypeService.reportTypeExistsInDatabase(ReportTypeName.GENE_INTEREST.getLabel()))
            .thenReturn(true);
        lenient().when(
            reportRepository.findAllByReportType_NameIs(ReportTypeName.GENE_INTEREST.getLabel()))
            .thenReturn(List.of(reportMockData(), reportMockData(), reportMockData()));

        assertDoesNotThrow(() ->
            testInstance.cleanAllReports()
        );
    }


    @Test
    void writeReport() {
        lenient()
            .when(reportTypeService.reportTypeNameExists(ReportTypeName.GENE_INTEREST.getLabel()))
            .thenReturn(true);
        lenient().when(
            reportTypeService.reportTypeExistsInDatabase(ReportTypeName.GENE_INTEREST.getLabel()))
            .thenReturn(true);

        lenient().when(
            reportRepository.findFirstByReportType_NameIsOrderByCreatedAtDesc(
                ReportTypeName.GENE_INTEREST.getLabel()))
            .thenReturn(reportMockData());

        assertDoesNotThrow(() ->
            testInstance.writeReport(response, ReportTypeName.GENE_INTEREST.getLabel())
        );
    }

    @Test
    void writeReportreportTypeNameNotExists() {

        assertDoesNotThrow(() ->
            testInstance.writeReport(response, ReportTypeName.GENE_INTEREST.getLabel())
        );
    }

    @Test
    void writeReportReportNotExist() {

        lenient()
            .when(reportTypeService.reportTypeNameExists(ReportTypeName.GENE_INTEREST.getLabel()))
            .thenReturn(true);
        lenient().when(
            reportTypeService.reportTypeExistsInDatabase(ReportTypeName.GENE_INTEREST.getLabel()))
            .thenReturn(true);

        assertDoesNotThrow(() ->
            testInstance.writeReport(response, ReportTypeName.GENE_INTEREST.getLabel())
        );
    }
}