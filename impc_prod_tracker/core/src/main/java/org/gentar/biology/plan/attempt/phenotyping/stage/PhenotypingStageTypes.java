package org.gentar.biology.plan.attempt.phenotyping.stage;

public enum PhenotypingStageTypes {
    EARLY_ADULT("early adult and embryo"),
    LATE_ADULT("late adult"),
    HAPLOESSENTIAL("haplo-essential"),
    EMBRYO("embryo");

    private final String typeName;

    PhenotypingStageTypes(String typeName)
    {
        this.typeName = typeName;
    }

    public String getTypeName()
    {
        return typeName;
    }
}
