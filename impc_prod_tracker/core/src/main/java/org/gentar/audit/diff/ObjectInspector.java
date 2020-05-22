package org.gentar.audit.diff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class in charge of inspecting an object and retrieve its properties and the values of those
 * properties.
 */
class ObjectInspector
{
    private Map<String, PropertyDescription> map;
    private Object object;
    private CheckedClassesTree checkedClassesTree;
    private List<String> fieldsToIgnore;

    ObjectInspector(Object object, List<String> fieldsToIgnore)
    {
        this.object = object;
        this.fieldsToIgnore = new ArrayList<>(fieldsToIgnore);
        this.checkedClassesTree = new CheckedClassesTree();
        map = new HashMap<>();
        checkedClassesTree.setRootClass(object.getClass());
        check(object.getClass(), object, null);
    }

    /**
     * Analysis the properties (fields) and its values in an object
     * @param type The type of the object.
     * @param object Object to analyse.
     * @param parentData Parent object if the object is a nested one.
     */
    private void check(Class<?> type, Object object, PropertyDefinition parentData)
    {
        List<PropertyDefinition> properties = PropertyChecker.getPropertiesDataByType(type);
        String parentName = getParentName(parentData);
        for (PropertyDefinition prop : properties)
        {
            PropertyDescription data = getPropertyData(prop, object, parentName);
            if (data != null)
            {
                prop.setName(data.getName());
                map.put(data.getName(), data);
                if (mustCheckInternalProperties(data, parentData))
                {
                    PropertyDefinition currentParent = getCurrentParent(parentData);
                    checkedClassesTree.addRelationIfNotExist(data.getType(), currentParent.getType());
                    check(data.getType(), data.getValue(), prop);
                }
            }
        }
    }

    private PropertyDefinition getCurrentParent(PropertyDefinition parentData)
    {
        PropertyDefinition propertyDefinition = parentData;
        if (parentData == null)
        {
            propertyDefinition = new PropertyDefinition(null, object.getClass());
        }
        return propertyDefinition;
    }

    private String getParentName(PropertyDefinition parentData)
    {
        String parentName = null;
        if (parentData != null)
        {
            parentName = parentData.getName();
        }
        return parentName;
    }

    private PropertyDescription getPropertyData(
        PropertyDefinition property, Object object, String parentName)
    {
        PropertyDescription propertyDescription = null;
        if (!mustIgnoreProperty(property.getName()))
        {
            propertyDescription = buildProperty(property, object, parentName);
        }
        return propertyDescription;
    }

    private PropertyDescription buildProperty(
        PropertyDefinition property, Object object, String parentName)
    {
        PropertyEvaluator propertyEvaluator = new PropertyEvaluator(property, object, parentName);
        propertyEvaluator.evaluate();
        return propertyEvaluator.getData();
    }

    public Map<String, PropertyDescription> getMap()
    {
        return map;
    }

    private boolean mustIgnoreProperty(String property)
    {
        return fieldsToIgnore.contains(property);
    }

    private boolean mustCheckInternalProperties(PropertyDescription data, PropertyDefinition parentData)
    {
        return !data.isSimpleValue() && !isCircularReference(data, parentData);
    }

    private boolean isCircularReference(PropertyDescription data, PropertyDefinition parentData)
    {
        return !checkedClassesTree.canRelationBeAdded(
            data.getType(), getCurrentParent(parentData).getType());
    }


    Map<String, Object> getValuesForSimpleProperties()
    {
        Map<String, Object> simpleProps = new HashMap<>(map.size());
        for (Map.Entry<String, PropertyDescription> entry : map.entrySet())
        {
            if (entry.getValue().isSimpleValue())
            {
                simpleProps.put(entry.getKey(), entry.getValue().getValue());
            }
        }
        return simpleProps;
    }

    void printSimple()
    {
        getValuesForSimpleProperties().forEach((k, v) -> {
            System.out.println(k + ": " + v);
        });
    }
}
