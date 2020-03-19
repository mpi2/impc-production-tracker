package org.gentar.biology.project;

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

    public String getTypeName()
    {
        return typeName;
    }
}
