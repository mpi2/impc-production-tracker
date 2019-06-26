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

    public PlanUpdaterImpl(
        HistoryService historyService,
        ContextAwarePolicyEnforcement policyEnforcement,
        PlanRepository planRepository)
    {
        this.historyService = historyService;
        this.policyEnforcement = policyEnforcement;
        this.planRepository = planRepository;
    }

    @Override
    public void updatePlan(Plan originalPlan, Plan newPlan)
    {
        validatePermissionToUpdatePlan(newPlan);
        changeStatusIfNeeded(newPlan);
        saveChanges(newPlan);
        traceChanges(originalPlan, newPlan);
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
     * Save the changes into the database for the specific plan.
     * @param plan Plan being updated.
     */
    private void saveChanges(Plan plan)
    {
        System.out.println("Saving changes");
        planRepository.save(plan);
    }

    /**
     * Stores the changes in an audit table to keep track of the changes in the plan.
     * @param originalPlan
     * @param newPlan Plan being updated.
     */
    private void traceChanges(Plan originalPlan, Plan newPlan)
    {
        historyService.traceChanges(originalPlan, newPlan);
    }
}
