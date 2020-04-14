package org.gentar.biology.plan.attempt.phenotyping.stage;

public enum PhenotypingStageTypes {
    EARLY_ADULT("early adult"),
    LATE_ADULT("late adult"),
    EARLY_HAPLOESSENTIAL("early haplo-essential"),
    LATE_HAPLOESSENTIAL("late haplo-essential");

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
