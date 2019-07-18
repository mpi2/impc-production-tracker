package uk.ac.ebi.impc_prod_tracker.common.diff;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class to keep information about the definition of a property: Its name and its type.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyDefinition
{
    private String name;
    private Class<?> type;
}
