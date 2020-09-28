package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttempt;
import org.gentar.biology.plan.engine.PlanUpdater;
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
    private final PlanUpdater planUpdater;

    public PhenotypingStageCreator(
        EntityManager entityManager,
        HistoryService<PhenotypingStage> historyService,
        PhenotypingStageStateSetter phenotypingStageStateSetter,
        PhenotypingStageValidator phenotypingStageValidator,
        PlanUpdater planUpdater)
    {
        this.entityManager = entityManager;
        this.historyService = historyService;
        this.phenotypingStageStateSetter = phenotypingStageStateSetter;
        this.phenotypingStageValidator = phenotypingStageValidator;
        this.planUpdater = planUpdater;
    }

    @Transactional
    public PhenotypingStage create(PhenotypingStage phenotypingStage)
    {
        validateData(phenotypingStage);
        validatePermissions(phenotypingStage);
        setInitialStatus(phenotypingStage);
        PhenotypingStage createdPhenotypingStage = save(phenotypingStage);
        registerCreationInHistory(createdPhenotypingStage);
        associateToPhenotypingAttempt(phenotypingStage);
        updatePlanDueToChangesInChild(createdPhenotypingStage);
        return createdPhenotypingStage;
    }

    private void validatePermissions(PhenotypingStage phenotypingStage)
    {
        phenotypingStageValidator.validateCreationPermission(phenotypingStage);
    }

    // Needed so when validating the pla, it already has access to this phenotyping stage
    private void associateToPhenotypingAttempt(PhenotypingStage phenotypingStage)
    {
        PhenotypingAttempt phenotypingAttempt = phenotypingStage.getPhenotypingAttempt();
        phenotypingAttempt.addPhenotypingStage(phenotypingStage);
    }

    private void updatePlanDueToChangesInChild(PhenotypingStage createdPhenotypingStage)
    {
        planUpdater.notifyChangeInChild(createdPhenotypingStage.getPhenotypingAttempt().getPlan());
    }

    private void validateData(PhenotypingStage phenotypingStage)
    {
        phenotypingStageValidator.validateData(phenotypingStage);
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
