package org.gentar.biology.plan.engine;

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

    public PlanCreator(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    /**
     * Create a project in the system and keep trace of the event in the history.
     * @param plan Object with the project information before it is saved in database.
     * @return Created project.
     */
    public Plan createPlan(Plan plan)
    {
        Plan createdPlan = savePlan(plan);
        registerCreationInHistory();
        return createdPlan;
    }

    private Plan savePlan(Plan plan)
    {
        entityManager.persist(plan);
        plan.setPin(buildPin(plan.getId()));
        return plan;
    }

    private void registerCreationInHistory()
    {
        // TODO history for Plan creation
    }

    private String buildPin(Long id)
    {
        String identifier = String.format("%0" + 10 + "d", id);
        identifier = "PIN:" + identifier;
        return identifier;
    }
}
