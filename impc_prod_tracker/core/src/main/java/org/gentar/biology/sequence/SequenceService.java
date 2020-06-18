package org.gentar.biology.sequence;

import org.gentar.biology.sequence.category.SequenceCategory;
import org.gentar.biology.sequence.category.SequenceCategoryName;
import org.springframework.stereotype.Component;

@Component
public class SequenceService
{
    private SequenceRepository sequenceRepository;

    public SequenceService(SequenceRepository sequenceRepository)
    {
        this.sequenceRepository = sequenceRepository;
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
        sequenceRepository.save(sequence);
    }
}
