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
public class PhenotypingStageCreator
{
    @PersistenceContext
    private final EntityManager entityManager;
    private final HistoryService<PhenotypingStage> historyService;
    private final PhenotypingStageStateSetter phenotypingStageStateSetter;
    private final PhenotypingStageValidator phenotypingStageValidator;

    public PhenotypingStageCreator(
        EntityManager entityManager,
        HistoryService<PhenotypingStage> historyService,
        PhenotypingStageStateSetter phenotypingStageStateSetter,
        PhenotypingStageValidator phenotypingStageValidator)
    {
        this.entityManager = entityManager;
        this.historyService = historyService;
        this.phenotypingStageStateSetter = phenotypingStageStateSetter;
        this.phenotypingStageValidator = phenotypingStageValidator;
    }

    @Transactional
    public PhenotypingStage create(PhenotypingStage phenotypingStage)
    {
        validateData(phenotypingStage);
        setInitialStatus(phenotypingStage);
        PhenotypingStage createdPhenotypingStage = save(phenotypingStage);
        registerCreationInHistory(createdPhenotypingStage);
        return createdPhenotypingStage;
    }

    private void validateData(PhenotypingStage phenotypingStage)
    {
        phenotypingStageValidator.validate(phenotypingStage);
    }

    private void setInitialStatus(PhenotypingStage phenotypingStage)
    {
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
        phenotypingStage.setPsn(buildPsn(phenotypingStage.getId()));
        return phenotypingStage;
    }

    private String buildPsn(Long id)
    {
        String identifier = String.format("%0" + 12 + "d", id);
        identifier = "PSN:" + identifier;
        return identifier;
    }
}
