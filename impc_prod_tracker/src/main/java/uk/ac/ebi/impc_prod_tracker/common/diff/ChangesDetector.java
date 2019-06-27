package uk.ac.ebi.impc_prod_tracker.common.diff;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import java.lang.reflect.InvocationTargetException;
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
        this.fieldsToIgnore = fieldsToIgnore;
        this.fieldsToCheckRecursively = fieldsToCheckRecursively;
        this.oldObject = oldObject;
        this.newObject = newObject;
    }

    /**
     * Retrieves the changes.
     * @return List of {@link ChangeEntry} with the changes.
     */
    public List<ChangeEntry> getChanges()
    {
        return getChanges(fieldsToIgnore, fieldsToCheckRecursively, oldObject, newObject);
    }

    private List<ChangeEntry> getChanges(
        List<String> fieldsToIgnore,
        List<String> fieldsToCheckRecursively,
        Object oldObject,
        Object newObject)
    {
        List<ChangeEntry> changeEntries = new ArrayList<>();
        String propertyName = "";
        BeanMap beanMap = new BeanMap(oldObject);
        try
        {
            for (Object propertyNameObject : beanMap.keySet())
            {
                propertyName = (String) propertyNameObject;
                if (fieldsToIgnore.contains(propertyName))
                {
                    continue;
                }
                {
                    Object oldObjPropertyValue = propertyUtils.getProperty(oldObject, propertyName);
                    Object newObjPropertyValue = propertyUtils.getProperty(newObject, propertyName);
                    Class<?> type = propertyUtils.getPropertyType(oldObject, propertyName);

                    if (oldObjPropertyValue == null && newObjPropertyValue == null)
                    {
                        continue;
                    }

                    ChangeEntry changeEntry;

                    if (isASimpleValue(type))
                    {
                        changeEntry = checkDiffBetweenSimpleValues(
                            propertyName, oldObjPropertyValue, newObjPropertyValue);
                    }
                    else
                    {
                        changeEntry = checkDiffBetweenComplexValues(
                            propertyName, oldObjPropertyValue, newObjPropertyValue);
                        if (fieldsToCheckRecursively.contains(propertyName))
                        {
                            List<ChangeEntry> innerChangeEntries =
                                getChanges(fieldsToIgnore,
                                    fieldsToCheckRecursively,
                                    oldObjPropertyValue,
                                    newObjPropertyValue);
                            changeEntries.addAll(innerChangeEntries);
                            fieldsToCheckRecursively.remove(propertyName);
                        }
                    }
                    if (changeEntry != null)
                    {
                        changeEntries.add(changeEntry);
                    }
                }
            }
        } catch (Exception e)
        {
            LOGGER.error("Error analising differences between entities: " + e.getMessage()
                + ". Entity: "+ propertyName);
        }
        return changeEntries;
    }


    /**
     * Checks if a value is a simple value that can be compared with others using "equals"
     *
     * @param propertyType property type.
     * @return True if the value is simple.
     */
    private boolean isASimpleValue(Class<?> propertyType)
    {
        return BeanUtils.isSimpleValueType(propertyType);
    }

    /***
     * Evaluates if two values are different and if so, creates a ChangeEntry with the information.
     * @param propertyName Property name.
     * @param oldValue First value to compare.
     * @param newValue Second value to compare.
     * @return {@link ChangeEntry} object if there is change.
     */
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

    /**
     * Evaluates if two objects are different and if so, creates a ChangeEntry with the information.
     *
     * @param propertyName Property name.
     * @param oldValue     First value to compare.
     * @param newValue     Second value to compare.
     * @return {@link ChangeEntry} object if there is change.
     */
    private ChangeEntry checkDiffBetweenComplexValues(
        String propertyName, Object oldValue, Object newValue)
        throws IllegalAccessException, NoSuchMethodException, InvocationTargetException
    {
        ChangeEntry changeEntry = null;
        if (objectContainsIdAndNameProp(oldValue) && objectContainsIdAndNameProp(newValue))
        {
            Long oldId = (Long) propertyUtils.getProperty(oldValue, ID_PROPERTY);
            Long newId = (Long) propertyUtils.getProperty(newValue, ID_PROPERTY);
            if (!oldId.equals(newId))
            {
                String oldName = (String) propertyUtils.getProperty(oldValue, NAME_PROPERTY);
                String newName = (String) propertyUtils.getProperty(newValue, NAME_PROPERTY);

                changeEntry = buildChangeEntry(propertyName, oldName, newName);
            }
        }
        return changeEntry;
    }

    /**
     * Checks if the object contains both an id and name properties. Those properties can be used
     * to check if two objects are the same.
     *
     * @param object Object to evaluate.
     * @return True if the object contains the properties id and name.
     */
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
