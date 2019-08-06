package uk.ac.ebi.impc_prod_tracker.service.plan.engine;

import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.history.History;
import java.util.List;

/**
 * Class to manage the historic information of a plan.
 */
public interface HistoryService
{
    History detectTrackOfChanges(Plan originalPlan, Plan newPlan);
    void saveTrackOfChanges(History historyEntry);
    List<History> getHistoryByPlanId(Long planId);
}
