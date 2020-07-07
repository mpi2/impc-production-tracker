package org.gentar.biology.mutation;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.colony.Colony_;
import org.gentar.biology.location.Location_;
import org.gentar.biology.species.Species_;
import org.gentar.biology.status.Status_;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.gentar.security.permissions.PermissionService;
import org.springframework.stereotype.Component;

@Component
public class MutationUpdater
{
    private final MutationRepository mutationRepository;
    private final HistoryService<Mutation> historyService;
    private final ContextAwarePolicyEnforcement policyEnforcement;

    public MutationUpdater(
        MutationRepository mutationRepository,
        HistoryService<Mutation> historyService,
        ContextAwarePolicyEnforcement policyEnforcement)
    {
        this.mutationRepository = mutationRepository;
        this.historyService = historyService;
        this.policyEnforcement = policyEnforcement;
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
        if (!policyEnforcement.hasPermission(mutation, PermissionService.UPDATE_MUTATION))
        {
            throw new UserOperationFailedException("You don't have permission to edit this mutation");
        }
    }

    private void validateData(Mutation newMutation)
    {
        // Add validations if needed
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
