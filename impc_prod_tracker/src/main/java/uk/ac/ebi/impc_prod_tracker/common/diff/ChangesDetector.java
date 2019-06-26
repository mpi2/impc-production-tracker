package uk.ac.ebi.impc_prod_tracker.common.diff;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ChangesDetector<T>
{
    private List<String> fieldsToIgnore;
    private T oldObject;
    private T newObject;
    private static PropertyUtilsBean propertyUtils = new PropertyUtilsBean();

    public ChangesDetector(List<String> fieldsToIgnore, T oldObject, T newObject)
    {
        this.fieldsToIgnore = fieldsToIgnore;
        this.oldObject = oldObject;
        this.newObject = newObject;
    }

    public List<ChangeEntry> getChanges()
    {
        List<ChangeEntry> changeEntries = new ArrayList<>();
        BeanMap beanMap = new BeanMap(oldObject);
        try
        {
            for (Object propertyNameObject : beanMap.keySet())
            {
                String propertyName = (String) propertyNameObject;
                if (fieldsToIgnore.contains(propertyName))
                {
                    continue;
                }
                {
                    Object oldObjPropertyValue = propertyUtils.getProperty(oldObject, propertyName);
                    Object newObjPropertyValue = propertyUtils.getProperty(newObject, propertyName);

                    if (oldObjPropertyValue == null && newObjPropertyValue == null)
                    {
                        continue;
                    }
                    ChangeEntry changeEntry;
                    if (isASimpleValue(oldObjPropertyValue))
                    {
                        changeEntry = checkDiffBetweenSimpleValues(
                            propertyName, oldObjPropertyValue, newObjPropertyValue);
                    } else
                    {
                        changeEntry = checkDiffBetweenComplexValues(
                            propertyName, oldObjPropertyValue, newObjPropertyValue);
                    }
                    if (changeEntry != null)
                    {
                        changeEntries.add(changeEntry);
                    }

                }
            }
        } catch (Exception e)
        {
        }
        return changeEntries;
    }

    /**
     * Checks if a value is a simple value that can be compared with others using operators
     * like = or !=.
     *
     * @param propertyValue The value to evaluate.
     * @return True if the value is simple.
     */
    private boolean isASimpleValue(Object propertyValue)
    {
        return BeanUtils.isSimpleValueType(propertyValue.getClass());
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
        if (oldValue != newValue)
        {
            changeEntry = buildChangeEntry(propertyName, oldValue, newValue);
        }
        return changeEntry;
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
            Long oldId = (Long) propertyUtils.getProperty(oldValue, "id");
            Long newId = (Long) propertyUtils.getProperty(newValue, "id");
            if (oldId != newId)
            {
                String oldName = (String) propertyUtils.getProperty(oldValue, "name");
                String newName = (String) propertyUtils.getProperty(newValue, "name");

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
        return propertyUtils.isReadable(object, "id") && propertyUtils.isReadable(object, "name");
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
