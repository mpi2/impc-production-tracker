package org.gentar.biology.plan.type;

public enum PlanTypes
{
    PRODUCTION("Production"),
    PHENOTYPING("Phenotyping"),
    BREEDING("Breeding");

    private String typeName;

    PlanTypes(String typeName)
    {
        this.typeName = typeName;
    }

    public String getTypeName()
    {
        return typeName;
    }
}
