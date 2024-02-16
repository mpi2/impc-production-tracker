package org.gentar.audit.diff;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class detects the changes between 2 versions of an object. Both must be from the same type.
 * @param <T> Generic to guarantee we are comparing objects of the same type.
 */
public class ChangesDetector<T>
{
    private final List<String> fieldsToIgnore;
    private final T oldObject;
    private final T newObject;
    private Map<String, PropertyDescription> oldObjectPropertyData;
    private Map<String, PropertyDescription> newObjectPropertyData;
    private final List<ChangeEntry> changeEntries;

    /**
     * Detects the changes between 2 versions of an object.
     * @param fieldsToIgnore List with fields that need to be ignored in this process.
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
        Map<String, ChangeDetectionInput> input = buildInputMap();
        input.forEach((k, v) ->
        {
            List<ChangeEntry> changes = evaluateProperty(v);
            changeEntries.addAll(changes);
        });
        changeEntries.sort(Comparator.comparing(ChangeEntry::getProperty));
        return changeEntries;
    }

    /**
     * Builds the input for the process: A map with all the properties (both in the old and new
     * versions of the project) as keys and their values as value in the map.
     * We need to scan all the properties in case one object has more properties than the other
     * (this happens with collections).
     * @return A map with the properties and their values.
     */
    private Map<String, ChangeDetectionInput> buildInputMap()
    {
        Map<String, ChangeDetectionInput> inputMap = new HashMap<>();
        oldObjectPropertyData.forEach((k, v) ->
        {
            ChangeDetectionInput input =
                new ChangeDetectionInput(
                    k,
                    oldObjectPropertyData.get(k),
                    newObjectPropertyData.get(k),
                    oldObjectPropertyData.get(k).getType());
            inputMap.put(k, input);
        });
        newObjectPropertyData.forEach((k, v) ->
        {
            ChangeDetectionInput input =
                new ChangeDetectionInput(
                    k,
                    oldObjectPropertyData.get(k),
                    newObjectPropertyData.get(k),
                    newObjectPropertyData.get(k).getType());
            inputMap.put(k, input);
        });
        return inputMap;
    }

    private List<ChangeEntry> evaluateProperty(ChangeDetectionInput input)
    {
        List<ChangeEntry> changes = new ArrayList<>();
        String property = input.getProperty();
        PropertyDescription oldPropertyData = input.getOldPropertyData();
        PropertyDescription newPropertyData = input.getNewPropertyData();
        if (PropertyChecker.isASimpleValue(input.getType()))
        {
            ChangeEntry changeEntry = evaluateChange(property, oldPropertyData, newPropertyData);
            if (changeEntry != null)
            {
                changes.add(changeEntry);
            }
        }
        else
        {
            if (isElementInACollection(property))
            {
                ChangeEntry changeInCollection = detectChangeInCollections(property);
                if (changeInCollection != null)
                {
                    changes.add(changeInCollection);
                }
            }
        }
        return changes;
    }

    private boolean isElementInACollection(String property)
    {
        return property.endsWith("]");
    }

    private ChangeEntry buildChangeEntry(
        String propertyName, Class<?> type, Object oldValue, Object newValue, ChangeType changeType)
    {
        ChangeEntry changeEntry = new ChangeEntry();
        changeEntry.setProperty(propertyName);
        changeEntry.setOldValue(oldValue);
        changeEntry.setNewValue(newValue);
        changeEntry.setType(type);
        changeEntry.setChangeType(changeType);
        return changeEntry;
    }

    /**
     * Checks if a property that is an element in a collection has changes, been added or removed.
     * @param property Name of the property
     * @return ChangeEntry describing the change if any.
     */
    private ChangeEntry detectChangeInCollections(String property)
    {
        ChangeEntry changeEntry = null;
        PropertyDescription oldPropertyWithValue = oldObjectPropertyData.get(property);
        PropertyDescription newPropertyWithValue = newObjectPropertyData.get(property);
        if (oldPropertyWithValue != null && newPropertyWithValue != null)
        {
            changeEntry = evaluateChange(
                property, oldPropertyWithValue, newPropertyWithValue, ChangeType.CHANGED_ELEMENT);
        }
        else if (oldPropertyWithValue == null)
        {
            changeEntry =
                buildChangeEntry(
                    property,
                    newPropertyWithValue.getType(),
                    null,
                    newPropertyWithValue.getValue(),
                    ChangeType.ADDED);
        }
        else
        {
            changeEntry =
                buildChangeEntry(
                    property,
                    oldPropertyWithValue.getType(),
                    oldPropertyWithValue.getValue(),
                    null,
                    ChangeType.REMOVED);
        }
        return changeEntry;
    }

    private ChangeEntry evaluateChange(
        String property, PropertyDescription oldPropertyData, PropertyDescription newPropertyData)
    {
        return evaluateChange(property, oldPropertyData, newPropertyData, ChangeType.CHANGED_FIELD);
    }

    private ChangeEntry evaluateChange(
        String property,
        PropertyDescription oldPropertyData,
        PropertyDescription newPropertyData,
        ChangeType changeType
        )
    {
        Object oldValue = null;
        Object newValue = null;
        Class<?> type = null;
        if (oldPropertyData != null)
        {
            oldValue = oldPropertyData.getValue();
            type = oldPropertyData.getType();
        }
        if (newPropertyData != null)
        {
            newValue = newPropertyData.getValue();
            type = newPropertyData.getType();
        }
        ChangeEntry changeEntry = null;
        if (!Objects.equals(oldValue, newValue))
        {
            changeEntry = buildChangeEntry(property, type, oldValue, newValue, changeType);
        }
        return changeEntry;
    }

    /**
     * This class allows to represent the input for the process: a property and its values in both
     * the old version of the object and the new version
     */
    @Data
    @AllArgsConstructor
    private static class ChangeDetectionInput
    {
        // Name of the property (field).
        String property;
        // The metadata and value for the property in the old object.
        PropertyDescription oldPropertyData;
        // The metadata and value for the property in the new object.
        PropertyDescription newPropertyData;
        // Type of the property. It is also in oldPropertyData and newPropertyData but put here
        // to easy access and to avoid check in both fields in case one is null.
        Class<?> type;

        public String toString()
        {
            return property + ": [" + oldPropertyData + "] [ " + newPropertyData + "]";
        }
    }
}
