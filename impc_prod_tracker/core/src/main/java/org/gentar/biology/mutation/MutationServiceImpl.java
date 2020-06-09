package org.gentar.biology.mutation;

import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.outcome.OutcomeService;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanService;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class MutationServiceImpl implements MutationService
{
    private MutationRepository mutationRepository;
    private PlanService planService;
    private OutcomeService outcomeService;

    public MutationServiceImpl(
        MutationRepository mutationRepository, PlanService planService, OutcomeService outcomeService)
    {
        this.mutationRepository = mutationRepository;
        this.planService = planService;
        this.outcomeService = outcomeService;
    }

    public Mutation getMutationByPinTpoAndId(String pin, String tpo, String min)
    {
        Mutation mutation = null;
        Plan plan = planService.getNotNullPlanByPin(pin);
        Outcome outcome = outcomeService.getByTpoFailsIfNotFound(tpo);
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
            throw new UserOperationFailedException("Mutation with id " + min + " does not exist.");
        }
        return mutation;
    }
}
