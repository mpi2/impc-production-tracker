package uk.ac.ebi.impc_prod_tracker.common.diff;

import uk.ac.ebi.impc_prod_tracker.common.ObjectIdExtractor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Compares two collections and return a list? of ChangeEntry? with the changes
 */
class CollectionsComparator<T>
{
    private String collectionName;
    private Collection<? extends T> oldCollection;
    private Collection<? extends T> newCollection;

    private List<ChangeEntry> changes = new ArrayList<>();

    CollectionsComparator(
        String collectionName, Collection<? extends T> oldCollection, Collection<? extends T> newCollection)
    {
        this.collectionName = collectionName;
        this.oldCollection = oldCollection;
        this.newCollection = newCollection;
    }

    public List<ChangeEntry> getChanges()
    {
        detectDeletedElements();
        detectAddedElements();
        detectChangedElements();

        return changes;
    }

    private void detectChangedElements()
    {
        if (oldCollection != null)
        {
            oldCollection.forEach(oldElement ->
            {
                Long oldElementId = ObjectIdExtractor.getObjectId(oldElement);
                Optional<? extends T> newElementOpt = getElementWithId(newCollection, oldElementId);
                if (newElementOpt.isPresent())
                {
                    Object newElement = newElementOpt.get();
                    if (!oldElement.equals(newElement))
                    {
                        markAsChanged(oldElement, newElement);
                    }
                }
            });
        }
    }

    private void markAsChanged(T oldElement, Object newElement)
    {
        ChangeEntry changeEntry = buildBasicChangeInfo(oldElement);
        changeEntry.setOldValue(oldElement);
        changeEntry.setNewValue(newElement);
        changes.add(changeEntry);
    }

    private void detectDeletedElements()
    {
        if (oldCollection != null)
        {
            oldCollection.forEach(oldElement ->
            {
                Long oldElementId = ObjectIdExtractor.getObjectId(oldElement);
                if (!hasCollectionElementWithId(newCollection, oldElementId))
                {
                    markAsDeleted(oldElement);
                }
            });
        }
    }

    private void markAsDeleted(Object oldElement)
    {
        ChangeEntry changeEntry = buildBasicChangeInfo(oldElement);
        changeEntry.setOldValue(oldElement);
        changeEntry.setNewValue(null);
        changes.add(changeEntry);
    }

    private void detectAddedElements()
    {
        if (newCollection != null)
        {
            newCollection.forEach(newElement -> {
                Long newElementId = ObjectIdExtractor.getObjectId(newElement);
                if (!hasCollectionElementWithId(oldCollection, newElementId))
                {
                    markAsNew(newElement);
                }
            });
        }
    }

    private void markAsNew(Object newElement)
    {
        ChangeEntry changeEntry = buildBasicChangeInfo(newElement);
        changeEntry.setOldValue(null);
        changeEntry.setNewValue(newElement);
        changes.add(changeEntry);
    }

    private boolean hasCollectionElementWithId(Collection<? extends T> collection, Long id)
    {
        boolean result;
        if (collection == null)
        {
            result = false;
        } else
        {
            result = collection.stream().anyMatch(t -> ObjectIdExtractor.getObjectId(t).equals(id));
        }
        return result;
    }

    private Optional<? extends T> getElementWithId(Collection<? extends T> collection, Long id)
    {
        return collection.stream().filter(t -> ObjectIdExtractor.getObjectId(t).equals(id)).findFirst();
    }

    private ChangeEntry buildBasicChangeInfo(Object element)
    {
        ChangeEntry changeEntry = new ChangeEntry();
        changeEntry.setProperty(getPropertyNameForElement(element));
        changeEntry.setType(element.getClass());
        return changeEntry;
    }

    private String getPropertyNameForElement(Object oldElement)
    {
        return collectionName + "#" + ObjectIdExtractor.getObjectId(oldElement);
    }
}
