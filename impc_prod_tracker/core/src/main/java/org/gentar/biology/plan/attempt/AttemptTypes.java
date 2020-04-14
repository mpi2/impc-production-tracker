package org.gentar.biology.plan.attempt;

public enum AttemptTypes {
    CRISPR("crispr"),
    BREEDING("breeding"),
    EARLY_ADULT("early adult"),
    LATE_ADULT("late adult"),
    HAPLOESSENTIAL_EARLY("haplo-essential early"),
    HAPLOESSENTIAL_LATE("haplo-essential late");

    private String typeName;

    AttemptTypes(String typeName)
    {
        this.typeName = typeName;
    }

    public final String getTypeName()
    {
        return typeName;
    }

    public static AttemptTypes getAttemptTypeEnumByName(String name)
    {
        AttemptTypes[] attemptTypes = AttemptTypes.values();
        for (AttemptTypes attemptTypesValue : attemptTypes)
        {
            if (attemptTypesValue.getTypeName().equalsIgnoreCase(name))
                return attemptTypesValue;
        }
        return null;
    }
}
