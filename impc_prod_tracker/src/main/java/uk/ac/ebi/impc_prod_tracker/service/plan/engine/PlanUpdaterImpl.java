package uk.ac.ebi.impc_prod_tracker.service.plan.engine;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.conf.security.abac.spring.ContextAwarePolicyEnforcement;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.PlanRepository;


@Component
public class PlanUpdaterImpl implements PlanUpdater
{
    private HistoryService historyService;
    private ContextAwarePolicyEnforcement policyEnforcement;
    private PlanRepository planRepository;
    private UpdatePlanValidator updatePlanValidator;

    public PlanUpdaterImpl(
        HistoryService historyService,
        ContextAwarePolicyEnforcement policyEnforcement,
        PlanRepository planRepository,
        UpdatePlanValidator updatePlanValidator)
    {
        this.historyService = historyService;
        this.policyEnforcement = policyEnforcement;
        this.planRepository = planRepository;
        this.updatePlanValidator = updatePlanValidator;
    }

    @Override
    public void updatePlan(Plan originalPlan, Plan newPlan)
    {
        validatePermissionToUpdatePlan(newPlan);
        validateData(newPlan);
        detectTrackOfChanges(originalPlan, newPlan);
        changeStatusIfNeeded(newPlan);
        saveChanges(newPlan);
        saveTrackOfChanges();
    }

    /**
     * Check if the current logged user has permission to update the plan p.
     * @param plan Plan being updated.
     */
    private void validatePermissionToUpdatePlan(Plan plan)
    {
        policyEnforcement.checkPermission(plan, "UPDATE_PLAN");
    }

    /**
     * Check if the changes in the plan require a change on the status.
     * @param plan Plan being updated.
     */
    private void changeStatusIfNeeded(Plan plan)
    {
        System.out.println("Changing status");
    }

    /**
     * Validates that the changes are valid.
     */
    private void validateData(Plan newPlan)
    {
        updatePlanValidator.validatePlan(newPlan);
    }

    /**
     * Save the changes into the database for the specific plan.
     * @param plan Plan being updated.
     */
    private void saveChanges(Plan plan)
    {
        System.out.println("Saving changes");
        planRepository.save(plan);
    }

    /**
     * Detects the track of the changes between originalPlan and newPlan.
     * @param originalPlan The plan before the update.
     * @param newPlan The updated plan.
     */
    private void detectTrackOfChanges(Plan originalPlan, Plan newPlan)
    {
        historyService.detectTrackOfChanges(originalPlan, newPlan);
    }

    /**
     * Stores the track of the changes.
     */
    private void saveTrackOfChanges()
    {
        historyService.saveTrackOfChanges();
    }
}
