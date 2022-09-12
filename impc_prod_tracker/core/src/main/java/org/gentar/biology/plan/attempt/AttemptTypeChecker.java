package org.gentar.biology.plan.attempt;

import org.gentar.biology.plan.Plan;

public class AttemptTypeChecker
{
    public static final String CRISPR_TYPE = "crispr";
    public static final String ES_CELL_TYPE = "es cell";
    public static final String ES_CELL_ALLELE_MODIFICATION_TYPE = "es cell allele modification";
    public static final String PHENOTYPING_TYPE = "adult and embryo phenotyping";
    public static final String NOT_DEFINED_TYPE = "Not defined";
    public static final String HAPLO_ESSENTIAL_CRISPR_TYPE = "haplo-essential crispr";

    public static String getAttemptTypeName(Plan plan)
    {
        return plan.getAttemptType() == null ? NOT_DEFINED_TYPE : plan.getAttemptType().getName();
    }
}
