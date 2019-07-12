package uk.ac.ebi.impc_prod_tracker.common.diff;

import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Inspects an object and retrieves a map with each property (accessible with a get) as the key and
 * an {@link PropertyValueData} as value.
 * Properties that are part of a nested object take the parent property name and appends it to its
 * own name, allowing to know that the property is a nested one.
 */
class ObjectInspector
{
    private Map<String, PropertyValueData> propValMap;
    private Object object;
    private CheckedClassesTree checkedClassesTree = new CheckedClassesTree();
    private List<String> fieldsToIgnore;

    ObjectInspector(Object object, List<String> fieldsToIgnore)
    {
        Assert.notNull(object, "object is null");
        this.object = object;
        this.fieldsToIgnore = new ArrayList<>(fieldsToIgnore);
        this.fieldsToIgnore.add("class");
        propValMap = new HashMap<>();
        init();
    }

    Map<String, PropertyValueData> getPropertyValueMap()
    {
        return propValMap;
    }

    Map<String, PropertyValueData> getSimpleValues()
    {
        return propValMap.entrySet().stream()
            .filter(map -> map.getValue().isSimpleValue())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void init()
    {
        checkedClassesTree.setRootClass(object.getClass());
        propValMap = buildPropsMap(object, null);
    }

    private boolean isASimpleValue(Class<?> type)
    {
        return BeanUtils.isSimpleValueType(type);
    }

    private PropertyValueData buildPropertyValueDataByProperty(
        String propertyName, Object object, PropertyValueData parentPropertyValueData)
    {
        PropertyValueData propertyValueData = new PropertyValueData();
        Object value = PropertyChecker.getValue(propertyName, object);
        String name = propertyName;

        if (parentPropertyValueData != null)
        {
            name = parentPropertyValueData.getName() + "." + name;
        }
        propertyValueData.setName(name);
        Class<?> type = PropertyChecker.getPropertyType(object, propertyName);
        propertyValueData.setType(type);
        propertyValueData.setValue(value);
        propertyValueData.setSimpleValue(isASimpleValue(type));

        return propertyValueData;
    }

    private Map<String, PropertyValueData> buildPropsMap(
        Object object, PropertyValueData parentData)
    {
        Map<String, PropertyValueData> map = new HashMap<>();

        BeanMap beanMap = new BeanMap(object);

        for (Object propertyNameObject : beanMap.keySet())
        {
            String propertyName = (String) propertyNameObject;

            if (!fieldsToIgnore.contains(propertyName))
            {
                evaluateProperty(object, parentData, map, propertyName);
            }
        }
        return map;
    }

    private void evaluateProperty(
        Object object,
        PropertyValueData parentData,
        Map<String, PropertyValueData> map,
        String propertyName)
    {
        PropertyValueData propertyValueData =
            buildPropertyValueDataByProperty(propertyName, object, parentData);

        map.put(propertyValueData.getName(), propertyValueData);

        if (shouldSearchRecursively(propertyValueData, object))
        {
            Map<String, PropertyValueData> innerMap =
                buildPropsMap(propertyValueData.getValue(), propertyValueData);
            map.putAll(innerMap);
        }
    }

    private boolean shouldSearchRecursively(PropertyValueData propertyValueData, Object object)
    {
        boolean result = false;
        if (!propertyValueData.isSimpleValue())
        {
            result = classNotCheckedYet(object, propertyValueData);
        }
        return result;
    }

    private boolean classNotCheckedYet(Object object, PropertyValueData propertyValueData)
    {
        return checkedClassesTree.addRelation(propertyValueData.getType(), object.getClass());
    }
}
