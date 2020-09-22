package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.plan.PlanStatusManager;
import org.gentar.biology.status.Status_;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.gentar.security.permissions.PermissionService;
import org.gentar.statemachine.StateTransitionsManager;
import org.springframework.stereotype.Component;

@Component
public class PhenotypingStageUpdater
{
    private final HistoryService<PhenotypingStage> historyService;
    private final StateTransitionsManager stateTransitionsManager;
    private final PhenotypingStageRepository phenotypingStageRepository;
    private final PlanStatusManager planStatusManager;
    private final ContextAwarePolicyEnforcement policyEnforcement;

    public PhenotypingStageUpdater(
        HistoryService<PhenotypingStage> historyService,
        StateTransitionsManager stateTransitionsManager,
        PhenotypingStageRepository phenotypingStageRepository,
        PlanStatusManager planStatusManager,
        ContextAwarePolicyEnforcement policyEnforcement)
    {
        this.historyService = historyService;
        this.stateTransitionsManager = stateTransitionsManager;
        this.phenotypingStageRepository = phenotypingStageRepository;
        this.planStatusManager = planStatusManager;
        this.policyEnforcement = policyEnforcement;
    }

    History update(PhenotypingStage originalPhenotypingStage, PhenotypingStage newPhenotypingStage)
    {
        validatePermission(newPhenotypingStage);
        validateData(newPhenotypingStage);
        changeStatusIfNeeded(newPhenotypingStage);
        History history = detectTrackOfChanges(originalPhenotypingStage, newPhenotypingStage);
        saveChanges(newPhenotypingStage);
        saveTrackOfChanges(history);
        updatePlanDueToChangesInChild(newPhenotypingStage);
        return history;
    }

    private void validatePermission(PhenotypingStage phenotypingStage)
    {
        if (!policyEnforcement.hasPermission(
            phenotypingStage.getPhenotypingAttempt().getPlan(), PermissionService.UPDATE_PLAN_ACTION))
        {
            throw new UserOperationFailedException(
                "You don't have permission to edit this phenotyping stage.");
        }
    }

    private void validateData(PhenotypingStage phenotypingStage)
    {
        // Add validations if needed
    }

    private void changeStatusIfNeeded(PhenotypingStage phenotypingStage)
    {
        stateTransitionsManager.processEvent(phenotypingStage);
    }

    private void saveChanges(PhenotypingStage phenotypingStage)
    {
        phenotypingStageRepository.save(phenotypingStage);
    }

    private void saveTrackOfChanges(History history)
    {
        historyService.saveTrackOfChanges(history);
    }

    private History detectTrackOfChanges(
        PhenotypingStage originalPhenotypingStage, PhenotypingStage newPhenotypingStage)
    {
        History history =
            historyService.detectTrackOfChanges(
                originalPhenotypingStage, newPhenotypingStage, originalPhenotypingStage.getId());
        history = historyService.filterDetailsInNestedEntity(history, PhenotypingStage_.STATUS, Status_.NAME);
        return history;
    }

    private void updatePlanDueToChangesInChild(PhenotypingStage createdPhenotypingStage)
    {
        planStatusManager.setSummaryStatus(createdPhenotypingStage.getPhenotypingAttempt().getPlan());
    }
}
