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
    private HistoryService<PhenotypingStage> historyService;
    private StateTransitionsManager stateTransitionsManager;
    private PhenotypingStageRepository phenotypingStageRepository;
    private PlanStatusManager planStatusManager;
    private ContextAwarePolicyEnforcement policyEnforcement;

    public PhenotypingStageUpdater (
            HistoryService historyService,
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
        planStatusManager.setSummaryStatus(originalPhenotypingStage.getPhenotypingAttempt().getPlan());
        return history;
    }

    private void validatePermission(PhenotypingStage phenotypingStage)
    {
        if (!policyEnforcement.hasPermission(phenotypingStage.getPhenotypingAttempt().getPlan(), PermissionService.UPDATE_PLAN_ACTION))
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
        if (phenotypingStage.getPsn() == null)
        {
            buildPsn(phenotypingStage.getId());
        }
        phenotypingStageRepository.save(phenotypingStage);
    }

    private String buildPsn(Long id)
    {
        String identifier = String.format("%0" + 12 + "d", id);
        identifier = "PSN:" + identifier;
        return identifier;
    }

    private void saveTrackOfChanges(History history)
    {
        historyService.saveTrackOfChanges(history);
    }

    private History detectTrackOfChanges(PhenotypingStage originalPhenotypingStage, PhenotypingStage newPhenotypingStage)
    {
        History history =
                historyService.detectTrackOfChanges(
                        originalPhenotypingStage, newPhenotypingStage, originalPhenotypingStage.getId());
        history = historyService.filterDetailsInNestedEntity(history, PhenotypingStage_.STATUS, Status_.NAME);
        return history;
    }
}
