package uk.ac.ebi.impc_prod_tracker.common.history;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.impc_prod_tracker.common.diff.ChangeEntry;
import uk.ac.ebi.impc_prod_tracker.common.diff.ChangesDetector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class that takes two objects and gets a list of {@link ChangeDescription} objects with the changes.
 * It detects the cases where a change report in a property needs to keep a reference to an external
 * entity so the values can always be checked in case of posterior updates on that external entity.
 * @param <T> The type of the objects being compared.
 */
class HistoryChangesAdaptor<T>
{
    private List<String> fieldsToExclude;
    private T originalEntity;
    private T newEntity;

    private PropertyMapGrouper propertyMapGrouper;
    private List<ChangeDescription> changeDescriptions = new ArrayList<>();
    private Map<String, Map<String, ChangeEntry>> groupedChanges;
    private static final String ID_PROPERTY_NAME = "id";
    private static final Logger LOGGER = LoggerFactory.getLogger(HistoryChangesAdaptor.class);

    HistoryChangesAdaptor(List<String> fieldsToExclude, T originalEntity, T newEntity)
    {
        this.fieldsToExclude = fieldsToExclude;
        this.originalEntity = originalEntity;
        this.newEntity = newEntity;
        propertyMapGrouper = new PropertyMapGrouper();
    }

    public List<ChangeDescription> getChanges()
    {
        List<ChangeEntry> changeEntries = getObjectsChanges();
        groupedChanges = propertyMapGrouper.getGroupedChanges(changeEntries);
        groupedChanges.forEach(this::convertGroupToChangesDescription);
        return changeDescriptions;
    }


    private List<ChangeEntry> getObjectsChanges()
    {
        ChangesDetector<T> changesDetector =
            new ChangesDetector<>(fieldsToExclude, originalEntity, newEntity);
        return changesDetector.getChanges();
    }

    private void convertGroupToChangesDescription(
        String groupName, Map<String, ChangeEntry> propertyGroup)
    {
        if (PropertyMapGrouper.ROOT.equals(groupName))
        {
            addPropertiesForRootObject(propertyGroup);
        }
        else
        {
            addPropertiesForNestedObjects(groupName, propertyGroup);
        }
    }

    private void addPropertiesForNestedObjects(String groupName, Map<String, ChangeEntry> propertyGroup)
    {
        ChangeEntry groupRoot = propertyGroup.get(groupName);
        propertyGroup.forEach((k, v) ->
        {
            if (propertyNeedsEntityIdReference(groupName, k))
            {
                ChangeEntry idProperty = propertyGroup.get(groupName + "." + ID_PROPERTY_NAME);
                if (idProperty == null)
                {
                    LOGGER.error("There is not an id property in the group " + groupName + ". " +
                        "Probably the object evaluated does not have an id property that can be used."+
                        ". The evaluated properties are: " + propertyGroup);
                }
                ChangeDescription changeDescription = createChangeDescriptionWithReference(
                    groupRoot.getType().getSimpleName(), idProperty, v);
                changeDescriptions.add(changeDescription);
            }
        });
    }

    private void addPropertiesForRootObject(Map<String, ChangeEntry> propertyGroup)
    {
        propertyGroup.forEach((k, v) -> {
            if (!ID_PROPERTY_NAME.equals(k))
            {
                ChangeDescription changeDescription = createBasicChangeDescription(v);
                changeDescriptions.add(changeDescription);
            }
        });
    }

    private ChangeDescription createBasicChangeDescription(ChangeEntry change)
    {
        ChangeDescription changeDescription = new ChangeDescription();
        changeDescription.setProperty(change.getProperty());
        changeDescription.setOldValue(change.getOldValue());
        changeDescription.setNewValue(change.getNewValue());

        return changeDescription;
    }

    private ChangeDescription createChangeDescriptionWithReference(
        String groupParentName, ChangeEntry idPropertyChange, ChangeEntry change)
    {
        ChangeDescription changeDescription = createBasicChangeDescription(change);
        changeDescription.setParentClass(groupParentName);
        changeDescription.setOldValueId(idPropertyChange.getOldValue());
        changeDescription.setNewValueId(idPropertyChange.getNewValue());

        return changeDescription;
    }

    private boolean propertyNeedsEntityIdReference(String groupName, String property)
    {
        return !property.equals(groupName) && !property.endsWith("." + ID_PROPERTY_NAME);
    }
}
