package org.gentar.biology.plan.type;

import org.gentar.util.LabeledType;
import java.util.HashMap;
import java.util.Map;

/**
 * This enum should match the values in plan_type table.
 */
public enum PlanTypeName implements LabeledType
{
    PRODUCTION("production"),
    PHENOTYPING("phenotyping");

    private static final Map<String, PlanTypeName> BY_LABEL = new HashMap<>();
    static
    {
        for (PlanTypeName e: values())
        {
            BY_LABEL.put(e.label, e);
        }
    }

    private final String label;

    PlanTypeName(String label)
    {
        this.label = label;
    }

    public static PlanTypeName valueOfLabel(String label)
    {
        return BY_LABEL.get(label);
    }

    @Override
    public String getLabel()
    {
        return label;
    }
}
