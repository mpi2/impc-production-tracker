package org.gentar.biology.outcome;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.colony.Colony;
import org.gentar.biology.colony.Colony_;
import org.gentar.biology.plan.PlanStatusManager;
import org.gentar.biology.status.Status_;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.gentar.security.permissions.PermissionService;
import org.gentar.statemachine.StateTransitionsManager;
import org.springframework.stereotype.Component;

@Component
public class OutcomeUpdater
{
    private HistoryService<Outcome> historyService;
    private StateTransitionsManager stateTransitionsManager;
    private OutcomeRepository outcomeRepository;
    private PlanStatusManager planStatusManager;
    private ContextAwarePolicyEnforcement policyEnforcement;

    public OutcomeUpdater(
        HistoryService historyService,
        StateTransitionsManager stateTransitionsManager,
        OutcomeRepository outcomeRepository,
        PlanStatusManager planStatusManager,
        ContextAwarePolicyEnforcement policyEnforcement)
    {
        this.historyService = historyService;
        this.stateTransitionsManager = stateTransitionsManager;
        this.outcomeRepository = outcomeRepository;
        this.planStatusManager = planStatusManager;
        this.policyEnforcement = policyEnforcement;
    }

    History update(Outcome originalOutcome, Outcome newOutcome)
    {
        validatePermission(newOutcome);
        validateData(newOutcome);
        changeStatusIfNeeded(newOutcome);
        History history = detectTrackOfChanges(originalOutcome, newOutcome);
        saveChanges(newOutcome);
        saveTrackOfChanges(history);
        planStatusManager.setSummaryStatus(originalOutcome.getPlan());
        return history;
    }

    private void validatePermission(Outcome outcome)
    {
        if (!policyEnforcement.hasPermission(outcome.getPlan(), PermissionService.UPDATE_PLAN_ACTION))
        {
            throw new UserOperationFailedException(
                "You don't have permission to edit this outcome");
        }
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
        return history;
    }
}
