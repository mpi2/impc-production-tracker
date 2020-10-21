package org.gentar.biology.mutation;

import org.gentar.Mapper;
import org.gentar.biology.mutation.sequence.MutationSequence;
import org.gentar.biology.sequence.SequenceDTO;
import org.gentar.biology.sequence.SequenceMapper;
import org.gentar.biology.sequence.category.SequenceCategoryName;
import org.springframework.stereotype.Component;

@Component
public class MutationSequenceMapper implements Mapper<MutationSequence, MutationSequenceDTO>
{
    private final SequenceMapper sequenceMapper;

    public MutationSequenceMapper(SequenceMapper sequenceMapper)
    {
        this.sequenceMapper = sequenceMapper;
    }

    @Override
    public MutationSequenceDTO toDto(MutationSequence mutationSequence)
    {
        MutationSequenceDTO mutationSequenceDTO = new MutationSequenceDTO();
        mutationSequenceDTO.setId(mutationSequence.getId());
        mutationSequenceDTO.setIndex(mutationSequence.getIndex());
        SequenceDTO sequenceDTO = sequenceMapper.toDto(mutationSequence.getSequence());
        // No locations allowed under mutation sequences
        if (sequenceDTO != null)
        {
            sequenceDTO.setSequenceLocationDTOS(null);
        }
        mutationSequenceDTO.setSequenceDTO(sequenceDTO);
        return mutationSequenceDTO;
    }

    @Override
    public MutationSequence toEntity(MutationSequenceDTO mutationSequenceDTO)
    {
        MutationSequence mutationSequence = new MutationSequence();
        if (mutationSequenceDTO != null)
        {
            mutationSequence.setId(mutationSequenceDTO.getId());
            mutationSequence.setIndex(mutationSequenceDTO.getIndex());
            if (mutationSequenceDTO.getSequenceDTO() != null)
            {
                SequenceDTO sequenceDTO = mutationSequenceDTO.getSequenceDTO();
                sequenceDTO.setSequenceCategoryName(SequenceCategoryName.OUTCOME_SEQUENCE.getLabel());

                mutationSequence.setSequence(
                    sequenceMapper.toEntity(sequenceDTO));
            }
        }

        return mutationSequence;
    }
}
