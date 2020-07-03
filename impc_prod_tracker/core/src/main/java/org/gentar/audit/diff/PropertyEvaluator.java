package org.gentar.audit.diff;

/**
 * Class that takes a {@link PropertyDescription} value and completes its information with the value
 * that the object has for the property and also additional information like the correct name of the
 * property (appending the parent name) and whether or not the property holds a simple value.
 */
class PropertyEvaluator
{
    private final Object object;
    private final String parentName;
    private final PropertyDescription data = new PropertyDescription();

    PropertyEvaluator(PropertyDefinition input, Object object, String parentName)
    {
        this.data.setName(input.getName());
        this.data.setType(input.getType());
        this.data.setPropertyDefinition(input);
        this.object = object;
        this.parentName = parentName;
    }

    /**
     *
     */
    void evaluate()
    {
        data.setValue(PropertyChecker.getValue(data.getName(), object));
        data.getPropertyDefinition().setName(buildPropertyName());
        data.setSimpleValue(PropertyChecker.isASimpleValue(data.getType()));
    }

    /**
     * Sets the data evaluating the value of the property in the object.
     */
    void buildByValue()
    {
        data.setValue(object);
        data.getPropertyDefinition().setName(buildPropertyName());
        data.setSimpleValue(PropertyChecker.isASimpleValue(data.getType()));
    }

    PropertyDescription getData()
    {
        return data;
    }

    private String buildPropertyName()
    {
        String name = data.getPropertyDefinition().getName();
        if (parentName != null)
        {
            name = parentName + "." + name;
        }
        return name;
    }
}
