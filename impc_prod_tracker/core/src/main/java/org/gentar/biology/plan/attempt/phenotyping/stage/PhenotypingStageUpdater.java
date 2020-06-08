package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.plan.PlanStatusManager;
import org.gentar.biology.status.Status_;
import org.gentar.statemachine.StateTransitionsManager;
import org.springframework.stereotype.Component;

@Component
public class PhenotypingStageUpdater
{
    private HistoryService<PhenotypingStage> historyService;
    private StateTransitionsManager stateTransitionsManager;
    private PhenotypingStageRepository phenotypingStageRepository;
    private PlanStatusManager planStatusManager;

    public PhenotypingStageUpdater(HistoryService<PhenotypingStage> historyService,
                                   StateTransitionsManager stateTransitionsManager,
                                   PhenotypingStageRepository phenotypingStageRepository,
                                   PlanStatusManager planStatusManager) {
        this.historyService = historyService;
        this.stateTransitionsManager = stateTransitionsManager;
        this.phenotypingStageRepository = phenotypingStageRepository;
        this.planStatusManager = planStatusManager;
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

    /**
     * Check if the current logged user has permission to update the phenotyping stage.
     * @param newPhenotypingStage Phenotype stage being updated.
     */
    private void validatePermission(PhenotypingStage newPhenotypingStage)
    {
        // Add validations if needed
    }

    /**
     * Validates that the changes are valid.
     * @param newPhenotypingStage
     */
    private void validateData(PhenotypingStage newPhenotypingStage)
    {
        // Add validations if needed
    }

    /**
     * Check if the changes in the plan require a change on the status.
     * @param newPhenotypingStage Phenotyping stage being updated.
     */
    private void changeStatusIfNeeded(PhenotypingStage newPhenotypingStage)
    {
        if (newPhenotypingStage != null && newPhenotypingStage.getEvent() != null)
        {
            stateTransitionsManager.processEvent(newPhenotypingStage);
        }
    }

    /**
     * Detects the track of the changes between originalPhenotypingStage and newPhenotypingStage.
     * @param originalPhenotypingStage The phenotyping stage before the update.
     * @param newPhenotypingStage The updated phenotyping stage.
     */
    private History detectTrackOfChanges(PhenotypingStage originalPhenotypingStage, PhenotypingStage newPhenotypingStage)
    {
        History history = historyService.detectTrackOfChanges(
                        originalPhenotypingStage, newPhenotypingStage, originalPhenotypingStage.getId());
        history = historyService.filterDetailsInNestedEntity(history, PhenotypingStage_.STATUS, Status_.NAME);
        return history;
    }

    /**
     * Save the changes into the database for the specific phenotyping stage.
     * @param newPhenotypingStage Phenotyping stage being updated.
     */
    private void saveChanges(PhenotypingStage newPhenotypingStage)
    {
        phenotypingStageRepository.save(newPhenotypingStage);
    }

    /**
     * Stores the track of the changes.
     * @param history
     */
    private void saveTrackOfChanges(History history)
    {
        historyService.saveTrackOfChanges(history);
    }
}
