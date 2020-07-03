package org.gentar.biology.sequence_location;

import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

@Component
public class SequenceLocationService
{
    private final SequenceLocationRepository sequenceLocationRepository;

    private static final String NOT_FOUND_ERROR = "Sequence location with id [%s] does not exist.";

    public SequenceLocationService(SequenceLocationRepository sequenceLocationRepository)
    {
        this.sequenceLocationRepository = sequenceLocationRepository;
    }

    public SequenceLocation getById(Long id)
    {
        return sequenceLocationRepository.findFirstById(id);
    }

    public SequenceLocation getByIdFailsIfNull(Long id)
    {
        SequenceLocation sequenceLocation = getById(id);
        if (sequenceLocation == null)
        {
            throw new UserOperationFailedException(String.format(NOT_FOUND_ERROR, id));
        }
        return sequenceLocation;
    }
}
