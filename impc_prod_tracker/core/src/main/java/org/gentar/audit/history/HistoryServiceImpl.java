package org.gentar.audit.history;

import org.gentar.audit.history.detail.HistoryDetail;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HistoryServiceImpl<T> implements HistoryService<T>
{
    private final HistoryBuilder<T> historyBuilder;
    private final HistoryRepository historyRepository;

    private static final String FIELD_NAME_SEPARATOR = ".";

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
        if(!additionalHistories.isEmpty()) {
            histories.addAll(additionalHistories);
        }
        return histories;
    }

    private List<History> getAdditionalRecordsForExternalReferences(List<History> histories)
    {
        List<History> additionalHistories = new ArrayList<>();
        histories.forEach(history ->
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
                }));
        return additionalHistories;
    }

    private List<History> removeDuplicates(List<History> list)
    {
        return list.stream().distinct().collect(Collectors.toList());
    }

    private List<History> getOlderChangesForReferenceEntity(
        String field, LocalDateTime date, String referenceEntity, String entityId)
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
                    newHistory.setHistoryDetailSet(List.of(hd));
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
        if (field.contains(nestedEntityFieldName + FIELD_NAME_SEPARATOR))
        {
            String fieldToKeepInNested = nestedEntityFieldName + FIELD_NAME_SEPARATOR + fieldName;
            if (field.contains(nestedEntityFieldName))
            {
                result = field.contains(fieldToKeepInNested);
            }
        }
        return result;
    }
}
