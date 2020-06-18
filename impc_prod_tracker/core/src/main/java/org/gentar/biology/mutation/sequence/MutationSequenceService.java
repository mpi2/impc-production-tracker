package org.gentar.biology.mutation.sequence;

import org.springframework.stereotype.Component;

@Component
public class MutationSequenceService
{
    private MutationSequenceRepository mutationSequenceRepository;

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
