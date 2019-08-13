package uk.ac.ebi.impc_prod_tracker.service.plan.engine;

import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.history.History;
import java.util.List;

/**
 * Class to manage the historic information of a plan.
 */
public interface HistoryService
{
    void detectTrackOfChanges(Plan originalPlan, Plan newPlan);
    void saveTrackOfChanges();
    List<History> getHistoryByPlanId(Long planId);
}
