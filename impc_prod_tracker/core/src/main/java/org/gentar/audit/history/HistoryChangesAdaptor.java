package org.gentar.audit.history;

import org.gentar.audit.diff.ChangeEntry;
import org.gentar.audit.diff.ChangesDetector;
import org.gentar.audit.diff.PropertyChecker;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Class that takes two objects and gets a list of {@link ChangeDescription} objects with the changes.
 * It detects the cases where a change report in a property needs to keep a reference to an external
 * entity so the values can always be checked in case of posterior updates on that external entity.
 *
 * @param <T> The type of the objects being compared.
 */
class HistoryChangesAdaptor<T>
{
    private final List<String> fieldsToExclude;
    private final T originalEntity;
    private final T newEntity;

    HistoryChangesAdaptor(T originalEntity, T newEntity)
    {
        this(Collections.singletonList(""), originalEntity, newEntity);
    }

    HistoryChangesAdaptor(List<String> fieldsToExclude, T originalEntity, T newEntity)
    {
        this.fieldsToExclude = fieldsToExclude;
        this.originalEntity = originalEntity;
        this.newEntity = newEntity;
    }

    public List<ChangeDescription> getChanges()
    {
        List<ChangeEntry> changeEntries = getObjectsChanges();
        changeEntries = filterUnwantedChanges(changeEntries);
        ChangeDescriptionCreator changeDescriptionCreator = new ChangeDescriptionCreator();
        return changeDescriptionCreator.create(changeEntries);
    }

    /**
     * Remove unwanted changes.
     *
     * @param changeEntries Original list of changes.
     * @return Filtered changes
     */
    private List<ChangeEntry> filterUnwantedChanges(List<ChangeEntry> changeEntries)
    {
        List<ChangeEntry> filteredData = new ArrayList<>();
        for (ChangeEntry changeEntry : changeEntries)
        {
            if (!comparingNullAndEmptyCollections(changeEntry))
            {
                filteredData.add(changeEntry);
            }
        }
        return filteredData;
    }

    private boolean comparingNullAndEmptyCollections(ChangeEntry changeEntry)
    {
        boolean result = false;
        if (PropertyChecker.isCollection(changeEntry.getType()))
        {
            Collection newValue = (Collection) changeEntry.getNewValue();
            Collection oldValue = (Collection) changeEntry.getOldValue();
            result =
                (newValue == null && oldValue != null && oldValue.isEmpty())
                    || (newValue != null && newValue.isEmpty() && oldValue == null);
        }
        return result;
    }

    private List<ChangeEntry> getObjectsChanges()
    {
        ChangesDetector<T> changesDetector =
            new ChangesDetector<>(fieldsToExclude, originalEntity, newEntity);
        return changesDetector.getChanges();
    }
}
