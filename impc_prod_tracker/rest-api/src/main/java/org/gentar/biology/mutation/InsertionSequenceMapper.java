package org.gentar.biology.mutation;

import org.gentar.Mapper;
import org.gentar.biology.insertion_sequence.InsertionSequence;
import org.gentar.biology.sequence.SequenceDTO;
import org.springframework.stereotype.Component;

@Component
public class InsertionSequenceMapper implements Mapper<InsertionSequence, InsertionSequenceDTO> {

    @Override
    public InsertionSequenceDTO toDto(InsertionSequence insertionSequence) {
        InsertionSequenceDTO insertionSequenceDTO = new InsertionSequenceDTO();
        insertionSequenceDTO.setId(insertionSequence.getId());
        insertionSequenceDTO.setSequence(insertionSequence.getSequence());
        insertionSequenceDTO.setLocation(insertionSequence.getLocation());
        return insertionSequenceDTO;
    }

    @Override
    public InsertionSequence toEntity(InsertionSequenceDTO insertionSequenceDTO) {
        InsertionSequence insertionSequence = new InsertionSequence();
        if (insertionSequenceDTO != null) {
            insertionSequence.setId(insertionSequenceDTO.getId());
            insertionSequence.setSequence(insertionSequenceDTO.getSequence());
            insertionSequence.setLocation(insertionSequenceDTO.getLocation());
        }

        return insertionSequence;
    }
}
