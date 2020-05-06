package org.gentar.biology.sequence.category;

import org.gentar.util.LabeledType;
import java.util.HashMap;
import java.util.Map;

public enum SequenceCategoryName implements LabeledType
{
    INTENTION_TARGET_SEQUENCE("intention target sequence"),
    ORIGINAL_STARTING_SEQUENCE("original starting sequence"),
    OUTCOME_SEQUENCE("outcome sequence");

    private static final Map<String, SequenceCategoryName> BY_LABEL = new HashMap<>();
    static
    {
        for (SequenceCategoryName e: values())
        {
            BY_LABEL.put(e.label, e);
        }
    }

    private final String label;

    SequenceCategoryName(String label)
    {
        this.label = label;
    }

    public static SequenceCategoryName valueOfLabel(String label)
    {
        return BY_LABEL.get(label);
    }

    @Override
    public String getLabel()
    {
        return label;
    }
}
