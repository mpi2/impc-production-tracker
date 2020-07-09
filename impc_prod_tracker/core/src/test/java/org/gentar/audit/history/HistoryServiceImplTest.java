package org.gentar.audit.history;

import org.gentar.audit.diff.ChangeType;
import org.gentar.audit.history.detail.HistoryDetail;
import org.gentar.util.CollectionPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistoryServiceImplTest
{
    public static final LocalDateTime DATE = LocalDateTime.of(2020, 1, 1, 1, 1);
    private HistoryServiceImpl<Object> testInstance;

    @Mock
    private HistoryBuilder<Object> historyBuilder;
    @Mock
    private HistoryRepository historyRepository;

    @BeforeEach
    void setUp()
    {
        testInstance = new HistoryServiceImpl<>(historyBuilder, historyRepository);
    }

    @Test
    public void testDetectTrackOfChanges()
    {
        Object object1 = new Object();
        Object object2 = new Object();
        Long id = 1L;
        testInstance.detectTrackOfChanges(object1, object2, id);

        verify(historyBuilder, times(1)).buildHistoryEntry(object1, object2, id);
    }

    @Test
    public void testSaveTrackOfChangesHistoryNull()
    {
        testInstance.saveTrackOfChanges(null);

        verify(historyRepository, times(0)).save(any());
    }

    @Test
    public void testSaveTrackOfChangesHistoryNotNull()
    {
        History history = new History();
        testInstance.saveTrackOfChanges(history);

        verify(historyRepository, times(1)).save(history);
    }

    @Test
    public void testGetHistoryByEntityNameAndEntityId()
    {
        History history1 = buildHistory(1L, 100L, "entityName1");
        HistoryDetail historyDetail1 = buildHistoryDetail(1L, "field1", "oldValue", "newValue");
        history1.setHistoryDetailSet(Arrays.asList(historyDetail1));
        List<History> mockHistories = Arrays.asList(history1);

        when(historyRepository.findAllByEntityNameAndEntityIdOrderByDate("entityName1", 100L))
            .thenReturn(mockHistories);

        List<History> histories = testInstance.getHistoryByEntityNameAndEntityId("entityName1", 100L);

        assertThat("Expected 1 history record", histories.size(), is(1));
        History obtained = getHistoryById(1L, histories);
        assertThat(obtained, is(notNullValue()));
        assertThat(obtained.getEntityName(), is("entityName1"));
        assertThat(obtained.getEntityId(), is(100L));
        assertThat(obtained.getComment(), is("history-1"));
        List<HistoryDetail> historyDetails = obtained.getHistoryDetailSet();
        assertThat("Expected 1 history details record", historyDetails.size(), is(1));
        HistoryDetail historyDetail = getHistoryDetailByField("field1", historyDetails);
        assertThat(historyDetail, is(notNullValue()));
        assertThat(historyDetail.getId(), is(1L));
        assertThat(historyDetail.getOldValue(), is("oldValue"));
        assertThat(historyDetail.getNewValue(), is("newValue"));
        assertThat(historyDetail.getNote(), is(ChangeType.CHANGED_FIELD.getLabel()));
    }

    @Test
    public void testFilterDetailsInNestedEntityNoMatch()
    {
        History history1 = buildHistory(1L, 100L, "entityName1");
        HistoryDetail historyDetail1 = buildHistoryDetail(1L, "field1", "oldValue", "newValue");
        history1.setHistoryDetailSet(Arrays.asList(historyDetail1));

        History filtered = testInstance.filterDetailsInNestedEntity(history1, "noexisting", "noexisting");
        assertThat(filtered.getHistoryDetailSet(), is(history1.getHistoryDetailSet()));
    }

    @Test
    public void testFilterDetailsInNestedEntityMatch()
    {
        History history1 = buildHistory(1L, 100L, "entityName1");
        HistoryDetail historyDetail1 = buildHistoryDetail(1L, "field1", "oldValue1", "newValue1");
        HistoryDetail historyDetail2 = buildHistoryDetail(
            1L, "field1.nested.nestedField1", "oldValue2", "newValue2");
        HistoryDetail historyDetail3 =
            buildHistoryDetail(1L, "field1.nested.nestedField2", "oldValue3", "newValue3");
        history1.setHistoryDetailSet(Arrays.asList(historyDetail1, historyDetail2, historyDetail3));

        History filtered = testInstance.filterDetailsInNestedEntity(history1, "nested", "nestedField1");
        List<HistoryDetail> historyDetails = filtered.getHistoryDetailSet();
        assertThat(historyDetails.size(), is(2));
        HistoryDetail filteredHistoryDetail1 = getHistoryDetailByField("field1", historyDetails);
        assertThat(filteredHistoryDetail1, is(notNullValue()));
        HistoryDetail filteredHistoryDetail2 = getHistoryDetailByField("field1.nested.nestedField1", historyDetails);
        assertThat(filteredHistoryDetail2,is(notNullValue()));
    }

    @Test
    public void testFilterDetailsInNestedEntityFalseMatch()
    {
        History history1 = buildHistory(1L, 100L, "entityName1");
        HistoryDetail historyDetail1 =
            buildHistoryDetail(1L, "field1", "oldValue1 nestedField1", "newValue1 nested");
        HistoryDetail historyDetail2 = buildHistoryDetail(
            1L, "field1.nested.nestedField1", "oldValue2", "newValue2");
        HistoryDetail historyDetail3 =
            buildHistoryDetail(1L, "field1.nested.nestedField2", "oldValue3", "newValue3");
        HistoryDetail historyDetail4 =
            buildHistoryDetail(1L, "field1.collection[field=nested]", "oldValue3", "newValue3");
        history1.setHistoryDetailSet(Arrays.asList(historyDetail1, historyDetail2, historyDetail3, historyDetail4));

        History filtered = testInstance.filterDetailsInNestedEntity(history1, "nested", "nestedField1");
        List<HistoryDetail> historyDetails = filtered.getHistoryDetailSet();
        assertThat(historyDetails.size(), is(3));
        HistoryDetail filteredHistoryDetail1 = getHistoryDetailByField("field1", historyDetails);
        assertThat(filteredHistoryDetail1, is(notNullValue()));
        HistoryDetail filteredHistoryDetail2 =
            getHistoryDetailByField("field1.nested.nestedField1", historyDetails);
        assertThat(filteredHistoryDetail2,is(notNullValue()));
        HistoryDetail filteredHistoryDetail3 =
            getHistoryDetailByField("field1.nested.nestedField2", historyDetails);
        assertThat(filteredHistoryDetail3, is(nullValue()));
        HistoryDetail filteredHistoryDetail4 =
            getHistoryDetailByField("field1.collection[field=nested]", historyDetails);
        assertThat(filteredHistoryDetail4, is(notNullValue()));
    }

    private History buildHistory(Long id, Long entityId, String entityName)
    {
        History history = new History();
        history.setId(1L);
        history.setUser("User1");
        history.setDate(DATE);
        history.setComment("history-" + id);
        history.setEntityId(entityId);
        history.setEntityName(entityName);
        return history;
    }

    private HistoryDetail buildHistoryDetail(Long id, String field, String oldValue, String newValue)
    {
        HistoryDetail historyDetail = new HistoryDetail();
        historyDetail.setId(id);
        historyDetail.setField(field);
        historyDetail.setOldValue(oldValue);
        historyDetail.setNewValue(newValue);
        historyDetail.setNote(ChangeType.CHANGED_FIELD.getLabel());
        return historyDetail;
    }

    private History getHistoryById(Long id, List<History> histories)
    {
        History history = null;
        if (histories != null)
        {
            history = histories.stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
        }
        return history;
    }

    private HistoryDetail getHistoryDetailByField(String field, List<HistoryDetail> historyDetails)
    {
        HistoryDetail historyDetail = null;
        if (historyDetails != null)
        {
            historyDetail = historyDetails.stream().filter(x -> x.getField().equals(field))
                .findFirst().orElse(null);
        }
        return historyDetail;
    }

}