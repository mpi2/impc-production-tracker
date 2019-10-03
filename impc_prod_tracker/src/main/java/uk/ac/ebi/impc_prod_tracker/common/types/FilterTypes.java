package uk.ac.ebi.impc_prod_tracker.common.types;

public enum FilterTypes
{
    TPN("tpn"),
    WORK_UNIT_NAME("workUnitName");

    private String name;

    FilterTypes(String name)
    {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
