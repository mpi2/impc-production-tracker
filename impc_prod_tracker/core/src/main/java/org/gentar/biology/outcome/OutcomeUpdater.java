package org.gentar.biology.outcome;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.colony.Colony;
import org.gentar.statemachine.StateTransitionsManager;
import org.springframework.stereotype.Component;

@Component
public class OutcomeUpdater
{
    private HistoryService<Outcome> historyService;
    private StateTransitionsManager stateTransitionsManager;
    private OutcomeRepository outcomeRepository;

    public OutcomeUpdater(
        HistoryService historyService,
        StateTransitionsManager stateTransitionsManager,
        OutcomeRepository outcomeRepository)
    {
        this.historyService = historyService;
        this.stateTransitionsManager = stateTransitionsManager;
        this.outcomeRepository = outcomeRepository;
    }

    History update(Outcome originalOutcome, Outcome newOutcome)
    {
        historyService.setEntityData(Outcome.class.getSimpleName(), originalOutcome.getId());
        validatePermission(newOutcome);
        validateData(newOutcome);
        changeStatusIfNeeded(newOutcome);
        History history = detectTrackOfChanges(originalOutcome, newOutcome);
        saveChanges(newOutcome);
        saveTrackOfChanges(history);
        return history;
    }

    private void validatePermission(Outcome newOutcome)
    {
        // Add validations if needed
    }

    private void validateData(Outcome newOutcome)
    {
        // Add validations if needed
    }

    private void changeStatusIfNeeded(Outcome outcome)
    {
        Colony colony = outcome.getColony();
        if (colony != null && colony.getEvent() != null)
        {
            colony = (Colony) stateTransitionsManager.processEvent(colony);
        }
    }

    private void saveChanges(Outcome outcome)
    {
        outcomeRepository.save(outcome);
    }

    private void saveTrackOfChanges(History history)
    {
        historyService.saveTrackOfChanges(history);
    }

    private History detectTrackOfChanges(Outcome originalOutcome, Outcome newOutcome)
    {
        History historyEntry = historyService.detectTrackOfChanges(originalOutcome, newOutcome);
        return historyEntry;
    }
}
