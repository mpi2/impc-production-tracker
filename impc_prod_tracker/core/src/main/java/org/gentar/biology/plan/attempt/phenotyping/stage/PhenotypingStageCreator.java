package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 * Logic to create a phenotyping stage in the system.
 */
@Component
@Transactional
class PhenotypingStageCreator {
    @PersistenceContext
    private EntityManager entityManager;
    private HistoryService<PhenotypingStage> historyService;
    private PhenotypingStageStateSetter phenotypingStageStateSetter;

    PhenotypingStageCreator(EntityManager entityManager, HistoryService<PhenotypingStage> historyService, PhenotypingStageStateSetter phenotypingStageStateSetter) {
        this.entityManager = entityManager;
        this.historyService = historyService;
        this.phenotypingStageStateSetter = phenotypingStageStateSetter;
    }

    public PhenotypingStage create(PhenotypingStage phenotypingStage)
    {
        setInitialStatus(phenotypingStage);
        PhenotypingStage createdPhenotypingStage = save(phenotypingStage);
        registerCreationInHistory(createdPhenotypingStage);
        return createdPhenotypingStage;
    }

    private void setInitialStatus(PhenotypingStage phenotypingStage)
    {
        // Sets Plan Created for Status
        phenotypingStageStateSetter.setInitialStatus(phenotypingStage);
    }

    private void registerCreationInHistory(PhenotypingStage phenotypingStage)
    {
        History history = historyService.buildCreationTrack(phenotypingStage, phenotypingStage.getId());
        historyService.saveTrackOfChanges(history);
    }

    private PhenotypingStage save(PhenotypingStage phenotypingStage)
    {
        entityManager.persist(phenotypingStage);
        return phenotypingStage;
    }
}
