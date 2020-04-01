package org.gentar.audit.history;

import java.util.List;

/**
 * Class to manage the historic information of an entity.
 */
public interface HistoryService<T>
{
    History detectTrackOfChanges(T originalEntity, T newEntity, Long id);
    void saveTrackOfChanges(History historyEntry);
    List<History> getHistoryByEntityNameAndEntityId(String EntityName, Long EntityId);
    History buildCreationTrack(T entity, Long id);

    /**
     * Sometimes the history has too much information for nested objects, specially when
     * that nested object is a "type": If the parent entity is assigned with a new "type", we are
     * interested mainly in the name (or description) of that new type, not in all its attributes.
     * This method removes extra information for a nested object (identified by nestedEntityFieldName).
     * Extra information is information not including "fieldName".
     * @param history History record.
     * @param fieldName The information in the nested entity we want to keep.
     * @return History with details modified to show less information for nested entities.
     */
    History filterDetailsInNestedEntity(History history, String nestedEntityFieldName, String fieldName);
}
