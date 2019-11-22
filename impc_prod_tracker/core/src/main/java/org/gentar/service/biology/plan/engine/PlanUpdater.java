package org.gentar.service.biology.plan.engine;


import org.gentar.biology.plan.Plan;
import org.gentar.history.History;

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
     * @return
     */
    History updatePlan(Plan originalPlan, Plan newPlan);
}
