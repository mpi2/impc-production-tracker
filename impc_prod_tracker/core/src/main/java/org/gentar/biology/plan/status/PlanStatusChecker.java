package org.gentar.biology.plan.status;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.engine.state.BreedingPlanState;
import org.gentar.biology.plan.engine.state.LateAdultPhenotypePlanState;
import org.gentar.biology.plan.engine.state.PhenotypePlanState;
import org.gentar.biology.plan.engine.state.ProductionPlanState;

/**
 * Utility class to get information related with the status of a plan.
 */
public class PlanStatusChecker
{
    public static boolean planIsAborted(Plan plan)
    {
        String statusName = plan.getStatus().getName();
        return statusName.equals(ProductionPlanState.AttemptAborted.getInternalName()) ||
            statusName.equals(BreedingPlanState.BreedingAborted.getInternalName()) ||
            statusName.equals(PhenotypePlanState.PhenotypeProductionAborted.getInternalName()) ||
            statusName.equals(
                LateAdultPhenotypePlanState.LateAdultPhenotypeProductionAborted.getInternalName());
    }
}
