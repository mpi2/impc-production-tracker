package org.gentar.biology.mutation;

import org.gentar.Mapper;
import org.gentar.biology.insertion_sequence.InsertionSequence;
import org.springframework.stereotype.Component;

@Component
public class InsertionSequenceMapper implements Mapper<InsertionSequence, InsertionSequenceDTO> {

    private final InsertionCanonicalTargetedExonMapper insertionCanonicalTargetedExonMapper;
    private final InsertionTargetedExonMapper insertionTargetedExonMapper;

    public InsertionSequenceMapper(InsertionCanonicalTargetedExonMapper insertionCanonicalTargetedExonMapper, InsertionTargetedExonMapper insertionTargetedExonMapper) {
        this.insertionCanonicalTargetedExonMapper = insertionCanonicalTargetedExonMapper;
        this.insertionTargetedExonMapper = insertionTargetedExonMapper;
    }

    @Override
    public InsertionSequenceDTO toDto(InsertionSequence insertionSequence) {
        InsertionSequenceDTO insertionSequenceDTO = new InsertionSequenceDTO();
        insertionSequenceDTO.setId(insertionSequence.getId());
        insertionSequenceDTO.setIns(insertionSequence.getIns());
        insertionSequenceDTO.setSequence(insertionSequence.getSequence());
        insertionSequenceDTO.setLocation(insertionSequence.getLocation());
        insertionSequenceDTO.setInsertionCanonicalTargetedExonsDTO(insertionCanonicalTargetedExonMapper.toDtos(insertionSequence.getInsertionCanonicalTargetedExons()));
        insertionSequenceDTO.setInsertionTargetedExonsDTO(insertionTargetedExonMapper.toDtos(insertionSequence.getInsertionTargetedExons()));

        return insertionSequenceDTO;
    }

    @Override
    public InsertionSequence toEntity(InsertionSequenceDTO insertionSequenceDTO) {
        InsertionSequence insertionSequence = new InsertionSequence();
        if (insertionSequenceDTO != null) {
            insertionSequence.setId(insertionSequenceDTO.getId());
            insertionSequence.setSequence(insertionSequenceDTO.getSequence());
            insertionSequence.setLocation(insertionSequenceDTO.getLocation());
            insertionSequence.setIns(buildIns(insertionSequenceDTO.getId()));
        }

        return insertionSequence;
    }

    private String buildIns(Long id) {
        String identifier = String.format("%0" + 12 + "d", id);
        identifier = "INS:" + identifier;
        return identifier;
    }
}
