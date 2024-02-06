package org.gentar.biology.mutation.sequence;

import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

@Component
public class MutationSequenceService
{
    private final MutationSequenceRepository mutationSequenceRepository;

    private static final String NOT_FOUND_ERROR = "Mutation sequence with id [%s] does not exist.";

    public MutationSequence getById(Long id)
    {
        return mutationSequenceRepository.findFirstById(id);
    }

    public MutationSequence getByIdFailsIfNull(Long id)
    {
        MutationSequence mutationSequence = getById(id);
        if (mutationSequence == null)
        {
            throw new UserOperationFailedException(String.format(NOT_FOUND_ERROR, id));
        }
        return mutationSequence;
    }

    public MutationSequenceService(MutationSequenceRepository mutationSequenceRepository)
    {
        this.mutationSequenceRepository = mutationSequenceRepository;
    }

    /**
     * Save a Mutation Sequence.
     * @param mutationSequence Mutation Sequence information.
     */
    public void createSequence(MutationSequence mutationSequence)
    {
        mutationSequenceRepository.save(mutationSequence);
    }
}
