package org.gentar.biology.plan.attempt.phenotyping.stage.material_deposited_type;

import org.gentar.util.LabeledType;
import java.util.HashMap;
import java.util.Map;

public enum MaterialDepositedTypeNames implements LabeledType
{
    PARAFFIN_EMBEDDED_SECTIONS("Paraffin-embedded Sections"),
    FIXED_TISSUE("Fixed Tissue");

    private static final Map<String, MaterialDepositedTypeNames> BY_LABEL = new HashMap<>();
    static
    {
        for (MaterialDepositedTypeNames e: values())
        {
            BY_LABEL.put(e.label, e);
        }
    }

    private final String label;

    MaterialDepositedTypeNames(String label)
    {
        this.label = label;
    }

    public static MaterialDepositedTypeNames valueOfLabel(String label)
    {
        return BY_LABEL.get(label);
    }

    @Override
    public String getLabel()
    {
        return label;
    }
}
