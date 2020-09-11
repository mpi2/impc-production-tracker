package org.gentar.biology.plan.engine;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanStatusManager;
import org.gentar.biology.project.ProjectService;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.organization.work_unit.WorkUnit;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.gentar.security.permissions.PermissionService;
import org.springframework.stereotype.Component;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 * Class with the logic to save a plan in the system.
 */
@Component
@Transactional
public class PlanCreator
{
    @PersistenceContext
    private final EntityManager entityManager;
    private final HistoryService<Plan> historyService;
    private final PlanStatusManager planStatusManager;
    private final ProjectService projectService;
    private final ContextAwarePolicyEnforcement policyEnforcement;

    public PlanCreator(
        EntityManager entityManager,
        HistoryService<Plan> historyService,
        PlanStatusManager planStatusManager,
        ProjectService projectService,
        ContextAwarePolicyEnforcement policyEnforcement)
    {
        this.entityManager = entityManager;
        this.historyService = historyService;
        this.planStatusManager = planStatusManager;
        this.projectService = projectService;
        this.policyEnforcement = policyEnforcement;
    }

    /**
     * Create a project in the system and keep trace of the event in the history.
     *
     * @param plan Object with the project information before it is saved in database.
     * @return Created project.
     */
    @Transactional
    public Plan createPlan(Plan plan)
    {
        validatePermissionToCreatePlan(plan);
        setStatusAndSummaryStatus(plan);
        Plan createdPlan = savePlan(plan);
        registerCreationInHistory(createdPlan);
        updateProjectDueToChangesInChild(plan);
        return createdPlan;
    }

    /**
     * Check if the current logged user has permission to update the plan p.
     * @param plan Plan being updated.
     */
    private void validatePermissionToCreatePlan(Plan plan)
    {
        if (!policyEnforcement.hasPermission(plan, PermissionService.CREATE_PLAN_ACTION))
        {
            WorkUnit workUnit = plan.getWorkUnit();
            String workUnitName = workUnit == null ? "Not defined" : workUnit.getName();
            throw new UserOperationFailedException(
                "You don't have permission to create a plan in work unit "+ workUnitName);
        }
    }

    private void setStatusAndSummaryStatus(Plan plan)
    {
        // Sets Plan Created for Status
        planStatusManager.setInitialStatus(plan);
        // Sets the correct initial statuses for the nested objects in plan.
        planStatusManager.setChildrenInitialStatuses(plan);
        // If the data is adequate, move through the state machine (system triggered transitions)
        planStatusManager.updateStatusIfNeeded(plan);
    }

    private Plan savePlan(Plan plan)
    {
        entityManager.persist(plan);
        plan.setPin(buildPin(plan.getId()));
        if (plan.getPhenotypingAttempt() != null)
        {
            if (plan.getPhenotypingAttempt().getPhenotypingStages() != null)
            {
                plan.getPhenotypingAttempt().getPhenotypingStages().forEach(x -> x.setPsn(buildPsn(x.getId())));
            }
        }
        return plan;
    }

    private void registerCreationInHistory(Plan plan)
    {
        History history = historyService.buildCreationTrack(plan, plan.getId());
        historyService.saveTrackOfChanges(history);
    }

    private String buildPin(Long id)
    {
        String identifier = String.format("%0" + 10 + "d", id);
        identifier = "PIN:" + identifier;
        return identifier;
    }

    private String buildPsn(Long id)
    {
        String identifier = String.format("%0" + 12 + "d", id);
        identifier = "PSN:" + identifier;
        return identifier;
    }

    /**
     * The creation in the plan can lead to changes in the project (summary status or
     * assignment status, for instance). So we need to notify the project about this.
     *
     * @param plan Plan that was created.
     */
    private void updateProjectDueToChangesInChild(Plan plan)
    {
        projectService.checkForUpdates(plan.getProject());
    }
}
