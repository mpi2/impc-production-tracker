package org.gentar.biology.outcome;

import org.springframework.stereotype.Component;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 * Logic to create an outcome in the system.
 */
@Component
@Transactional
class OutcomeCreator
{
    @PersistenceContext
    private EntityManager entityManager;

    public Outcome create(Outcome outcome)
    {
        Outcome createdOutcome = save(outcome);
        return createdOutcome;
    }

    private Outcome save(Outcome outcome)
    {
        entityManager.persist(outcome);
        outcome.setTpo(buildTpo(outcome.getId()));
        return outcome;
    }

    private String buildTpo(Long id)
    {
        String identifier = String.format("%0" + 9 + "d", id);
        identifier = "TPO:" + identifier;
        return identifier;
    }
}
