package org.gentar.biology.insertion_sequence;

import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

@Component
public class InsertionSequenceService
{
    private final InsertionSequenceRepository insertionSequenceRepository;

    private static final String NOT_FOUND_ERROR = "The sequence with if [%s] does not exist.";

    public InsertionSequenceService(InsertionSequenceRepository insertionSequenceRepository)
    {
        this.insertionSequenceRepository = insertionSequenceRepository;
    }

    public InsertionSequence getById(Long id)
    {
        return insertionSequenceRepository.findFirstById(id);
    }

    public InsertionSequence getByIdFailsIfNull(Long id)
    {
        InsertionSequence sequence = getById(id);
        if (sequence == null)
        {
            throw new UserOperationFailedException(String.format(NOT_FOUND_ERROR, id));
        }
        return sequence;
    }

}
