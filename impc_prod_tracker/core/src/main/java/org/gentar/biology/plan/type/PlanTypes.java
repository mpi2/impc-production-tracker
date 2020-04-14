package org.gentar.biology.plan.type;

/**
 * This enum should match the values in plan_type table.
 */
public enum PlanTypes
{
    PRODUCTION("production"),
    PHENOTYPING("phenotyping");

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
