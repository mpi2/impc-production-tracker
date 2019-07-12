package uk.ac.ebi.impc_prod_tracker.common.diff;

import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
        this.fieldsToIgnore.addAll(Arrays.asList("class", "empty"));
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

    void printSimple()
    {
        getSimpleValues().forEach((k,v) -> {
            System.out.println(k + ": " + v.getValue());
        });
    }

    private void init()
    {
        checkedClassesTree.setRootClass(object.getClass());
        buildPropsMap(propValMap, object, null);
    }

    private PropertyValueData buildPropertyValueData(
        String propertyName, Object object, PropertyValueData parentPropertyValueData)
    {
        PropertyValueData propertyValueData = new PropertyValueData();
        Class<?> type = PropertyChecker.getPropertyType(object, propertyName);
        boolean mustSkip = mustSkipValueByType(type);
        if (mustSkip)
        {
            propertyValueData = null;
        }
        else
        {
            Object value = PropertyChecker.getValue(propertyName, object);
            String name = propertyName;

            if (parentPropertyValueData != null)
            {
                name = parentPropertyValueData.getName() + "." + name;
            }
            propertyValueData.setName(name);

            propertyValueData.setType(type);
            propertyValueData.setValue(value);
            propertyValueData.setSimpleValue(PropertyChecker.isASimpleValue(type));
        }

        return propertyValueData;
    }

    private void buildPropsMap(
        Map<String, PropertyValueData> map, Object object, PropertyValueData parentData)
    {
        BeanMap beanMap = new BeanMap(object);

        for (Object propertyNameObject : beanMap.keySet())
        {
            String property = (String) propertyNameObject;
            if (!mustIgnoredProperty(property))
            {
                checkProperty(object, parentData, map, (String) propertyNameObject);
            }
        }
    }

    private void checkProperty(
        Object object,
        PropertyValueData parentData,
        Map<String, PropertyValueData> map,
        String propertyName)
    {
        PropertyValueData propertyValueData = buildPropertyValueData(propertyName, object, parentData);

        if (propertyValueData != null)
        {
            evaluateProperty(object, propertyValueData, map);
        }
    }

    private boolean mustIgnoredProperty(String property)
    {
        return fieldsToIgnore.contains(property);
    }

    private boolean mustSkipValueByType(Class<?> type)
    {
        return PropertyChecker.isCollection(type);
    }

    private void evaluateProperty(
        Object object, PropertyValueData propertyValueData, Map<String, PropertyValueData> map)
    {
        map.put(propertyValueData.getName(), propertyValueData);

        if (shouldSearchRecursively(propertyValueData, object))
        {
            buildPropsMap(map, propertyValueData.getValue(), propertyValueData);
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
