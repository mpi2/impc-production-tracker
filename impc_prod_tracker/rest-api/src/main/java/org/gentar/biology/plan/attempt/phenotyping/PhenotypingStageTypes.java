package org.gentar.biology.plan.attempt.phenotyping;

public enum PhenotypingStageTypes {
    EARLY_ADULT("early adult"),
    LATE_ADULT("late adult"),
    EARLY_HAPLOESSENTIAL("early haploessential"),
    LATE_HAPLOESSENTIAL("late haploessential");

    private String typeName;

    PhenotypingStageTypes(String typeName)
    {
        this.typeName = typeName;
    }

    public String getTypeName()
    {
        return typeName;
    }
}
