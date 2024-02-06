package org.gentar.biology.plan.engine;

import org.gentar.biology.plan.Plan;
import org.gentar.audit.history.History;

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
     */
    History updatePlan(Plan originalPlan, Plan newPlan);

    /**
     * Method to be called by children elements ina plan after they change their data and need
     * to notify the plan so its summary status is updated correctly.
     * @param plan Plan to be notified of the changes
     */
    void notifyChangeInChild(Plan plan);
}
