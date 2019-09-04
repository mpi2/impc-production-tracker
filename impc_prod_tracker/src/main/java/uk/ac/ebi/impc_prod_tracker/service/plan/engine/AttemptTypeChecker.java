package uk.ac.ebi.impc_prod_tracker.service.plan.engine;

import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;

public class AttemptTypeChecker
{
    public static final String CRISPR_TYPE = "Crispr";
    public static final String NOT_DEFINED_TYPE = "Not defined";

    public static String getAttemptTypeName(Plan plan)
    {
        return plan.getAttemptType() == null ? NOT_DEFINED_TYPE : plan.getAttemptType().getName();
    }
}
