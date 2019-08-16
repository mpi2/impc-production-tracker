package uk.ac.ebi.impc_prod_tracker.common.history;

import lombok.Data;

/**
 * Class to describe a change in a property between two objects.
 * For properties which value depend on another entity, an id is kept for posteriors checks of the
 * values. This is the case where the property is a value inside a nested object. The values of the
 * original nested value can be changed and so we need a reference to the id.
 */
@Data

class ChangeDescription
{
    // Name of the property in the objects.
    private String property;

    // The old value of the property.
    private Object oldValue;

    // The new value of the property.
    private Object newValue;

    // Name of the class of the property. Needed if a posterior check of the values is needed.
    private String parentClass;

    // Id in the "parentClass" entity for the old value.
    private Object oldValueId;

    // Id in the "parentClass" entity for the new value.
    private Object newValueId;
}
