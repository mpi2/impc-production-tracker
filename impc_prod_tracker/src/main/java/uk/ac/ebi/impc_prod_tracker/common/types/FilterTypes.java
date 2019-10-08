package uk.ac.ebi.impc_prod_tracker.common.types;

public enum FilterTypes
{
    TPN("tpn"),
    GENE("gene"),
    WORK_UNIT_NAME("workUnitName"),
    MARKER_SYMBOL("markerSymbol"),
    INTENTION("intention"),
    CONSORTIUM("consortium"),
    ASSIGNMENT_STATUS("assignmentStatus"),
    PRIVACY_NAME("privacyName");

    private String name;

    FilterTypes(String name)
    {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
