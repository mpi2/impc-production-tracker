package uk.ac.ebi.impc_prod_tracker.common.history;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.conf.security.abac.spring.SubjectRetriever;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity_;
import uk.ac.ebi.impc_prod_tracker.data.common.history.History;
import uk.ac.ebi.impc_prod_tracker.data.common.history.detail.HistoryDetail;

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
    private String entityName;
    private Long entityId;

    public HistoryBuilder(SubjectRetriever subjectRetriever)
    {
        this.subjectRetriever = subjectRetriever;
    }

    public void setEntityName(String entityName)
    {
        this.entityName = entityName;
    }

    public void setEntityId(Long entityId)
    {
        this.entityId = entityId;
    }

    /**
     * Builds a {@link History} record with the information about the changes executed in an entity
     * as part as a update.
     * @param originalEntity The entity before the update.
     * @param newEntity The updated entity.
     * @return {@link History} object with the information or null if no changes where detected.
     */
    public History buildHistoryEntry(T originalEntity, T newEntity)
    {
        History history = new History();
        List<HistoryDetail> historyDetails = getDetails(originalEntity, newEntity);
        if (historyDetails.isEmpty())
        {
            history = null;
        }
        else
        {
            history.setEntityId(entityId);
            history.setEntityName(entityName);
            history.setDate(LocalDateTime.now());

            history.setHistoryDetailSet(historyDetails);
            history.setUser(subjectRetriever.getSubject().getLogin());
        }

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
                historyDetail.setOldValue(x.getOldValue().toString());
                historyDetail.setNewValue(x.getNewValue().toString());
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
