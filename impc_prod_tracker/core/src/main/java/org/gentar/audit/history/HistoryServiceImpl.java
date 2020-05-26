package org.gentar.audit.history;

import org.springframework.stereotype.Component;
import org.gentar.audit.history.detail.HistoryDetail;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    public History detectTrackOfChanges(T originalEntity, T newEntity, Long id)
    {
        return historyBuilder.buildHistoryEntry(originalEntity, newEntity, id);
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
        additionalHistories = removeDuplicates(additionalHistories);
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

    private List<History> removeDuplicates(List<History> list)
    {
        return list.stream().distinct().collect(Collectors.toList());
    }

    private List<History> getOlderChangesForReferenceEntity(
        String field, LocalDate date, String referenceEntity, String entityId)
    {
        List<History> result = new ArrayList<>();
        if (entityId !=null)
        {
            List<History> histories =
                historyRepository.findAllByEntityNameAndEntityIdAndDateAfter(
                    referenceEntity, Long.parseLong(entityId), date);

            histories.forEach(h ->
            {
                List<HistoryDetail> historyDetails = h.getHistoryDetailSet();
                String wantedRecordField = field.substring(field.lastIndexOf(".") + 1);
                HistoryDetail hd = findRecordInDetailsWithField(wantedRecordField, historyDetails);
                if (hd != null)
                {
                    History newHistory = new History();
                    newHistory.setUser(h.getUser());
                    newHistory.setComment(h.getComment());
                    newHistory.setHistoryDetailSet(Arrays.asList(hd));
                    newHistory.setDate(h.getDate());
                    result.add(newHistory);
                }
            });
        }
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
    public History buildCreationTrack(T entity, Long id)
    {
        return historyBuilder.buildCreationHistoryEntry(entity, id);
    }

    @Override
    public History filterDetailsInNestedEntity(
        History history, String nestedEntityFieldName, String fieldName)
    {
        if (history != null)
        {
            List<HistoryDetail> details = history.getHistoryDetailSet();
            if (details != null)
            {
                details = details.
                    stream().filter(x -> detailPassesFilter(x, nestedEntityFieldName, fieldName))
                    .collect(Collectors.toList());
            }
            history.setHistoryDetailSet(details);
        }
        return history;
    }

    public boolean detailPassesFilter(
        HistoryDetail historyDetail, String nestedEntityFieldName, String fieldName)
    {
        boolean result = true;
        String field = historyDetail.getField();
        String fieldToKeepInNested = nestedEntityFieldName + "." + fieldName;
        if (field.contains(nestedEntityFieldName))
        {
            result = field.contains(fieldToKeepInNested);
        }
        return result;
    }
}
