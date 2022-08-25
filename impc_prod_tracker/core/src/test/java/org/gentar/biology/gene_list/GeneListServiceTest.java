package org.gentar.biology.gene_list;

import static org.gentar.mockdata.MockData.MGI_00000001;
import static org.gentar.mockdata.MockData.TEST_NAME;
import static org.gentar.mockdata.MockData.consortiumMockData;
import static org.gentar.mockdata.MockData.geneByListRecordMockData;
import static org.gentar.mockdata.MockData.geneListListMockData;
import static org.gentar.mockdata.MockData.geneListMockData;
import static org.gentar.mockdata.MockData.geneListProjectionsMockData;
import static org.gentar.mockdata.MockData.listRecordMockData;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.gentar.biology.gene_list.filter.GeneListFilter;
import org.gentar.biology.gene_list.record.GeneListProjection;
import org.gentar.biology.gene_list.record.ListRecord;
import org.gentar.organization.consortium.ConsortiumService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class GeneListServiceTest {

    @Mock
    private GeneListRepository geneListRepository;
    @Mock
    private GeneListRecordService geneListRecordService;
    @Mock
    private GeneListCsvConverter geneListCsvConverter;
    @Mock
    private ConsortiumService consortiumService;

    GeneListService testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new GeneListService(geneListRepository,
            geneListRecordService,
            geneListCsvConverter,
            consortiumService);
    }

    @Test
    void getAll() {
        lenient().when(geneListRepository.findAll()).thenReturn(geneListListMockData());
        List<GeneList> geneLists = testInstance.getAll();
        assertEquals(geneLists.get(0).getId(), 1L);
    }

    @Test
    void getGeneListByConsortium() {
        lenient().when(geneListRepository.findByConsortium(consortiumMockData()))
            .thenReturn(geneListMockData());
        GeneList geneList = testInstance.getGeneListByConsortium(consortiumMockData());
        assertEquals(geneList.getId(), 1L);
    }

    @Test
    void getAllWithFilters() {
        Pageable pageable = PageRequest.of(0, 1);
        GeneListFilter filter = GeneListFilter.getInstance();
        final Page<ListRecord> page = new PageImpl<>(List.of(listRecordMockData()));
        lenient().when(geneListRecordService.getAllBySpecs(pageable, filter)).thenReturn(page);
        final Page<ListRecord> listRecords = testInstance.getAllWithFilters(pageable, filter);
        assertEquals(listRecords.getTotalElements(), 1);
    }

    @Test
    void getAllNotPaginatedWithFilters() {
        Pageable pageable = PageRequest.of(0, 1);
        GeneListFilter filter = GeneListFilter.getInstance();
        lenient().when(geneListRecordService.getAllNotPaginated(filter))
            .thenReturn(List.of(listRecordMockData()));
        List<ListRecord> listRecords = testInstance.getAllNotPaginatedWithFilters(filter);
        assertEquals(listRecords.get(0).getId(), 1);
    }

    @Test
    void getAllStream() {
        Pageable pageable = PageRequest.of(0, 1);
        GeneListFilter filter = GeneListFilter.getInstance();
        lenient().when(geneListRecordService.getAllStream(filter))
            .thenReturn(Stream.of(listRecordMockData()));
        Stream<ListRecord> listRecords = testInstance.getAllStream(filter);
        assertEquals(listRecords.toList(), List.of(listRecordMockData()));
    }

    @Test
    void getAllAccIdsByConsortiumId() {

        lenient().when(geneListRecordService.getAllAccIdsByConsortiumId(1L))
            .thenReturn(List.of(MGI_00000001));
        List<String> accIds = testInstance.getAllAccIdsByConsortiumId(1L);
        assertEquals(accIds, List.of(MGI_00000001));
    }

    @Test
    void updateRecordsInList() {
        ListRecord listRecord =new ListRecord();
        listRecord.setId(1L);
        listRecord.setGenesByRecord(Set.of(geneByListRecordMockData()));
        GeneList geneList =new GeneList();
        geneList.setId(1L);
        geneList.setListRecords(List.of(listRecord));

        lenient().when(consortiumService.getConsortiumByNameOrThrowException(TEST_NAME))
            .thenReturn(consortiumMockData());

        lenient().when(geneListRepository.findByConsortium(consortiumMockData()))
            .thenReturn(geneList);

        assertDoesNotThrow(() ->
            testInstance.updateRecordsInList(List.of(listRecordMockData()), TEST_NAME)
        );
    }

    @Test
    void updateRecordsInListGeneListEmpty() {
        lenient().when(consortiumService.getConsortiumByNameOrThrowException(TEST_NAME))
            .thenReturn(consortiumMockData());

        assertDoesNotThrow(() ->
            testInstance.updateRecordsInList(List.of(listRecordMockData()), TEST_NAME)
        );

    }

    @Test
    void deleteRecordsInList() {
        lenient().when(consortiumService.getConsortiumByNameOrThrowException(TEST_NAME))
            .thenReturn(consortiumMockData());

        lenient().when(geneListRepository.findByConsortium(consortiumMockData()))
            .thenReturn(new GeneList());

        assertDoesNotThrow(() ->
            testInstance.deleteRecordsInList(List.of(1L), TEST_NAME)
        );
    }

    @Test
    void getGeneListProjectionsByConsortiumName() {

        lenient().when(geneListRepository.findAllGeneListProjectionsByConsortiumName(TEST_NAME))
            .thenReturn(List.of(geneListProjectionsMockData()));

        List<GeneListProjection> listRecords =
            testInstance.getGeneListProjectionsByConsortiumName(TEST_NAME);
        assertEquals(listRecords.get(0).getId(), 1);
    }

    @Test
    void updateListWithCsvContent() {
        lenient().when(consortiumService.getConsortiumByNameOrThrowException(TEST_NAME))
            .thenReturn(consortiumMockData());
        assertDoesNotThrow(() ->
            testInstance.updateListWithCsvContent(TEST_NAME, List.of(List.of(TEST_NAME)))
        );
    }

    @Test
    void updateListWithCsvContentListRecordEmpty() {

        assertDoesNotThrow(() ->
            testInstance.updateListWithCsvContent(TEST_NAME, List.of(List.of(TEST_NAME)))
        );
    }

    @Test
    void genesByRecordToString() {

        String accId =
            testInstance.genesByRecordToString(List.of(geneByListRecordMockData()));
        assertEquals(accId, MGI_00000001+"-");
    }
}