package org.gentar.biology.plan.engine;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanStatusManager;
import org.gentar.biology.project.ProjectService;
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
    private EntityManager entityManager;
    private HistoryService<Plan> historyService;
    private PlanStatusManager planStatusManager;
    private ProjectService projectService;

    public PlanCreator(
        EntityManager entityManager,
        HistoryService<Plan> historyService,
        PlanStatusManager planStatusManager,
        ProjectService projectService)
    {
        this.entityManager = entityManager;
        this.historyService = historyService;
        this.planStatusManager = planStatusManager;
        this.projectService = projectService;
    }

    /**
     * Create a project in the system and keep trace of the event in the history.
     * @param plan Object with the project information before it is saved in database.
     * @return Created project.
     */
    @Transactional
    public Plan createPlan(Plan plan)
    {
        setStatusAndSummaryStatus(plan);
        Plan createdPlan = savePlan(plan);
        registerCreationInHistory(createdPlan);
        updateProjectDueToChangesInChild(plan);
        return createdPlan;
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

    /**
     * The creation in the plan can lead to changes in the project (summary status or
     * assignment status, for instance). So we need to notify the project about this.
     * @param plan Plan that was created.
     */
    private void updateProjectDueToChangesInChild(Plan plan)
    {
        projectService.checkForUpdates(plan.getProject());
    }
}
