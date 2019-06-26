package uk.ac.ebi.impc_prod_tracker.service.plan.engine;

import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;

/**
 * Contains the rules to update a plan and trace the changes
 */
public interface PlanUpdater
{
    /**
     * Update an existing plan using the new information that has been assigned to it.
     * Check if the current logged user has permission to update the plan.
     * Validates that the new data is consistent with the type of plan and its status.
     * Save the changes in an audit table to keep trace of the changes.
     * @param originalPlan
     * @param newPlan
     */
    void updatePlan(Plan originalPlan, Plan newPlan);
}
