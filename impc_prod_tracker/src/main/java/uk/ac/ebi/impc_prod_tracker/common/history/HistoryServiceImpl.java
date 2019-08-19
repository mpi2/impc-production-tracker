package uk.ac.ebi.impc_prod_tracker.common.history;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.common.history.History;
import uk.ac.ebi.impc_prod_tracker.data.common.history.HistoryRepository;
import uk.ac.ebi.impc_prod_tracker.data.common.history.detail.HistoryDetail;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class HistoryServiceImpl<T> implements HistoryService<T>
{
    private HistoryBuilder<T> historyBuilder;
    private HistoryRepository historyRepository;

    public HistoryServiceImpl(HistoryBuilder<T> historyBuilder, HistoryRepository historyRepository)
    {
        this.historyBuilder = historyBuilder;
        this.historyRepository = historyRepository;
    }

    @Override
    public History detectTrackOfChanges(T originalEntity, T newEntity)
    {
        return historyBuilder.buildHistoryEntry(originalEntity, newEntity);
    }

    @Override
    public void saveTrackOfChanges(History historyEntry)
    {
        if (historyEntry != null)
        {
            historyRepository.save(historyEntry);
        }
    }

    @Override
    public List<History> getHistoryByEntityNameAndEntityId(String entityName, Long entityId)
    {
        List<History> histories =
            historyRepository.findAllByEntityNameAndEntityIdOrderByDate(entityName, entityId);
        List<History> additionalHistories = getAdditionalRecordsForExternalReferences(histories);
        histories.addAll(additionalHistories);
        return histories;
    }

    private List<History> getAdditionalRecordsForExternalReferences(List<History> histories)
    {
        List<History> additionalHistories = new ArrayList<>();
        histories.forEach(history ->
        {
            history.getHistoryDetailSet().forEach(detail ->
            {
                if (detail.getReferenceEntity() != null)
                {
                    List<History> refHistoryForOldValues =
                        getOlderChangesForReferenceEntity(
                            detail.getField(),
                            history.getDate(),
                            detail.getReferenceEntity(),
                            detail.getOldValueEntityId());
                    additionalHistories.addAll(refHistoryForOldValues);

                    List<History> refHistoryForNewValues =
                        getOlderChangesForReferenceEntity(
                            detail.getField(),
                            history.getDate(),
                            detail.getReferenceEntity(),
                            detail.getNewValueEntityId());
                    additionalHistories.addAll(refHistoryForNewValues);

                }
            });
        });
        return additionalHistories;
    }

    private List<History> getOlderChangesForReferenceEntity(
        String field, LocalDateTime date, String referenceEntity, String entityId)
    {
        List<History> histories =
            historyRepository.findAllByEntityNameAndEntityIdAndDateAfter(
                referenceEntity, Long.parseLong(entityId), date);
        List<History> result = new ArrayList<>();
        histories.forEach(h ->
            {
                List<HistoryDetail> historyDetails = h.getHistoryDetailSet();
                String wantedRecordField = field.substring(field.lastIndexOf(".") + 1);
                HistoryDetail hd = findRecordInDetailsWithField(wantedRecordField, historyDetails);
                if (hd != null)
                {
                    History newHistory = new History();
                    newHistory.setUser(h.getUser());
                    newHistory.setAction(h.getAction());
                    newHistory.setComment(h.getComment());
                    newHistory.setHistoryDetailSet(Arrays.asList(hd));
                    newHistory.setDate(h.getDate());
                    result.add(newHistory);
                }
            });
        return result;

    }

    private HistoryDetail findRecordInDetailsWithField(
        String wantedRecordField, List<HistoryDetail> historyDetails)
    {
        for (HistoryDetail historyDetail : historyDetails)
        {
            if (wantedRecordField.equals(historyDetail.getField()))
                return historyDetail;
        }
        return null;
    }

    @Override
    public void setEntityData(String entityName, Long entityId)
    {
        historyBuilder.setEntityName(entityName);
        historyBuilder.setEntityId(entityId);
    }
}
