package org.gentar.audit.diff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class has the responsibility to create a list of {@link PropertyDescription}, that means,
 * fields, values and metadata about the fields.
 * It has two ways to do it:
 * 1) Using reflection to analyse the object: get the fields and then their values.
 * 2) If the object is a collection, then each field is an identifier in each element and the
 * value is the element itself. ( The value can be a simple object or another object).
 */
public class PropertyDescriptionExtractor
{
    public static final String NAME_SEPARATOR = ".";

    public List<PropertyDescription> buildByCollection(Object object, PropertyDefinition parentData)
    {
        List<PropertyDescription> propertyDescriptions = new ArrayList<>();
        if (object != null)
        {
            Collection objectAsCollection = (Collection) object;
            for (Object element : objectAsCollection)
            {
                PropertyDefinition propertyDefinition = new PropertyDefinition();
                String parentName = getParentName(parentData);
                String propertyCompleteName =
                    buildPropertyName(getElementInCollectionName(element), parentName);
                propertyDefinition.setName(propertyCompleteName);
                propertyDefinition.setType(element.getClass());
                PropertyDescription propertyWithValue = new PropertyDescription();
                propertyWithValue.setPropertyDefinition(propertyDefinition);
                propertyWithValue.setParentType(object.getClass());
                propertyWithValue.setSimpleValue(PropertyChecker.isASimpleValue(propertyDefinition.getType()));
                propertyWithValue.setValue(element);
                propertyDescriptions.add(propertyWithValue);
            }
        }
        return propertyDescriptions;
    }

    private String getElementInCollectionName(Object element)
    {
        String name;
        if (PropertyChecker.isASimpleValue(element.getClass()))
        {
            name = element.toString();
        }
        else
        {
            Long id = ObjectIdExtractor.getObjectId(element);
            if (id == null)
            {
                name = element.toString();
            }
            else
            {
                name = id + "";
            }
        }
        return "["+ name + "]";
    }

    public List<PropertyDescription> buildByType(
        Class<?> type, Object object, PropertyDefinition parentData)
    {
        List<PropertyDescription> propertyDescriptions = new ArrayList<>();
        List<PropertyDefinition> properties = PropertyChecker.getPropertiesDataByType(type);
        String parentName = getParentName(parentData);
        for (PropertyDefinition propertyDefinition : properties)
        {
            var propertyWithValue = buildProperty(propertyDefinition, object, parentName, type);
            propertyDescriptions.add(propertyWithValue);
        }
        return propertyDescriptions;
    }

    // Uses reflection like method to get the value of a property in an object given its name.
    private PropertyDescription buildProperty(
        PropertyDefinition propertyDefinition, Object object, String parentName, Class<?> parentType)
    {
        String originalPropertyName = propertyDefinition.getName();
        PropertyDescription propertyDescription = new PropertyDescription();
        propertyDescription.setPropertyDefinition(propertyDefinition);
        String propertyName = buildPropertyName(propertyDefinition.getName(), parentName);
        propertyDescription.setName(propertyName);
        propertyDescription.setSimpleValue(PropertyChecker.isASimpleValue(propertyDefinition.getType()));
        Object value = PropertyChecker.getValue(originalPropertyName, object);
        propertyDescription.setValue(value);
        propertyDescription.setParentType(parentType);
        return propertyDescription;
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

    /**
     * Builds the correct name for a property. It is its current name if the property is not
     * a nested object. If it is a nested object (it has parent name), we just concatenate the
     * parent name to the name of the property.
     * @param property Name of the property.
     * @param parentName Name of the parent (if the property is a nested object).
     * @return A string with the new name of the property.
     */
    private String buildPropertyName(String property, String parentName)
    {
        String name = property;
        if (parentName != null)
        {
            name = parentName + NAME_SEPARATOR + name;
        }
        return name;
    }
}
