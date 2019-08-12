package uk.ac.ebi.impc_prod_tracker.common.history;

import uk.ac.ebi.impc_prod_tracker.data.common.history.History;
import java.util.List;

/**
 * Class to manage the historic information of an entity.
 */
public interface HistoryService<T>
{
    History detectTrackOfChanges(T originalEntity, T newEntity);
    void saveTrackOfChanges(History historyEntry);
    List<History> getHistoryByEntityNameAndEntityId(String EntityName, Long EntityId);
    void setEntityData(String entityName, Long entityId);
}
