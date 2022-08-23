package org.gentar.biology.outcome;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.colony.Colony;
import org.gentar.biology.colony.Colony_;
import org.gentar.biology.colony.distribution.DistributionProduct_;
import org.gentar.biology.plan.engine.PlanUpdater;
import org.gentar.biology.status.Status_;
import org.gentar.organization.work_unit.WorkUnit_;
import org.gentar.statemachine.StateTransitionsManager;
import org.springframework.stereotype.Component;

@Component
public class OutcomeUpdater
{
    private final HistoryService<Outcome> historyService;
    private final StateTransitionsManager stateTransitionsManager;
    private final OutcomeRepository outcomeRepository;
    private final PlanUpdater planUpdater;
    private final OutcomeValidator outcomeValidator;

    public OutcomeUpdater(
        HistoryService<Outcome> historyService,
        StateTransitionsManager stateTransitionsManager,
        OutcomeRepository outcomeRepository,
        PlanUpdater planUpdater, OutcomeValidator outcomeValidator)
    {
        this.historyService = historyService;
        this.stateTransitionsManager = stateTransitionsManager;
        this.outcomeRepository = outcomeRepository;
        this.planUpdater = planUpdater;
        this.outcomeValidator = outcomeValidator;
    }

    History update(Outcome originalOutcome, Outcome newOutcome)
    {
        validatePermission(newOutcome);
        validateUpdateData(originalOutcome, newOutcome);
        changeStatusIfNeeded(newOutcome);
        History history = detectTrackOfChanges(originalOutcome, newOutcome);
        saveChanges(newOutcome);
        saveTrackOfChanges(history);
        updatePlanDueToChangesInChild(newOutcome);
        return history;
    }

    private void updatePlanDueToChangesInChild(Outcome outcome)
    {
        planUpdater.notifyChangeInChild(outcome.getPlan());
    }

    private void validatePermission(Outcome newOutcome)
    {
        outcomeValidator.validateUpdatePermission(newOutcome);
    }

    private void validateUpdateData(Outcome originalOutcome, Outcome newOutcome)
    {
        outcomeValidator.validateUpdateData(originalOutcome, newOutcome);
    }

    private void changeStatusIfNeeded(Outcome outcome)
    {
        Colony colony = outcome.getColony();
        if (colony != null && colony.getProcessDataEvent() != null)
        {
            stateTransitionsManager.processEvent(colony);
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

        History history =
            historyService.detectTrackOfChanges(
                originalOutcome, newOutcome, originalOutcome.getId());
        history = historyService.filterDetailsInNestedEntity(history, Colony_.STATUS, Status_.NAME);
        history =
            historyService.filterDetailsInNestedEntity(
                history, DistributionProduct_.DISTRIBUTION_CENTRE, WorkUnit_.NAME);
        return history;
    }
}
