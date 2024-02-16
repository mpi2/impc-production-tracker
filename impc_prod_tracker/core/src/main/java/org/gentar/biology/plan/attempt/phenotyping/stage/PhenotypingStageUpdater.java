package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.plan.engine.PlanUpdater;
import org.gentar.biology.status.Status_;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.gentar.statemachine.StateTransitionsManager;
import org.springframework.stereotype.Component;

@Component
public class PhenotypingStageUpdater
{
    private final HistoryService<PhenotypingStage> historyService;
    private final StateTransitionsManager stateTransitionsManager;
    private final PhenotypingStageRepository phenotypingStageRepository;
    private final PlanUpdater planUpdater;
    private final ContextAwarePolicyEnforcement policyEnforcement;
    private final PhenotypingStageValidator phenotypingStageValidator;

    public PhenotypingStageUpdater(
        HistoryService<PhenotypingStage> historyService,
        StateTransitionsManager stateTransitionsManager,
        PhenotypingStageRepository phenotypingStageRepository,
        PlanUpdater planUpdater,
        ContextAwarePolicyEnforcement policyEnforcement,
        PhenotypingStageValidator phenotypingStageValidator)
    {
        this.historyService = historyService;
        this.stateTransitionsManager = stateTransitionsManager;
        this.phenotypingStageRepository = phenotypingStageRepository;
        this.planUpdater = planUpdater;
        this.policyEnforcement = policyEnforcement;
        this.phenotypingStageValidator = phenotypingStageValidator;
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
        phenotypingStageValidator.validateUpdatePermission(phenotypingStage);
    }

    private void validateData(PhenotypingStage phenotypingStage)
    {
        phenotypingStageValidator.validateData(phenotypingStage);
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

    private void updatePlanDueToChangesInChild(PhenotypingStage phenotypingStage)
    {
        planUpdater.notifyChangeInChild(phenotypingStage.getPhenotypingAttempt().getPlan());
    }
}
