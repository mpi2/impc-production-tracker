package uk.ac.ebi.impc_prod_tracker.common.diff;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class to keep information about a property in an object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class PropertyValueData
{
    private Object value;
    private Class<?> type;
    private String name;
    private boolean isSimpleValue;
}
