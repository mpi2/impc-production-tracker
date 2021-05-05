package org.gentar.biology.plan.attempt;

import org.gentar.util.LabeledType;
import java.util.HashMap;
import java.util.Map;

public enum AttemptTypesName implements LabeledType
{
    CRISPR("crispr"),
    ES_CELL("es cell"),
    HAPLOESSENTIAL_CRISPR("haplo-essential crispr"),
    BREEDING("breeding"),
    ADULT_PHENOTYPING("adult and embryo phenotyping"),
    HAPLOESSENTIAL_PHENOTYPING("haplo-essential phenotyping");

    private static final Map<String, AttemptTypesName> BY_LABEL = new HashMap<>();
    static
    {
        for (AttemptTypesName e: values())
        {
            BY_LABEL.put(e.label, e);
        }
    }

    private final String label;

    AttemptTypesName(String label)
    {
        this.label = label;
    }

    public static AttemptTypesName valueOfLabel(String label)
    {
        return BY_LABEL.get(label);
    }

    @Override
    public String getLabel()
    {
        return label;
    }
}
