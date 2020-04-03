package org.gentar.biology.plan.engine;

import org.gentar.biology.project.ProjectService;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.audit.history.HistoryService;
import org.gentar.statemachine.StateTransitionsManager;
import org.springframework.stereotype.Component;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanRepository;
import org.gentar.audit.history.History;

@Component
public class PlanUpdaterImpl implements PlanUpdater
{
    private HistoryService<Plan> historyService;
    private ContextAwarePolicyEnforcement policyEnforcement;
    private PlanRepository planRepository;
    private PlanValidator planValidator;
    private StateTransitionsManager stateTransitionManager;
    private ProjectService projectService;

    public PlanUpdaterImpl(
        HistoryService<Plan> historyService,
        ContextAwarePolicyEnforcement policyEnforcement,
        PlanRepository planRepository,
        PlanValidator planValidator,
        StateTransitionsManager stateTransitionManager,
        ProjectService projectService)
    {
        this.historyService = historyService;
        this.policyEnforcement = policyEnforcement;
        this.planRepository = planRepository;
        this.planValidator = planValidator;
        this.stateTransitionManager = stateTransitionManager;
        this.projectService = projectService;
    }

    @Override
    public History updatePlan(Plan originalPlan, Plan newPlan)
    {
        validatePermissionToUpdatePlan(newPlan);
        validateData(newPlan);
        changeStatusIfNeeded(newPlan);
        History history = detectTrackOfChanges(originalPlan, newPlan);
        saveChanges(newPlan);
        saveTrackOfChanges(history);
        projectService.checkForUpdates(newPlan.getProject());
        return history;
    }

    /**
     * Check if the current logged user has permission to update the plan p.
     * @param plan Plan being updated.
     */
    private void validatePermissionToUpdatePlan(Plan plan)
    {
        if (!policyEnforcement.hasPermission(plan, "UPDATE_PLAN"))
        {
            throw new UserOperationFailedException(
                "You don't have permission to edit this plan");
        }
    }

    /**
     * Check if the changes in the plan require a change on the status.
     * @param plan Plan being updated.
     */
    private void changeStatusIfNeeded(Plan plan)
    {
        if (plan.getEvent() != null)
        {
             stateTransitionManager.processEvent(plan);
        }
    }

    /**
     * Validates that the changes are valid.
     */
    private void validateData(Plan newPlan)
    {
        planValidator.validate(newPlan);
    }

    /**
     * Save the changes into the database for the specific plan.
     * @param plan Plan being updated.
     */
    private void saveChanges(Plan plan)
    {
       planRepository.save(plan);
    }

    /**
     * Detects the track of the changes between originalPlan and newPlan.
     * @param originalPlan The plan before the update.
     * @param newPlan The updated plan.
     */
    private History detectTrackOfChanges(Plan originalPlan, Plan newPlan)
    {
        return historyService.detectTrackOfChanges(originalPlan, newPlan, originalPlan.getId());
    }

    /**
     * Stores the track of the changes.
     * @param history
     */
    private void saveTrackOfChanges(History history)
    {
        historyService.saveTrackOfChanges(history);
    }
}
