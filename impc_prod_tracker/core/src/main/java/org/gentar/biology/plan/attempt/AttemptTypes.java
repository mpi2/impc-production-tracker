package org.gentar.biology.plan.attempt;

public enum AttemptTypes {
    CRISPR("crispr"),
    HAPLOESSENTIAL_CRISPR("haplo-essential crispr"),
    BREEDING("breeding"),
    ADULT_PHENOTYPING("adult phenotyping"),
    HAPLOESSENTIAL_PHENOTYPING("haplo-essential phenotyping");

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
