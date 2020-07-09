package org.gentar.audit.diff;

import org.gentar.util.LabeledType;
import java.util.HashMap;
import java.util.Map;

/**
 * Allows to specify the type of change in a property.
 */
public enum ChangeType implements LabeledType
{
    // The property was added to a collection.
    ADDED("Element added"),
    // The property was removed from a collection.
    REMOVED("Element removed"),
    // The property is an element in a collection and it was changed.
    CHANGED_ELEMENT("Element changed"),
    // The property changed its value.
    CHANGED_FIELD("Field changed"),
    // The property was not changed.
    UNTOUCHED("No changed");

    private static final Map<String, ChangeType> BY_LABEL = new HashMap<>();
    static
    {
        for (ChangeType e: values())
        {
            BY_LABEL.put(e.label, e);
        }
    }

    private final String label;

    ChangeType(String label)
    {
        this.label = label;
    }

    public static ChangeType valueOfLabel(String label)
    {
        return BY_LABEL.get(label);
    }

    @Override
    public String getLabel()
    {
        return label;
    }
}
