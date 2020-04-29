package org.gentar.biology.outcome.type;

import org.gentar.util.LabeledType;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is a representation of the records in the table outcome_type.
 */
public enum OutcomeTypeName implements LabeledType
{
    SPECIMEN("Specimen"),
    COLONY("Colony");

    private static final Map<String, OutcomeTypeName> BY_LABEL = new HashMap<>();
    static
    {
        for (OutcomeTypeName e: values())
        {
            BY_LABEL.put(e.label, e);
        }
    }

    private final String label;

    OutcomeTypeName(String label)
    {
        this.label = label;
    }

    public static OutcomeTypeName valueOfLabel(String label)
    {
        return BY_LABEL.get(label);
    }

    @Override
    public String getLabel()
    {
        return label;
    }
}
