package org.gentar.service.biology.plan.engine;

import org.gentar.biology.plan.Plan;

public class AttemptTypeChecker
{
    public static final String CRISPR_TYPE = "Crispr";
    public static final String NOT_DEFINED_TYPE = "Not defined";

    public static String getAttemptTypeName(Plan plan)
    {
        return plan.getAttemptType() == null ? NOT_DEFINED_TYPE : plan.getAttemptType().getName();
    }
}
