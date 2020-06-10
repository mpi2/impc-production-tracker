package org.gentar.biology.outcome;

import org.gentar.audit.history.History;
import org.gentar.biology.colony.Colony;
import org.gentar.biology.colony.engine.ColonyState;
import org.gentar.biology.mutation.Mutation;
import org.gentar.biology.mutation.MutationService;
import org.gentar.biology.outcome.type.OutcomeType;
import org.gentar.biology.outcome.type.OutcomeTypeRepository;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanService;
import org.gentar.biology.status.Status;
import org.gentar.biology.status.StatusService;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.security.abac.ResourceAccessChecker;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OutcomeServiceImpl implements OutcomeService
{
    private OutcomeRepository outcomeRepository;
    private OutcomeTypeRepository outcomeTypeRepository;
    private OutcomeCreator outcomeCreator;
    private StatusService statusService;
    private OutcomeUpdater outcomeUpdater;
    private PlanService planService;
    private MutationService mutationService;
    private ResourceAccessChecker<Outcome> resourceAccessChecker;

    public static final String READ_OUTCOME_ACTION = "READ_OUTCOME";

    public OutcomeServiceImpl(
        OutcomeRepository outcomeRepository,
        OutcomeTypeRepository outcomeTypeRepository,
        OutcomeCreator outcomeCreator,
        StatusService statusService,
        OutcomeUpdater outcomeUpdater,
        PlanService planService,
        MutationService mutationService,
        ResourceAccessChecker<Outcome> resourceAccessChecker)
    {
        this.outcomeRepository = outcomeRepository;
        this.outcomeTypeRepository = outcomeTypeRepository;
        this.outcomeCreator = outcomeCreator;
        this.statusService = statusService;
        this.outcomeUpdater = outcomeUpdater;
        this.planService = planService;
        this.mutationService = mutationService;
        this.resourceAccessChecker = resourceAccessChecker;
    }

    private List<Outcome> getCheckedCollection(Collection<Outcome> outcomes)
    {
        return outcomes.stream().map(this::getAccessChecked)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Outcome getAccessChecked(Outcome outcome)
    {
        return (Outcome) resourceAccessChecker.checkAccess(outcome, READ_OUTCOME_ACTION);
    }

    @Override
    public Outcome getOutcomeByPinAndTpo(String pin, String tpo)
    {
        Plan plan = planService.getNotNullPlanByPin(pin);
        Outcome outcome = getByTpoFailsIfNotFound(tpo);
        if (plan != outcome.getPlan())
        {
            throw new UserOperationFailedException(
                "Plan "+ pin + " is not related with outcome +" + tpo + ".");
        }
        return outcome;
    }

    public List<Outcome> getOutcomes()
    {
        return getCheckedCollection(outcomeRepository.findAll());
    }

    public Outcome create(Outcome outcome)
    {
        setInitialStatus(outcome);
        Outcome createdOutcome = outcomeCreator.create(outcome);
        return createdOutcome;
    }

    private void setInitialStatus(Outcome outcome)
    {
        Colony colony = outcome.getColony();
        if (colony != null)
        {
            Status colonyStatus =
                statusService.getStatusByName(ColonyState.GenotypeNotConfirmed.getInternalName());
            colony.setStatus(colonyStatus);
        }
    }

    @Transactional
    public History update(Outcome outcome)
    {
        Outcome originalOutcome = new Outcome(getByTpoFailsIfNotFound(outcome.getTpo()));
        return outcomeUpdater.update(originalOutcome, outcome);
    }

    @Override
    public OutcomeType getOutcomeTypeByName(String name)
    {
        return outcomeTypeRepository.findByNameIgnoreCase(name);
    }

    @Override
    public OutcomeType getOutcomeTypeByNameFailingWhenNull(String name)
    {
        OutcomeType outcomeType = getOutcomeTypeByName(name);
        if (outcomeType == null)
        {
            throw new UserOperationFailedException("Outcome type" + name + " does not exist.");
        }
        return outcomeType;
    }

    @Override
    public Outcome getByTpoFailsIfNotFound(String tpo)
    {
        Outcome outcome = outcomeRepository.findByTpo(tpo);
        if (outcome == null)
        {
            throw new UserOperationFailedException("Outcome " + tpo + " does not exist");
        }
        return outcome;
    }

    public Mutation getMutationByPinTpoAndMin(String pin, String tpo, String min)
    {
        Mutation mutation = null;
        Plan plan = planService.getNotNullPlanByPin(pin);
        Outcome outcome = getByTpoFailsIfNotFound(tpo);
        if (plan != outcome.getPlan())
        {
            throw new UserOperationFailedException(
                "Plan " + pin + " is not related with outcome "+ tpo + ".");
        }
        Set<Mutation> allMutations = outcome.getMutations();
        if (allMutations != null)
        {
            mutation = allMutations.stream()
                .filter(x -> x.getMin().equals(min))
                .findFirst().orElse(null);
        }
        if (mutation == null)
        {
            throw new UserOperationFailedException("Mutation " + min + " not found");
        }
        return mutation;
    }

    @Override
    public History createMutationsAssociations(String pin, String tpo, List<String> mins)
    {
        Outcome outcome = getOutcomeByPinAndTpo(pin, tpo);
        Outcome originalOutcome = new Outcome(outcome);
        if (mins != null)
        {
            for (String min : mins)
            {
                Mutation mutation = mutationService.getMutationByMinFailsIfNull(min);
                outcome.addMutation(mutation);
            }
        }
        return outcomeUpdater.update(originalOutcome, outcome);
    }

    @Override
    public History deleteMutationsAssociations(String pin, String tpo, List<String> mins)
    {
        Outcome outcome = getOutcomeByPinAndTpo(pin, tpo);
        Outcome originalOutcome = new Outcome(outcome);
        if (outcome.getMutations() != null && mins != null)
        {
            Set<Mutation> mutationsToDelete = outcome.getMutations().stream()
                .filter(x -> mins.contains(x.getMin()))
                .collect(Collectors.toSet());
            for (Mutation mutation : mutationsToDelete)
            {
                outcome.deleteMutation(mutation);
            }
        }
        return outcomeUpdater.update(originalOutcome, outcome);
    }
}
