package org.gentar.audit.diff;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class to keep information about a property in an object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class PropertyDescription
{
    private PropertyDefinition propertyDefinition = new PropertyDefinition();
    private Object value;
    private boolean isSimpleValue;

    public String toString()
    {
        String name = propertyDefinition == null ? null : propertyDefinition.getName();
        String typeSimpleName = null;
        String parentTypeSimpleName = null;

        if (propertyDefinition != null)
        {
            typeSimpleName = propertyDefinition.getType().getSimpleName();
            if (propertyDefinition.getParentType() != null)
            {
                parentTypeSimpleName = propertyDefinition.getParentType().getSimpleName();
            }
        }
        return "[name: " + name + ", type: " + typeSimpleName
            + ", value: " + value + ", isSimpleValue:" +isSimpleValue
            +", parentType: "+parentTypeSimpleName+"]";
    }

    public String getName()
    {
        return propertyDefinition.getName();
    }

    public void setName(String name)
    {
        propertyDefinition.setName(name);
    }

    public Class<?> getType()
    {
        return propertyDefinition.getType();
    }

    public void setType(Class<?> type)
    {
        propertyDefinition.setType(type);
    }

    public Class<?> getParentType()
    {
        return propertyDefinition.getParentType();
    }

    public void setParentType(Class<?> type)
    {
        propertyDefinition.setParentType(type);
    }
}
