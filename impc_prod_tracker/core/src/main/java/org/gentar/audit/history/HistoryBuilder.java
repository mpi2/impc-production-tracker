package org.gentar.audit.history;

import org.gentar.BaseEntity_;
import org.gentar.security.abac.subject.SubjectRetriever;
import org.springframework.stereotype.Component;
import org.gentar.audit.history.detail.HistoryDetail;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that creates a History entry detecting the changes in a entity.
 */

@Component
public class HistoryBuilder<T>
{
    private SubjectRetriever subjectRetriever;
    private static final String RESOURCE_PRIVACY_PROPERTY_NAME = "resourcePrivacy";
    private static final String RESTRICTED_OBJECT_PROPERTY_NAME = "restrictedObject";

    public HistoryBuilder(SubjectRetriever subjectRetriever)
    {
        this.subjectRetriever = subjectRetriever;
    }

    /**
     * Builds a {@link History} record with the information about the changes executed in an entity
     * as part as a update.
     * @param originalEntity The entity before the update.
     * @param newEntity The updated entity.
     * @param id The id of the entity in the system.
     * @return {@link History} object with the information or null if no changes where detected.
     */
    public History buildHistoryEntry(T originalEntity, T newEntity, Long id)
    {
        History history = new History();
        List<HistoryDetail> historyDetails = getDetails(originalEntity, newEntity);
        if (historyDetails.isEmpty())
        {
            history = null;
        }
        else
        {
            history.setEntityId(id);
            history.setEntityName(originalEntity.getClass().getSimpleName());
            history.setDate(LocalDateTime.now());
            history.setComment(originalEntity.getClass().getSimpleName() + " updated");
            history.setHistoryDetailSet(historyDetails);
            history.setUser(subjectRetriever.getSubject().getLogin());
        }

        return history;
    }

    /**
     * Creates a {@link History} entity with information about the creation of an entity in the system.
     * @param entity The entity whose creation is being audited.
     * @param id The id of the entity.Entity can be any class so we cannot just use a method to
     *           get the id.
     * @return A record saying that the entity was created. There is NO details as that is used to
     *          log the changes done in an object (update rather than creation).
     */
    public History buildCreationHistoryEntry(T entity, Long id)
    {
        History history = new History();
        history.setEntityId(id);
        history.setEntityName(entity.getClass().getSimpleName());
        history.setDate(LocalDateTime.now());
        history.setComment(entity.getClass().getSimpleName() + " created");
        history.setUser(subjectRetriever.getSubject().getLogin());
        return history;
    }

    /**
     * Detects the changes between the plan before and after its change.
     * @param originalEntity The entity before the update.
     * @param newEntity The updated entity.
     * @return List of strings with the detected changes.
     */
    private List<HistoryDetail> getDetails(T originalEntity, T newEntity)
    {
        List<String> fieldsToExclude = new ArrayList<>();
        fieldsToExclude.add(BaseEntity_.CREATED_AT);
        fieldsToExclude.add(BaseEntity_.CREATED_BY);
        fieldsToExclude.add(BaseEntity_.LAST_MODIFIED);
        fieldsToExclude.add(BaseEntity_.LAST_MODIFIED_BY);
        fieldsToExclude.add(RESOURCE_PRIVACY_PROPERTY_NAME);
        fieldsToExclude.add(RESTRICTED_OBJECT_PROPERTY_NAME);

        HistoryChangesAdaptor<T> historyChangesAdaptor =
            new HistoryChangesAdaptor<>(fieldsToExclude, originalEntity, newEntity);

        List<ChangeDescription> changeDescriptions = historyChangesAdaptor.getChanges();

        List<HistoryDetail> details = new ArrayList<>();
        changeDescriptions.forEach(x ->
            {
                HistoryDetail historyDetail = new HistoryDetail();
                historyDetail.setField(x.getProperty());
                historyDetail.setOldValue(x.getOldValue() == null ? null : x.getOldValue().toString());
                historyDetail.setNewValue(x.getNewValue() == null ? null : x.getNewValue().toString());
                historyDetail.setReferenceEntity(x.getParentClass());
                historyDetail.setOldValueEntityId(
                    x.getOldValueId() == null ? null : x.getOldValueId().toString());
                historyDetail.setNewValueEntityId(
                    x.getNewValueId() == null ? null : x.getNewValueId().toString());

                details.add(historyDetail);
            });

        return details;
    }
}
