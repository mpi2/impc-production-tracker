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
    private Object value;
    private Class<?> type;
    private String name;
    private boolean isSimpleValue;

    public String toString()
    {
        return "[name: " + name + ", type: " + type.getSimpleName()
            + ", value: " + value + ", isSimpleValue:" +isSimpleValue +"]";
    }
}
