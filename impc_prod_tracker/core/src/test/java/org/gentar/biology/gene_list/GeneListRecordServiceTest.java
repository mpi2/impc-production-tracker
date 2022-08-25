package org.gentar.biology.gene_list;

import static org.gentar.mockdata.MockData.MGI_00000001;
import static org.gentar.mockdata.MockData.TEST_NAME;
import static org.gentar.mockdata.MockData.consortiumMockData;
import static org.gentar.mockdata.MockData.geneByListRecordMockData;
import static org.gentar.mockdata.MockData.geneListListMockData;
import static org.gentar.mockdata.MockData.listRecordMockData;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.gentar.biology.gene_list.filter.GeneListFilter;
import org.gentar.biology.gene_list.record.GeneByListRecordRepository;
import org.gentar.biology.gene_list.record.ListRecord;
import org.gentar.biology.gene_list.record.ListRecordRepository;
import org.gentar.biology.project.search.filter.FilterTypes;
import org.gentar.exceptions.UserOperationFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class GeneListRecordServiceTest {

    @Mock
    private ListRecordRepository listRecordRepository;
    @Mock
    private GeneByListRecordRepository geneByListRecordRepository;

    GeneListRecordService testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new GeneListRecordService(listRecordRepository,
            geneByListRecordRepository);
    }

    @Test
    void getGeneListRecordById() {
        lenient().when(listRecordRepository.findById(1L))
            .thenReturn(Optional.of(listRecordMockData()));
        ListRecord listRecord = testInstance.getGeneListRecordById(1L);
        assertEquals(listRecord.getId(), 1L);
    }

    @Test
    void getAllByConsortium() {
        Pageable pageable = PageRequest.of(0, 1);
        final Page<ListRecord> page = new PageImpl<>(List.of(listRecordMockData()));
        lenient().when(listRecordRepository.findAllByGeneListConsortiumName(pageable, TEST_NAME))
            .thenReturn(page);
        final Page<ListRecord> listRecords = testInstance.getAllByConsortium(pageable, TEST_NAME);
        assertEquals(listRecords.getTotalElements(), 1);
    }

    @Test
    void getAllNotPaginated() {
        GeneListFilter filter = GeneListFilter.getInstance();
        filter.setConsortiumName(TEST_NAME);
        filter.setVisible(true);
        Map<FilterTypes, List<String>> filterTypesListMap = new HashMap<>();
        filterTypesListMap.put(FilterTypes.VISIBLE, List.of(MGI_00000001));
        filter.setFilters(filterTypesListMap);
        lenient().when(listRecordRepository.findAll(Mockito.any(Specification.class)))
            .thenReturn(List.of(listRecordMockData()));
        List<ListRecord> listRecord = testInstance.getAllNotPaginated(filter);
        assertEquals(listRecord.get(0).getId(), 1L);
    }

    @Test
    void getAllStream() {
        GeneListFilter filter = GeneListFilter.getInstance();
        filter.setConsortiumName(TEST_NAME);
        filter.setVisible(true);
        Map<FilterTypes, List<String>> filterTypesListMap = new HashMap<>();
        filterTypesListMap.put(FilterTypes.VISIBLE, List.of(MGI_00000001));
        filter.setFilters(filterTypesListMap);

        lenient().when(
            listRecordRepository.stream(Mockito.any(Specification.class), eq(ListRecord.class)))
            .thenReturn(Stream.of(listRecordMockData()));
        Stream<ListRecord> listRecordStream = testInstance.getAllStream(filter);
        assertEquals(listRecordStream.toList(), List.of(listRecordMockData()));
    }

    @Test
    void getAllBySpecs() {

        GeneListFilter filter = GeneListFilter.getInstance();
        filter.setConsortiumName(TEST_NAME);
        filter.setVisible(true);
        Map<FilterTypes, List<String>> filterTypesListMap = new HashMap<>();
        filterTypesListMap.put(FilterTypes.VISIBLE, List.of(MGI_00000001));
        filter.setFilters(filterTypesListMap);

        Pageable pageable = PageRequest.of(0, 1);
        final Page<ListRecord> page = new PageImpl<>(List.of(listRecordMockData()));
        lenient().when(listRecordRepository.findAll(Mockito.any(Specification.class), eq(pageable)))
            .thenReturn(page);
        final Page<ListRecord> listRecords = testInstance.getAllBySpecs(pageable, filter);
        assertEquals(listRecords.getTotalElements(), 1);
    }

    @Test
    void getAllAccIdsByConsortiumId() {

        lenient().when(geneByListRecordRepository.getAllAccIdsByConsortiumId(1L)).thenReturn(
            List.of(MGI_00000001));

        List<String> accIds = testInstance.getAllAccIdsByConsortiumId(1L);
        assertEquals(accIds, List.of(MGI_00000001));
    }

    @Test
    void genesByRecordToString() {
        String accId =
            testInstance.genesByRecordToString(List.of(geneByListRecordMockData()));
        assertEquals(accId, MGI_00000001 + "-");

    }

    @Test
    void validateNewRecord() {
        Map<String, Long> geneRecordHashes = new HashMap<>();
        geneRecordHashes.put("ID", 1L);
        ListRecord listRecord = listRecordMockData();
        listRecord.setGenesByRecord(Set.of(geneByListRecordMockData()));

        assertDoesNotThrow(() ->
            testInstance.validateNewRecord(listRecord, geneRecordHashes, MGI_00000001)
        );
    }

    @Test
    void validateNewRecordIdNull() {
        Map<String, Long> geneRecordHashes = new HashMap<>();
        geneRecordHashes.put("MGI:00000001-", 2L);
        ListRecord listRecord = listRecordMockData();
        listRecord.setGenesByRecord(Set.of(geneByListRecordMockData()));

        Exception exception = assertThrows(UserOperationFailedException.class, () -> {
            testInstance.validateNewRecord(listRecord, geneRecordHashes, MGI_00000001);
        });

        String expectedMessage =
            "Gene(s) [" + MGI_00000001 + "] already in this list.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }
}