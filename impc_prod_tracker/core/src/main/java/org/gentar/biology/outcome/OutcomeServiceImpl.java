package org.gentar.biology.outcome;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.mutation.Mutation;
import org.gentar.biology.mutation.MutationService;
import org.gentar.biology.outcome.type.OutcomeType;
import org.gentar.biology.outcome.type.OutcomeTypeRepository;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanService;
import org.gentar.exceptions.NotFoundException;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.security.abac.ResourceAccessChecker;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OutcomeServiceImpl implements OutcomeService
{
    private final OutcomeRepository outcomeRepository;
    private final OutcomeTypeRepository outcomeTypeRepository;
    private final OutcomeCreator outcomeCreator;
    private final OutcomeUpdater outcomeUpdater;
    private final PlanService planService;
    private final MutationService mutationService;
    private final ResourceAccessChecker<Outcome> resourceAccessChecker;
    private final HistoryService<Outcome> historyService;

    public static final String READ_OUTCOME_ACTION = "READ_OUTCOME";

    public OutcomeServiceImpl(
        OutcomeRepository outcomeRepository,
        OutcomeTypeRepository outcomeTypeRepository,
        OutcomeCreator outcomeCreator,
        OutcomeUpdater outcomeUpdater,
        PlanService planService,
        MutationService mutationService,
        ResourceAccessChecker<Outcome> resourceAccessChecker,
        HistoryService<Outcome> historyService)
    {
        this.outcomeRepository = outcomeRepository;
        this.outcomeTypeRepository = outcomeTypeRepository;
        this.outcomeCreator = outcomeCreator;
        this.outcomeUpdater = outcomeUpdater;
        this.planService = planService;
        this.mutationService = mutationService;
        this.resourceAccessChecker = resourceAccessChecker;
        this.historyService = historyService;
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
                "Plan " + pin + " is not related with outcome +" + tpo + ".");
        }
        return outcome;
    }

    @Override
    public List<Outcome> getOutcomes()
    {
        return getCheckedCollection(outcomeRepository.findAll());
    }

    @Override
    @Transactional
    public Outcome create(Outcome outcome)
    {
        return outcomeCreator.create(outcome);
    }

    @Override
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
            throw new NotFoundException("Outcome type" + name + " does not exist.");
        }
        return outcomeType;
    }

    @Override
    public Outcome getByTpoFailsIfNotFound(String tpo)
    {
        Outcome outcome = outcomeRepository.findByTpo(tpo);
        if (outcome == null)
        {
            throw new NotFoundException("Outcome " + tpo + " does not exist.");
        }
        return outcome;
    }

    @Override
    public Mutation getMutationByPinTpoAndMin(String pin, String tpo, String min)
    {
        Mutation mutation = null;
        Plan plan = planService.getNotNullPlanByPin(pin);
        Outcome outcome = getByTpoFailsIfNotFound(tpo);
        if (plan != outcome.getPlan())
        {
            throw new UserOperationFailedException(
                "Plan " + pin + " is not related with outcome " + tpo + ".");
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
            throw new NotFoundException("Mutation " + min + " does not exist.");
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

    @Override
    public List<History> getOutcomeHistory(Outcome outcome)
    {
        List<History> outcomeHistory =
            historyService.getHistoryByEntityNameAndEntityId("Outcome", outcome.getId());
        Set<Mutation> mutations = outcome.getMutations();
        if (mutations != null)
        {
            mutations.forEach(x -> outcomeHistory.addAll(mutationService.getHistory(x)));
        }
        outcomeHistory.sort(Comparator.comparing(History::getDate));
        return outcomeHistory;
    }

    @Override
    public void associateOutcomeToPlan(Outcome outcome, String pin)
    {
        Plan plan = planService.getNotNullPlanByPin(pin);
        if (plan.getOutcomes() == null)
        {
            plan.setOutcomes(new HashSet<>());
        }
        plan.getOutcomes().add(outcome);
        outcome.setPlan(plan);
    }
}
