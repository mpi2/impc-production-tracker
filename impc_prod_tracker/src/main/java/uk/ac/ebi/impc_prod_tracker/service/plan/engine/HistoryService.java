package uk.ac.ebi.impc_prod_tracker.service.plan.engine;

import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.history.History;

import java.util.List;

/**
 * Class to manage the historic information of a plan.
 */
public interface HistoryService
{
    void traceChanges(Plan originalPlan, Plan newPlan);

    List<History> getHistoryByPlanId(Long planId);
}
