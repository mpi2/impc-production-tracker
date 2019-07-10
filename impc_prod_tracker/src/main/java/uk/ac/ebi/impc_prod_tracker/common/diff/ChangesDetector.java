package uk.ac.ebi.impc_prod_tracker.common.diff;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class detects the changes between 2 versions of an object. Both must be from the same type.
 * @param <T> Generic to guarantee we are comparing objects of the same type.
 */

public class ChangesDetector<T>
{
    private List<String> fieldsToIgnore;
    private List<String> fieldsToCheckRecursively;
    private T oldObject;
    private T newObject;
    private static PropertyUtilsBean propertyUtils = new PropertyUtilsBean();
    private static final String ID_PROPERTY = "id";
    private static final String NAME_PROPERTY = "name";
    private static final Logger LOGGER = LoggerFactory.getLogger(ChangesDetector.class);

    private String currentPropertyName = null;

    private List<ChangeEntry> changeEntries = new ArrayList<>();

    /**
     * Detects the changes between 2 versions of an object.
     * @param fieldsToIgnore Some properties can be ignored in the process.
     * @param fieldsToCheckRecursively Some nested objects can be ignored but others should be
     *        evaluated. In the last case, the name of the property needs to be added in this list.
     * @param oldObject The first version to compare.
     * @param newObject The second version to compare.
     */
    public ChangesDetector(
        List<String> fieldsToIgnore, List<String> fieldsToCheckRecursively, T oldObject, T newObject)
    {
        this.fieldsToIgnore = new ArrayList<>(fieldsToIgnore);
        this.fieldsToCheckRecursively = new ArrayList<>(fieldsToCheckRecursively);
        this.oldObject = oldObject;
        this.newObject = newObject;
    }

    /**
     * Retrieves the changes.
     * @return List of {@link ChangeEntry} with the changes.
     */
    public List<ChangeEntry> getChanges()
    {
        BeanMap beanMap = new BeanMap(oldObject);

        for (Object propertyNameObject : beanMap.keySet())
        {
            currentPropertyName = (String) propertyNameObject;
            checkChangesInCurrentProperty(currentPropertyName);
        }
        return changeEntries;
    }

    private void checkChangesInCurrentProperty(String property)
    {
        Object oldValue = getCurrentValue(property, oldObject);
        Object newValue = getCurrentValue(property, newObject);

        if (shouldSkipCurrentProperty(property, oldValue, newValue))
        {
            LOGGER.info("Property " + property + " skipped");
        }
        else
        {
            ChangeEntry changeEntry;
            Class<?> type = getTypeCurrentProperty();

            if (isASimpleValue(type))
            {
                changeEntry = checkDiffBetweenSimpleValues(property, oldValue, newValue);
            }
            else
            {
                changeEntry = checkDiffBetweenComplexValues(property, oldValue, newValue);

                if (isPropertyMaskedAsRecursive(property))
                {
                    searchChangesInInnerProperty(oldValue, newValue);
                }
            }
            if (changeEntry != null)
            {
                changeEntries.add(changeEntry);
            }
        }
    }

    private void searchChangesInInnerProperty(Object oldValue, Object newValue)
    {
        ChangesDetector innerPropertyChangesDetector =
            new ChangesDetector<>(
                fieldsToIgnore,
                fieldsToCheckRecursively,
                oldValue,
                newValue);
        List<ChangeEntry> innerChangeEntries = innerPropertyChangesDetector.getChanges();
        changeEntries.addAll(innerChangeEntries);
    }

    private Class<?> getTypeCurrentProperty()
    {
        Class<?> type = null;
        try
        {
            type = propertyUtils.getPropertyType(oldObject, currentPropertyName);
        }
        catch(Exception e)
        {

        }
        return type;
    }

    private Object getCurrentValue(String property, Object currentObject)
    {
        Object currentValue = null;
        try
        {
            currentValue = propertyUtils.getProperty(currentObject, property);
        }
        catch (Exception e)
        {
            LOGGER.error("Error setting current value for property: " + e.getMessage()
                + ". Property: "+ property);
        }
        return currentValue;
    }

    private boolean shouldSkipCurrentProperty(String property, Object oldValue, Object newValue)
    {
        return isPropertyMaskedAsIgnored(property) || oldAndNewValuesAreSameObject(oldValue, newValue);
    }

    private boolean isPropertyMaskedAsIgnored(String property)
    {
        return fieldsToIgnore.contains(property);
    }
    private boolean isPropertyMaskedAsRecursive(String property)
    {
        return fieldsToCheckRecursively.contains(property);
    }

    private boolean oldAndNewValuesAreSameObject(Object oldValue, Object newValue)
    {
        return oldValue == newValue;
    }

    private boolean isASimpleValue(Class<?> propertyType)
    {
        return BeanUtils.isSimpleValueType(propertyType);
    }

    private ChangeEntry checkDiffBetweenSimpleValues(
        String propertyName, Object oldValue, Object newValue)
    {
        ChangeEntry changeEntry = null;
        if (areValuesDifferent(oldValue, newValue))
        {
            changeEntry = buildChangeEntry(propertyName, oldValue, newValue);
        }
        return changeEntry;
    }

    private boolean areValuesDifferent(Object value1, Object value2)
    {
        return (value1 == null && value2 != null )
            || (value1 != null && value2 == null)
            || !Objects.equals(value1, value2);
    }

    private ChangeEntry checkDiffBetweenComplexValues(
        String propertyName, Object oldValue, Object newValue)
    {
        ChangeEntry changeEntry = null;

        if (objectContainsIdAndNameProp(oldValue) && objectContainsIdAndNameProp(newValue))
        {
            Long oldId = (Long) getCurrentValue(ID_PROPERTY, oldValue);
            Long newId = (Long) getCurrentValue(ID_PROPERTY, newValue);
            if (!oldId.equals(newId))
            {
                String oldName = (String) getCurrentValue(NAME_PROPERTY, oldValue);
                String newName = (String) getCurrentValue(NAME_PROPERTY, newValue);

                changeEntry = buildChangeEntry(propertyName, oldName, newName);
            }
        }

        return changeEntry;
    }

    private boolean objectContainsIdAndNameProp(Object object)
    {
        return propertyUtils.isReadable(object, ID_PROPERTY)
            && propertyUtils.isReadable(object, NAME_PROPERTY);
    }

    private ChangeEntry buildChangeEntry(String propertyName, Object oldValue, Object newValue)
    {
        ChangeEntry changeEntry = new ChangeEntry();
        changeEntry.setProperty(propertyName);
        changeEntry.setOldValue(String.valueOf(oldValue));
        changeEntry.setNewValue(String.valueOf(newValue));

        return changeEntry;
    }
}
