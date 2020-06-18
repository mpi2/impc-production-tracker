package org.gentar.biology.mutation;

import org.gentar.Mapper;
import org.gentar.biology.mutation.sequence.MutationSequence;
import org.gentar.biology.sequence.SequenceMapper;
import org.springframework.stereotype.Component;

@Component
public class MutationSequenceMapper implements Mapper<MutationSequence, MutationSequenceDTO>
{
    private SequenceMapper sequenceMapper;

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
        mutationSequenceDTO.setSequenceDTO(sequenceMapper.toDto(mutationSequence.getSequence()));
        return mutationSequenceDTO;
    }

    @Override
    public MutationSequence toEntity(MutationSequenceDTO mutationSequenceDTO)
    {
        MutationSequence mutationSequence = new MutationSequence();
        mutationSequence.setId(mutationSequenceDTO.getId());
        mutationSequence.setIndex(mutationSequenceDTO.getIndex());
        if (mutationSequenceDTO.getSequenceDTO() != null)
        {
            mutationSequence.setSequence(
                sequenceMapper.toEntity(mutationSequenceDTO.getSequenceDTO()));
        }
        return mutationSequence;
    }
}
