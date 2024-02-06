package org.gentar.biology.gene_list;

import org.gentar.biology.gene.external_ref.GeneExternalService;
import org.gentar.biology.gene_list.record.ListRecord;
import org.gentar.biology.gene_list.record.ListRecordTypeService;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.mockdata.MockData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;


@ExtendWith(MockitoExtension.class)
class GeneListCsvConverterTest {

    @Mock
    private GeneExternalService geneExternalService;
    @Mock
    private GeneListRecordService geneListRecordService;
    @Mock
    private ListRecordTypeService listRecordTypeService;


    public final static String CSV_GENE_HEADER = "Genes";
    public final static String CSV_NOTE_HEADER = "Note";
    public final static String CSV_TYPE_HEADER = "Type";
    public final static String CSV_VISIBLE_HEADER = "Visible";

    public final static String CSV_GENE = "MGI:00000001";
    public final static String CSV_NOTE = "Note";
    public final static String CSV_TYPE = "Type";
    public final static String CSV_VISIBLE = "Visible";

    private final static String HEADER_NOT_FOUND_ERROR = "Header %s not found in csv file.";

    GeneListCsvConverter testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new GeneListCsvConverter(geneExternalService,
            geneListRecordService,
            listRecordTypeService);
    }

    @Test
    void processCsvContentGeneHeaderEmpty() {

        Exception exception = assertThrows(UserOperationFailedException.class, () -> testInstance.processCsvContent(
            List.of(List.of(CSV_NOTE_HEADER, CSV_TYPE_HEADER, CSV_VISIBLE_HEADER))));

        String expectedMessage =
            String.format(HEADER_NOT_FOUND_ERROR, CSV_GENE_HEADER);
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    void processCsvContentNoteHeaderEmpty() {

        Exception exception = assertThrows(UserOperationFailedException.class, () -> testInstance.processCsvContent(
            List.of(List.of(CSV_GENE_HEADER, CSV_TYPE_HEADER, CSV_VISIBLE_HEADER))));

        String expectedMessage =
            String.format(HEADER_NOT_FOUND_ERROR, CSV_NOTE_HEADER);

        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void processCsvContentTypeHeaderEmpty() {

        Exception exception = assertThrows(UserOperationFailedException.class, () -> testInstance.processCsvContent(
            List.of(List.of(CSV_GENE_HEADER, CSV_NOTE_HEADER, CSV_VISIBLE_HEADER))));

        String expectedMessage =
            String.format(HEADER_NOT_FOUND_ERROR, CSV_TYPE_HEADER);

        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void processCsvContent() {

        Map<String, List<String>> recordsByColumns =
            testInstance.processCsvContent(List.of(
                List.of(CSV_GENE_HEADER, CSV_NOTE_HEADER, CSV_TYPE_HEADER, CSV_VISIBLE_HEADER),
                List.of(CSV_GENE, CSV_NOTE, CSV_TYPE, CSV_VISIBLE)));

        assertEquals(recordsByColumns.size(), 4);
    }

    @Test
    void buildListFromCsvContentNotMatchMGI() {

        String consortiumName = "WTSI";
        Map<String, List<String>> recordsByColumns = new HashMap<>();
        Map<String, Long> sortedAccIdsInCurrentLis = new HashMap<>();

        recordsByColumns.put(CSV_GENE_HEADER, List.of(CSV_GENE));
        recordsByColumns.put(CSV_NOTE_HEADER, List.of(CSV_NOTE));
        recordsByColumns.put(CSV_TYPE_HEADER, List.of(CSV_TYPE));
        recordsByColumns.put(CSV_VISIBLE_HEADER, List.of(CSV_VISIBLE));

        Exception exception = assertThrows(UserOperationFailedException.class, () -> {
            List<ListRecord> listData =
                testInstance.buildListFromCsvContent(consortiumName, recordsByColumns,
                    sortedAccIdsInCurrentLis);
        });

        String expectedMessage =
            CSV_GENE + " is not a valid gene symbol.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void buildListFromCsvContenListRecordTypeNull() {


        String consortiumName = "WTSI";
        Map<String, List<String>> recordsByColumns = new HashMap<>();
        Map<String, Long> sortedAccIdsInCurrentLis = new HashMap<>();

        recordsByColumns.put(CSV_GENE_HEADER, List.of(CSV_GENE));
        recordsByColumns.put(CSV_NOTE_HEADER, List.of(CSV_NOTE));
        recordsByColumns.put(CSV_TYPE_HEADER, List.of(CSV_TYPE));
        recordsByColumns.put(CSV_VISIBLE_HEADER, List.of(CSV_VISIBLE));

        Map<String, String> AccIdsBySymbols = new HashMap<>();
        AccIdsBySymbols.put(CSV_GENE.toLowerCase(), CSV_GENE.toLowerCase());

        lenient().when(geneExternalService.getAccIdsBySymbolsBulk(List.of(CSV_GENE)))
            .thenReturn(AccIdsBySymbols);

        Exception exception = assertThrows(UserOperationFailedException.class, () -> {
            List<ListRecord> listData =
                testInstance.buildListFromCsvContent(consortiumName, recordsByColumns,
                    sortedAccIdsInCurrentLis);
        });

        String expectedMessage =
            "Record type Type does not exist in consortium WTSI.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void buildListFromCsvContent() {

        String consortiumName = "WTSI";
        Map<String, List<String>> recordsByColumns = new HashMap<>();
        Map<String, Long> sortedAccIdsInCurrentLis = new HashMap<>();

        recordsByColumns.put(CSV_GENE_HEADER, List.of(CSV_GENE));
        recordsByColumns.put(CSV_NOTE_HEADER, List.of(CSV_NOTE));
        recordsByColumns.put(CSV_TYPE_HEADER, List.of(CSV_TYPE));
        recordsByColumns.put(CSV_VISIBLE_HEADER, List.of(CSV_VISIBLE));

        Map<String, String> AccIdsBySymbols = new HashMap<>();
        AccIdsBySymbols.put(CSV_GENE.toLowerCase(), CSV_GENE.toLowerCase());

        lenient().when(geneExternalService.getAccIdsBySymbolsBulk(List.of(CSV_GENE)))
            .thenReturn(AccIdsBySymbols);

        lenient().when(listRecordTypeService.getRecordTypeByTypeNameAndConsortiumName("Type", "WTSI"))
            .thenReturn(MockData.listRecordTypeMockData());


        List<ListRecord> listData =
            testInstance.buildListFromCsvContent(consortiumName, recordsByColumns,
                sortedAccIdsInCurrentLis);

        assertEquals(listData.size(), 1L);

    }
}