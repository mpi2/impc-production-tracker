package org.gentar.audit.history;

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
