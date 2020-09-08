package org.gentar.biology.plan.attempt.phenotyping.stage.type;

import org.gentar.util.LabeledType;
import java.util.HashMap;
import java.util.Map;

public enum PhenotypingStageTypeName implements LabeledType
{
    EARLY_ADULT("early adult"),
    LATE_ADULT("late adult"),
    HAPLOESSENTIAL("haplo-essential"),
    EMBRYO("embryo");

    private static final Map<String, PhenotypingStageTypeName> BY_LABEL = new HashMap<>();
    static
    {
        for (PhenotypingStageTypeName e: values())
        {
            BY_LABEL.put(e.label, e);
        }
    }

    private final String label;

    PhenotypingStageTypeName(String label)
    {
        this.label = label;
    }

    public static PhenotypingStageTypeName valueOfLabel(String label)
    {
        return BY_LABEL.get(label);
    }

    @Override
    public String getLabel()
    {
        return label;
    }
}
