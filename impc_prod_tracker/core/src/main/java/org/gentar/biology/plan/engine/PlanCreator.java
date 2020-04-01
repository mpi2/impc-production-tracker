package org.gentar.biology.plan.engine;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.assignment.AssignmentStatus;
import org.gentar.biology.status.Status;
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

    public PlanCreator(EntityManager entityManager,
                       HistoryService<Plan> historyService)
    {
        this.entityManager = entityManager;
        this.historyService = historyService;
    }

    /**
     * Create a project in the system and keep trace of the event in the history.
     * @param plan Object with the project information before it is saved in database.
     * @return Created project.
     */
    public Plan createPlan(Plan plan)
    {
        Plan createdPlan = savePlan(plan);
        registerCreationInHistory(createdPlan);
        return createdPlan;
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
}
