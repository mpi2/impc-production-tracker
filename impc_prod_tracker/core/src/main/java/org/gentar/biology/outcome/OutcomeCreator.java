package org.gentar.biology.outcome;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.colony.engine.ColonyStateSetter;
import org.gentar.biology.mutation.Mutation;
import org.gentar.biology.mutation.MutationService;
import org.gentar.biology.outcome.type.OutcomeTypeName;
import org.gentar.biology.plan.PlanStatusManager;
import org.gentar.biology.specimen.engine.SpecimenStateSetter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Set;

/**
 * Logic to create an outcome in the system.
 */
@Component
class OutcomeCreator
{
    @PersistenceContext
    private EntityManager entityManager;
    private final HistoryService<Outcome> historyService;
    private final ColonyStateSetter colonyStateSetter;
    private final SpecimenStateSetter specimenStateSetter;
    private final MutationService mutationService;
    private final OutcomeValidator outcomeValidator;
    private final PlanStatusManager planStatusManager;

    OutcomeCreator(
        HistoryService<Outcome> historyService,
        ColonyStateSetter colonyStateSetter,
        SpecimenStateSetter specimenStateSetter,
        MutationService mutationService,
        OutcomeValidator outcomeValidator,
        PlanStatusManager planStatusManager)
    {
        this.historyService = historyService;
        this.colonyStateSetter = colonyStateSetter;
        this.specimenStateSetter = specimenStateSetter;
        this.mutationService = mutationService;
        this.outcomeValidator = outcomeValidator;
        this.planStatusManager = planStatusManager;
    }

    @Transactional
    public Outcome create(Outcome outcome)
    {
        validatePermission(outcome);
        setInitialStatus(outcome);
        Outcome createdOutcome = save(outcome);
        saveMutations(createdOutcome);
        registerCreationInHistory(createdOutcome);
        updatePlanDueToChangesInChild(createdOutcome);
        return createdOutcome;
    }

    private void updatePlanDueToChangesInChild(Outcome outcome)
    {
        planStatusManager.setSummaryStatus(outcome.getPlan());
    }

    private void validatePermission(Outcome outcome)
    {
        outcomeValidator.validateCreationPermission(outcome);
    }

    private void setInitialStatus(Outcome outcome)
    {
        String outcomeTypeName =
            outcome.getOutcomeType() == null ? "" : outcome.getOutcomeType().getName();
        if (OutcomeTypeName.COLONY.getLabel().equals(outcomeTypeName))
        {
            colonyStateSetter.setInitialStatus(outcome.getColony());
        }
        else if (OutcomeTypeName.SPECIMEN.getLabel().equals(outcomeTypeName))
        {
            specimenStateSetter.setInitialStatus(outcome.getSpecimen());
        }
    }

    private void registerCreationInHistory(Outcome outcome)
    {
        History history = historyService.buildCreationTrack(outcome, outcome.getId());
        historyService.saveTrackOfChanges(history);
    }

    private Outcome save(Outcome outcome)
    {
        entityManager.persist(outcome);
        outcome.setTpo(buildTpo(outcome.getId()));
        return outcome;
    }

    private void saveMutations(Outcome outcome)
    {
        Set<Mutation> mutations = outcome.getMutations();
        if (mutations != null)
        {
            mutations.forEach(mutationService::create);
        }
    }

    private String buildTpo(Long id)
    {
        String identifier = String.format("%0" + 12 + "d", id);
        identifier = "TPO:" + identifier;
        return identifier;
    }
}
