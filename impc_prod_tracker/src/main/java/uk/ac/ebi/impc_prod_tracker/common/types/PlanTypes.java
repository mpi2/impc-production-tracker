package uk.ac.ebi.impc_prod_tracker.common.types;

public enum PlanTypes
{
    PRODUCTION("Production"),
    PHENOTYPING("Phenotyping");

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
