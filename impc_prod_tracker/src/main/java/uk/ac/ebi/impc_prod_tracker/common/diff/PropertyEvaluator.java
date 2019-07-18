package uk.ac.ebi.impc_prod_tracker.common.diff;

/**
 * Class that takes a {@lonk PropertyValueData} value and completes its information with the value
 * that the object has for the property and also additional information like the correct name of the
 * property (appending the parent name) and whether or not the property holds a simple value.
 */
class PropertyEvaluator
{
    private Object object;
    private String parentName;
    private PropertyValueData data = new PropertyValueData();

    PropertyEvaluator(PropertyDefinition input, Object object, String parentName)
    {
        this.data.setName(input.getName());
        this.data.setType(input.getType());
        this.object = object;
        this.parentName = parentName;
    }

    void evaluate()
    {
        if (mustSkip())
        {
            data = null;
        }
        else
        {
            buildData();
        }
    }

    PropertyValueData getData()
    {
        return data;
    }

    private void buildData()
    {
        data.setValue(PropertyChecker.getValue(data.getName(), object));
        data.setName(buildPropertyName());
        data.setSimpleValue(PropertyChecker.isASimpleValue(data.getType()));
    }

    private String buildPropertyName()
    {
        String name = data.getName();
        if (parentName != null)
        {
            name = parentName + "." + name;
        }
        return name;
    }

    private boolean mustSkip()
    {
        return PropertyChecker.isCollection(data.getType());
    }
}
