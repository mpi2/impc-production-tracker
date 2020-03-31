package org.gentar.audit.diff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class detects the changes between 2 versions of an object. Both must be from the same type.
 * @param <T> Generic to guarantee we are comparing objects of the same type.
 */

public class ChangesDetector<T>
{
    private List<String> fieldsToIgnore;
    private T oldObject;
    private T newObject;

    private Map<String, PropertyDescription> oldObjectPropertyData;
    private Map<String, PropertyDescription> newObjectPropertyData;

    private List<ChangeEntry> changeEntries;

    /**
     * Detects the changes between 2 versions of an object.
     * @param fieldsToIgnore Some properties can be ignored in the process.
     *        evaluated. In the last case, the name of the property needs to be added in this list.
     * @param oldObject The first version to compare.
     * @param newObject The second version to compare.
     */
    public ChangesDetector(
        List<String> fieldsToIgnore, T oldObject, T newObject)
    {
        this.fieldsToIgnore = new ArrayList<>(fieldsToIgnore);
        this.oldObject = oldObject;
        this.newObject = newObject;
        initObjectInspectors();
        changeEntries = buildChanges();
    }

    private void initObjectInspectors()
    {
        ObjectInspector oldObjectInspector = new ObjectInspector(oldObject, fieldsToIgnore);
        oldObjectPropertyData = oldObjectInspector.getMap();

        ObjectInspector newObjectInspector = new ObjectInspector(newObject, fieldsToIgnore);
        newObjectPropertyData = newObjectInspector.getMap();
    }

    /**
     * Returns a list of ChangeEntry with the changes between the the old and new objects.
     * @return ChangeEntry list with changes. Empty if no changes.
     */
    public List<ChangeEntry> getChanges()
    {
        return changeEntries;
    }

    private List<ChangeEntry> buildChanges()
    {
        List<ChangeEntry> changeEntries = new ArrayList<>();

        oldObjectPropertyData.forEach((k, v) ->
        {
            List<ChangeEntry> changes = evaluateProperty(
                k, oldObjectPropertyData.get(k), newObjectPropertyData.get(k));
            changeEntries.addAll(changes);
        });

        return changeEntries;
    }

    private List<ChangeEntry> evaluateProperty(
        String property, PropertyDescription oldPropertyData, PropertyDescription newPropertyData)
    {
        List<ChangeEntry> changes = new ArrayList<>();


        if (PropertyChecker.isCollection(oldPropertyData.getType()))
        {
            Collection oldCollection = (Collection) oldPropertyData.getValue();
            Collection newCollection = (Collection) newPropertyData.getValue();
            CollectionsComparator<?> collectionsComparator =
                new CollectionsComparator(property, oldCollection, newCollection);
            changes = collectionsComparator.getChanges();
            if (!changes.isEmpty())
            {
                changes.add(buildChangeEntry(property, oldPropertyData, newPropertyData));
            }
        }
        else
        {
            if (!Objects.equals(oldPropertyData.getValue(), newPropertyData.getValue()))
            {
                changes.add(buildChangeEntry(property, oldPropertyData, newPropertyData));
            }
        }
        return changes;
    }

    private ChangeEntry buildChangeEntry(
        String propertyName, PropertyDescription oldPropertyData, PropertyDescription newPropertyData)
    {
        ChangeEntry changeEntry = new ChangeEntry();
        changeEntry.setProperty(propertyName);
        changeEntry.setOldValue(oldPropertyData.getValue());
        changeEntry.setNewValue(newPropertyData.getValue());
        changeEntry.setType(oldPropertyData.getType());

        return changeEntry;
    }

    public void print()
    {
        changeEntries.forEach(x ->
        {
            System.out.println(x.getProperty() + " old[" + x.getOldValue() + "] new[" + x.getNewValue() + "]");
        });
    }
}
