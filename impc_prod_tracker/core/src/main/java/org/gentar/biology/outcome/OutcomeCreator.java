package org.gentar.biology.outcome;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.plan.Plan;
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
    private HistoryService<Outcome> historyService;

    OutcomeCreator(HistoryService<Outcome> historyService)
    {
        this.historyService = historyService;
    }

    public Outcome create(Outcome outcome)
    {
        Outcome createdOutcome = save(outcome);
        registerCreationInHistory(createdOutcome);
        return createdOutcome;
    }

    private void registerCreationInHistory(Outcome outcome)
    {
        History history = historyService.buildCreationTrack(outcome, outcome.getId());
        historyService.saveTrackOfChanges(history);
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
