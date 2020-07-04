package org.gentar.audit.history;

import org.gentar.audit.diff.ChangeEntry;
import org.gentar.exceptions.SystemOperationFailedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that manages conversion from ChangeEntry to ChangeDescription. It means to add
 * reference ids to changes in properties that are nested objects for other objects. We need to keep
 * the ids the object belongs to.
 */
public class ChangeDescriptionCreator
{
    private static final String ID_PROPERTY_NAME = "id";
    private static final String SEPARATOR = ".";
    private Map<String, ChangeEntry> map;

    public List<ChangeDescription> create(List<ChangeEntry> changeEntries)
    {
        List<ChangeDescription> changeDescriptions = new ArrayList<>();
        initMap(changeEntries);
        if (changeEntries != null)
        {
            for (ChangeEntry changeEntry : changeEntries)
            {
                ChangeDescription changeDescription = convertToChangeDescription(changeEntry);
                changeDescriptions.add(changeDescription);
            }
        }
        return changeDescriptions;
    }

    private void initMap(List<ChangeEntry> changeEntries)
    {
        map = new HashMap<>();
        if (changeEntries != null)
        {
            changeEntries.forEach(x -> map.put(x.getProperty(), x));
        }
    }

    private ChangeDescription convertToChangeDescription(ChangeEntry changeEntry)
    {
        Long oldValueId = null;
        Long newValueId = null;
        String parentClass = null;
        if (isNestedObject(changeEntry.getProperty()))
        {
            String parentName = getParentName(changeEntry.getProperty());
            ChangeEntry parentEntry = map.get(parentName);
            if (parentEntry != null)
            {
                oldValueId  = objectToLong(parentEntry.getOldValue());
                newValueId  = objectToLong(parentEntry.getNewValue());
                parentClass = parentName;
            }
        }
        ChangeDescription changeDescription = new ChangeDescription();
        changeDescription.setProperty(changeEntry.getProperty());
        changeDescription.setOldValue(changeEntry.getOldValue());
        changeDescription.setNewValue(changeEntry.getNewValue());
        changeDescription.setChangeType(changeEntry.getChangeType());
        changeDescription.setParentClass(parentClass);
        changeDescription.setOldValueId(oldValueId);
        changeDescription.setNewValueId(newValueId);
        return changeDescription;
    }

    private Long objectToLong(Object object)
    {
        Long result = null;
        if (object != null)
        {
            try
            {
                result = Long.parseLong(object.toString());
            }
            catch (NumberFormatException numberFormatException)
            {
                throw new SystemOperationFailedException(
                    "Error setting entity reference for nested object", "Object: " +  object);
            }

        }
        return  result;
    }

    private String getParentName(String property)
    {
        int lastPointIndex = property.lastIndexOf(SEPARATOR);
        return property.substring(0, lastPointIndex + 1) + ID_PROPERTY_NAME;
    }

    private boolean isNestedObject(String property)
    {
        return property.contains(SEPARATOR);
    }

}
