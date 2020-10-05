package org.gentar.biology.species;

import org.gentar.util.LabeledType;
import java.util.HashMap;
import java.util.Map;

public enum SpeciesNames implements LabeledType
{
    MOUSE("Mus musculus"),
    HUMAN("Homo sapiens");

    private static final Map<String, SpeciesNames> BY_LABEL = new HashMap<>();
    static
    {
        for (SpeciesNames e: values())
        {
            BY_LABEL.put(e.label, e);
        }
    }

    private final String label;

    SpeciesNames(String label)
    {
        this.label = label;
    }

    public static SpeciesNames valueOfLabel(String label)
    {
        return BY_LABEL.get(label);
    }

    @Override
    public String getLabel()
    {
        return label;
    }
}
