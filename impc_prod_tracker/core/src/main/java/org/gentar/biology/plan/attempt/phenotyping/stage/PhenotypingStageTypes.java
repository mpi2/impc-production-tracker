package org.gentar.biology.plan.attempt.phenotyping.stage;

public enum PhenotypingStageTypes {
    EARLY_ADULT("early adult"),
    LATE_ADULT("late adult"),
    HAPLOESSENTIAL("haplo-essential"),
    EMBRYO("embryo");

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
