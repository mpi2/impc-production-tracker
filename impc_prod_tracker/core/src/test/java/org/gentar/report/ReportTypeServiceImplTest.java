package org.gentar.report;

import static org.gentar.mockdata.MockData.reportTypeMockData;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;

@ExtendWith(MockitoExtension.class)
class ReportTypeServiceImplTest {

    @Mock
    private ReportTypeRepository reportTypeRepository;

    private MockHttpServletResponse response;

    ReportTypeServiceImpl testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new ReportTypeServiceImpl(reportTypeRepository);
        response = new MockHttpServletResponse();
    }

    @Test
    void createAllReportTypes() {

        lenient().when(
            reportTypeRepository.findReportTypeByNameIs(ReportTypeName.GENE_INTEREST.getLabel()))
            .thenReturn(reportTypeMockData());
        assertDoesNotThrow(() ->
            testInstance.createAllReportTypes()
        );
    }

    @Test
    void createSpecificReportType() {

        assertDoesNotThrow(() ->
            testInstance.createSpecificReportType(response, ReportTypeName.GENE_INTEREST.getLabel())
        );
    }

    @Test
    void createSpecificReportTypeReportTypeNameExists() {

        lenient().when(
            reportTypeRepository.findReportTypeByNameIs(ReportTypeName.GENE_INTEREST.getLabel()))
            .thenReturn(reportTypeMockData());

        assertDoesNotThrow(() ->
            testInstance.createSpecificReportType(response, ReportTypeName.GENE_INTEREST.getLabel())
        );
    }

    @Test
    void updateAllReportTypeDescriptions() {

        lenient().when(
            reportTypeRepository.findReportTypeByNameIs(ReportTypeName.GENE_INTEREST.getLabel()))
            .thenReturn(reportTypeMockData());

        assertDoesNotThrow(() ->
            testInstance.updateAllReportTypeDescriptions()
        );
    }

    @Test
    void updateReportTypeDescription() {

        assertDoesNotThrow(() ->
            testInstance
                .updateReportTypeDescription(response, ReportTypeName.GENE_INTEREST.getLabel())
        );
    }

    @Test
    void updateReportTypeDescriptionReportTypeExistsInDatabase() {
        lenient().when(
            reportTypeRepository.findReportTypeByNameIs(ReportTypeName.GENE_INTEREST.getLabel()))
            .thenReturn(reportTypeMockData());

        assertDoesNotThrow(() ->
            testInstance
                .updateReportTypeDescription(response, ReportTypeName.GENE_INTEREST.getLabel())
        );
    }

    @Test
    void updateAllReportTypePublicSettings() {

        lenient().when(
            reportTypeRepository.findReportTypeByNameIs(ReportTypeName.GENE_INTEREST.getLabel()))
            .thenReturn(reportTypeMockData());


        assertDoesNotThrow(() ->
            testInstance.updateAllReportTypePublicSettings()
        );
    }

    @Test
    void listPublicReportTypes() {

        lenient().when(
            reportTypeRepository.findAllByIsPublicIsTrue())
            .thenReturn(List.of(reportTypeMockData()));

        List<ReportType> reportTypes = testInstance.listPublicReportTypes();
        assertEquals(reportTypes.get(0).getId(), 1L);
    }

}