package org.gentar.audit.diff;

import org.gentar.util.CollectionPrinter;

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
    // Map for a field (property) and the value and meta info for it.
    private final Map<String, PropertyDescription> map;
    // Object being evaluated.
    private final Object object;
    // Mechanism to avoid circular dependencies by remembering which classes have been evaluated.
    private final CheckedClassesTree checkedClassesTree;
    // Fields that are not needed for the analysis.
    private final List<String> fieldsToIgnore;

    private final PropertyDescriptionExtractor propertyDescriptionExtractor;

    ObjectInspector(Object object, List<String> fieldsToIgnore)
    {
        this.object = object;
        this.fieldsToIgnore = new ArrayList<>(fieldsToIgnore);
        this.checkedClassesTree = new CheckedClassesTree();
        map = new HashMap<>();
        checkedClassesTree.setRootClass(object.getClass());
        propertyDescriptionExtractor = new PropertyDescriptionExtractor();
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
        List<PropertyDescription> propertiesWithValues;

        if (PropertyChecker.isCollection(type))
        {
            propertiesWithValues = propertyDescriptionExtractor.buildByCollection(object, parentData);
        }
        else
        {
            propertiesWithValues = propertyDescriptionExtractor.buildByType(type, object, parentData);
        }

        for (PropertyDescription propertyWithValue : propertiesWithValues)
        {
            if (!mustIgnoreProperty(propertyWithValue.getName()))
            {
                map.put(propertyWithValue.getPropertyDefinition().getName(), propertyWithValue);
                if (mustCheckInternalProperties(propertyWithValue, parentData))
                {
                   // PropertyDefinition currentParent = getCurrentParent(parentData);
                    Class<?> parentTypeToRegister = getParentTypeToRegister(parentData);

                    checkedClassesTree.addRelationIfNotExist(
                        propertyWithValue.getType(), parentTypeToRegister);
                    check(
                        propertyWithValue.getType(),
                        propertyWithValue.getValue(),
                        propertyWithValue.getPropertyDefinition());
                }
            }
        }
    }

    private Class<?> getParentTypeToRegister(PropertyDefinition parentData)
    {
        Class<?> parentTypeToRegister = null;
        if (parentData == null)
        {
            parentTypeToRegister = object.getClass();
        }
        else
        {
            // If the current element is the child of a collection, we don't want to register in the
            // dependencies tree the relation children->Collection but children->Owner of the collection
            if (PropertyChecker.isCollection(parentData.getType()))
            {
                parentTypeToRegister = parentData.getParentType();
            }
            else
            {
                parentTypeToRegister = parentData.getType();
            }
        }
        return parentTypeToRegister;
        /*if (parentData != null)
        {
            // If the current element is the child of a collection, we don't want to register in the
            // dependencies tree the relation children->Collection but children->Owner of the collection
            if (PropertyChecker.isCollection(parentData.getType()))
            {
                parentTypeToRegister = parentData.getParentType();
            }
            else
            {
                parentTypeToRegister = parentData.getType();
            }
        }
        return parentTypeToRegister;
        /*
        PropertyDefinition propertyDefinition = parentData;
        if (parentData == null)
        {
            propertyDefinition = new PropertyDefinition(null, object.getClass(), null);
        }
        else
        {
            // If the current element is the child of a collection, we don't want to register in the
            // dependencies tree the relation children->Collection but children->Owner of the collection
            if (PropertyChecker.isCollection(parentData.getType()))
            {
                propertyDefinition.setType(parentData.getParentType());
            }
        }
        return propertyDefinition;*/
    }

    public Map<String, PropertyDescription> getMap()
    {
        return map;
    }

    private boolean mustIgnoreProperty(String property)
    {
        int lastIndexForDot = property.lastIndexOf(".");
        String shortPropertyName = property.substring(lastIndexForDot + 1);
        return fieldsToIgnore.contains(shortPropertyName);
    }

    // Determines if we need to recursively evaluate the data in the property

    private boolean mustCheckInternalProperties(PropertyDescription data, PropertyDefinition parentData)
    {
        return !data.isSimpleValue() && !isCircularReference(data, parentData);
    }

    private boolean isCircularReference(PropertyDescription data, PropertyDefinition parentData)
    {
        Class<?> parentTypeToRegister = getParentTypeToRegister(parentData);
        boolean canRelationBeAdded =
            checkedClassesTree.canRelationBeAdded(data.getType(), parentTypeToRegister);

        return !canRelationBeAdded;
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
}
