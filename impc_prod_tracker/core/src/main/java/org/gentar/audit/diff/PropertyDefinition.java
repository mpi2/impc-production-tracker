package org.gentar.audit.diff;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class to keep information about the definition of a property: Its name, its type and the type
 * of its parent in case the property is a nested object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyDefinition
{
    private String name;
    private Class<?> type;
    private Class<?> parentType;
}
