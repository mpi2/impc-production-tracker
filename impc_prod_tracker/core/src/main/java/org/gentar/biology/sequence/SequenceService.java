package org.gentar.biology.sequence;

import org.gentar.biology.sequence.category.SequenceCategory;
import org.gentar.biology.sequence.category.SequenceCategoryName;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

@Component
public class SequenceService
{
    private SequenceRepository sequenceRepository;

    private static final String NOT_FOUND_ERROR = "The sequence with if [%s] does not exist.";

    public SequenceService(SequenceRepository sequenceRepository)
    {
        this.sequenceRepository = sequenceRepository;
    }

    public Sequence getById(Long id)
    {
        return sequenceRepository.findFirstById(id);
    }

    public Sequence getByIdFailsIfNull(Long id)
    {
        Sequence sequence = getById(id);
        if (sequence == null)
        {
            throw new UserOperationFailedException(String.format(NOT_FOUND_ERROR, id));
        }
        return sequence;
    }

    /**
     * Checks if the sequence has a specific category.
     * @param sequence Sequence to evaluate.
     * @param sequenceCategoryName Name of the sequence category.
     * @return Whether or not the sequence has the sequence category.
     */
    public boolean sequenceHasCategory(Sequence sequence, SequenceCategoryName sequenceCategoryName)
    {
        SequenceCategory sequenceCategory = sequence == null ? null : sequence.getSequenceCategory();
        assert sequenceCategory != null;
        return sequenceCategory.getName().equals(sequenceCategoryName.getLabel());
    }

    /**
     * Save a sequence.
     * @param sequence Sequence information.
     */
    public void createSequence(Sequence sequence)
    {
        if (sequence != null)
        {
            sequenceRepository.save(sequence);
        }
    }
}
