package org.gentar.biology.mutation;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.location.Location;
import org.gentar.biology.mutation.sequence.MutationSequence;
import org.gentar.biology.sequence.Sequence;
import org.gentar.biology.sequence_location.SequenceLocation;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

@Component
public class MutationUpdater
{
    private MutationRepository mutationRepository;
    private HistoryService<Mutation> historyService;

    public MutationUpdater(MutationRepository mutationRepository, HistoryService<Mutation> historyService)
    {
        this.mutationRepository = mutationRepository;
        this.historyService = historyService;
    }

    History update(Mutation originalMutation, Mutation newMutation)
    {
        validatePermission(newMutation);
        validateData(newMutation);
        History history = detectTrackOfChanges(originalMutation, newMutation);
        saveChanges(newMutation);
        saveTrackOfChanges(history);
        return history;
    }

    private void validatePermission(Mutation mutation)
    {
        // TODO Validates the user belongs to the centre that created the mutation
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
        testChangeLoc(originalMutation, newMutation);
        History history =
            historyService.detectTrackOfChanges(
                originalMutation, newMutation, originalMutation.getId());
        //history = historyService.filterDetailsInNestedEntity(history, Colony_.STATUS, Status_.NAME);
        return history;
    }

    private void testChangeLoc(Mutation originalMutation, Mutation newMutation)
    {
        MutationSequence originalMutationSequence = null;
        MutationSequence newMutationSequence = null;
        Location originalLocation = null;
        Location newLocation = null;
        // Original
        for (MutationSequence mutationSequence : originalMutation.getMutationSequences())
        {
            originalMutationSequence = mutationSequence;
        }

        for (SequenceLocation sequenceLocation : originalMutationSequence.getSequence().getSequenceLocations())
        {
            Location location = sequenceLocation.getLocation();
            originalLocation = location;
        }
        // New
        for (MutationSequence mutationSequence : newMutation.getMutationSequences())
        {
            newMutationSequence = mutationSequence;
        }

        for (SequenceLocation sequenceLocation : newMutationSequence.getSequence().getSequenceLocations())
        {
            Location location = sequenceLocation.getLocation();
            newLocation = location;
        }
        //boolean test = originalLocation.equals(newLocation);
    }

    private Location getLocationById(Collection<SequenceLocation> sequenceLocations, Long id)
    {
        for (SequenceLocation sequenceLocation: sequenceLocations)
        {
            Location location = sequenceLocation.getLocation();
            if (location.getId().equals(id))
            {
                return location;
            }
        }
        return null;
    }
}
