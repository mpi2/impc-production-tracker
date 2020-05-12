package org.gentar.biology.outcome;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.colony.engine.ColonyStateSetter;
import org.gentar.biology.outcome.type.OutcomeTypeName;
import org.gentar.biology.specimen.engine.SpecimenStateSetter;
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
    private ColonyStateSetter colonyStateSetter;
    private SpecimenStateSetter specimenStateSetter;

    OutcomeCreator(
        HistoryService<Outcome> historyService,
        ColonyStateSetter colonyStateSetter,
        SpecimenStateSetter specimenStateSetter)
    {
        this.historyService = historyService;
        this.colonyStateSetter = colonyStateSetter;
        this.specimenStateSetter = specimenStateSetter;
    }

    public Outcome create(Outcome outcome)
    {
        setInitialStatus(outcome);
        Outcome createdOutcome = save(outcome);
        registerCreationInHistory(createdOutcome);
        return createdOutcome;
    }

    private void setInitialStatus(Outcome outcome)
    {
        String outcomeTypeName =
            outcome.getOutcomeType() == null ? "" : outcome.getOutcomeType().getName();
        if (OutcomeTypeName.COLONY.getLabel().equals(outcomeTypeName))
        {
            colonyStateSetter.setInitialStatus(outcome.getColony());
        }
        else if (OutcomeTypeName.SPECIMEN.getLabel().equals(outcomeTypeName))
        {
            specimenStateSetter.setInitialStatus(outcome.getSpecimen());
        }
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
