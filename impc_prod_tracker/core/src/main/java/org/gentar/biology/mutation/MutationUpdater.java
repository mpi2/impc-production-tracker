package org.gentar.biology.mutation;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.colony.Colony_;
import org.gentar.biology.location.Location_;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.species.Species_;
import org.gentar.biology.status.Status_;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.gentar.security.permissions.Actions;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class MutationUpdater
{
    private final MutationRepository mutationRepository;
    private final HistoryService<Mutation> historyService;
    private final ContextAwarePolicyEnforcement policyEnforcement;
    private final MutationValidator mutationValidator;

    public MutationUpdater(
            MutationRepository mutationRepository,
            HistoryService<Mutation> historyService,
            ContextAwarePolicyEnforcement policyEnforcement,
            MutationValidator mutationValidator)
    {
        this.mutationRepository = mutationRepository;
        this.historyService = historyService;
        this.policyEnforcement = policyEnforcement;
        this.mutationValidator = mutationValidator;
    }

    History update(Mutation originalMutation, Mutation newMutation)
    {
        validatePermission(originalMutation);
        validateData(newMutation);
        History history = detectTrackOfChanges(originalMutation, newMutation);
        saveChanges(newMutation);
        saveTrackOfChanges(history);
        return history;
    }

    private void validatePermission(Mutation mutation)
    {
        Set<Plan> associatedPlans = getAssociatedPlans(mutation);
        for (Plan plan : associatedPlans)
        {
            // If the user can update the plan then they can update the mutation.
            if (!policyEnforcement.hasPermission(plan, Actions.UPDATE_PLAN_ACTION))
            {
                throw new UserOperationFailedException(
                    "To update this mutation you need permission to update the plan "
                        + plan.getPin() + ".");
            }
        }
    }

    private Set<Plan> getAssociatedPlans(Mutation mutation)
    {
        Set<Plan> plans = new HashSet<>();
        Set<Outcome> outcomes = mutation.getOutcomes();
        if (outcomes != null)
        {
            outcomes.forEach(x -> plans.add(x.getPlan()));
        }
        return plans;
    }

    private void validateData(Mutation newMutation)
    {
        mutationValidator.validateData(newMutation);
    }


    private void saveChanges(Mutation mutation)
    {
        mutationRepository.save(mutation);
    }

    private void saveTrackOfChanges(History history)
    {
        historyService.saveTrackOfChanges(history);
    }

    private History detectTrackOfChanges(Mutation originalMutation, Mutation newMutation)
    {
        History history =
            historyService.detectTrackOfChanges(
                originalMutation, newMutation, originalMutation.getId());
        if (history != null)
        {
            history = historyService.filterDetailsInNestedEntity(history, Colony_.STATUS, Status_.NAME);
            history = historyService.filterDetailsInNestedEntity(history, Location_.SPECIES, Species_.NAME);
        }
        return history;
    }

}
