package org.gentar.biology.outcome.type;

import org.gentar.util.LabeledType;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is a representation of the records in the table outcome_type.
 */
public enum OutcomeTypes implements LabeledType
{
    SPECIMEN("Specimen"),
    COLONY("Colony");

    private static final Map<String, OutcomeTypes> BY_LABEL = new HashMap<>();
    static
    {
        for (OutcomeTypes e: values())
        {
            BY_LABEL.put(e.label, e);
        }
    }

    private final String label;

    OutcomeTypes(String label)
    {
        this.label = label;
    }

    public static OutcomeTypes valueOfLabel(String label)
    {
        return BY_LABEL.get(label);
    }

    @Override
    public String getLabel()
    {
        return label;
    }
}
