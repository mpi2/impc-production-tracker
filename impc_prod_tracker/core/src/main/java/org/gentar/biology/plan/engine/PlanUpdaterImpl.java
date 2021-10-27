package org.gentar.biology.plan.engine;

import org.gentar.audit.history.detail.HistoryDetail;
import org.gentar.biology.plan.PlanStatusManager;
import org.gentar.biology.plan.Plan_;
import org.gentar.biology.project.ProjectService;
import org.gentar.biology.status.Status_;
import org.gentar.audit.history.HistoryService;
import org.springframework.stereotype.Component;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanRepository;
import org.gentar.audit.history.History;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PlanUpdaterImpl implements PlanUpdater
{
    private final HistoryService<Plan> historyService;
    private final PlanRepository planRepository;
    private final PlanValidator planValidator;
    private final ProjectService projectService;
    private final PlanStatusManager planStatusManager;

    public PlanUpdaterImpl(
        HistoryService<Plan> historyService,
        PlanRepository planRepository,
        PlanValidator planValidator,
        ProjectService projectService,
        PlanStatusManager planStatusManager)
    {
        this.historyService = historyService;
        this.planRepository = planRepository;
        this.planValidator = planValidator;
        this.projectService = projectService;
        this.planStatusManager = planStatusManager;
    }

    @Override
    @Transactional
    public History updatePlan(Plan originalPlan, Plan newPlan)
    {
        validatePermissionToUpdatePlan(newPlan);
        History history = new History();
        if (newPlan.getEvent() != null && newPlan.getEvent().getName().equals("abandonWhenCreated")) {
            Plan newOriginalPlan = new Plan(originalPlan);
            newOriginalPlan.setEvent(newPlan.getEvent());
            changeStatusIfNeeded(newOriginalPlan);
            history = detectTrackOfChanges(originalPlan, newOriginalPlan);
            saveChanges(newOriginalPlan);
            saveTrackOfChanges(history);
            updateProjectDueToChangesInChild(newOriginalPlan);
        } else {
            validateUpdateData(originalPlan, newPlan);
            changeStatusIfNeeded(newPlan);
            history = detectTrackOfChanges(originalPlan, newPlan);
            saveChanges(newPlan);
            saveTrackOfChanges(history);
            updateProjectDueToChangesInChild(newPlan);
        }
        return history;
    }

    @Override
    public void notifyChangeInChild(Plan plan)
    {
        Plan planAfterUpdate = new Plan(plan);
        planStatusManager.setSummaryStatus(planAfterUpdate);
        updatePlan(plan, planAfterUpdate);
    }

    private void validatePermissionToUpdatePlan(Plan newPlan)
    {
        planValidator.validatePermissionToUpdatePlan(newPlan);
    }

    /**
     * Check if the changes in the plan require a change on the status.
     * @param plan Plan being updated.
     */
    private void changeStatusIfNeeded(Plan plan)
    {
        planStatusManager.updateStatusIfNeeded(plan);
    }

    /**
     * Validates that the changes are valid.
     */
    private void validateUpdateData(Plan originalPlan, Plan newPlan)
    {
        planValidator.validate(newPlan);

        planValidator.validateUpdate(originalPlan, newPlan);
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
        History history =
            historyService.detectTrackOfChanges(originalPlan, newPlan, originalPlan.getId());
        history = historyService.filterDetailsInNestedEntity(history, Plan_.STATUS, Status_.NAME);
        history = historyService.filterDetailsInNestedEntity(history, Plan_.SUMMARY_STATUS, Status_.NAME);
        return history;
    }

    /**
     * Stores the track of the changes.
     * @param history
     */
    private void saveTrackOfChanges(History history)
    {
        historyService.saveTrackOfChanges(history);
    }

    /**
     * The modification in the plan can lead to changes in the project (summary status or
     * assignment status, for instance). So we need to notify the project about this.
     * @param plan Plan that was updated.
     */
    private void updateProjectDueToChangesInChild(Plan plan)
    {
        projectService.checkForUpdates(plan.getProject());
    }
}
