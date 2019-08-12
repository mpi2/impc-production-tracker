package uk.ac.ebi.impc_prod_tracker.common.history;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.common.history.History;
import uk.ac.ebi.impc_prod_tracker.data.common.history.HistoryRepository;
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
        return historyRepository.findAllByEntityNameAndEntityIdOrderByDate(entityName, entityId);
    }

    @Override
    public void setEntityData(String entityName, Long entityId)
    {
        historyBuilder.setEntityName(entityName);
        historyBuilder.setEntityId(entityId);
    }
}
