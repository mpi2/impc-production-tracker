package org.gentar.biology.mutation;

import org.gentar.Mapper;
import org.gentar.biology.insertion_sequence.InsertionSequence;
import org.gentar.biology.plan.attempt.crispr.canonical_targeted_exon.InsertionCanonicalTargetedExon;
import org.gentar.biology.plan.attempt.crispr.targeted_exon.InsertionTargetedExon;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
            setInsertionCanonicalTargetedExons(insertionSequence,insertionSequenceDTO);
            setInsertionTargetedExons(insertionSequence,insertionSequenceDTO);
        }

        return insertionSequence;
    }
    private void setInsertionCanonicalTargetedExons(InsertionSequence insertionSequence ,InsertionSequenceDTO insertionSequenceDTO)
    {
        List<InsertionCanonicalTargetedExonDTO> dtoList = insertionSequenceDTO.getInsertionCanonicalTargetedExonsDTO();

        Set<InsertionCanonicalTargetedExon> exonSet = dtoList.stream()
                .map(dto -> {
                    InsertionCanonicalTargetedExon exon = insertionCanonicalTargetedExonMapper.toEntity(dto);
                    exon.setInsertionSequence(insertionSequence); // establish parent-child relationship
                    return exon;
                })
                .collect(Collectors.toSet());

        insertionSequence.setInsertionCanonicalTargetedExons(exonSet);
    }

    private void setInsertionTargetedExons(InsertionSequence insertionSequence ,InsertionSequenceDTO insertionSequenceDTO)
    {
        List<InsertionTargetedExonDTO> dtoList = insertionSequenceDTO.getInsertionTargetedExonsDTO();

        Set<InsertionTargetedExon> exonSet = dtoList.stream()
                .map(dto -> {
                    InsertionTargetedExon exon = insertionTargetedExonMapper.toEntity(dto);
                    exon.setInsertionSequence(insertionSequence); // establish parent-child relationship
                    return exon;
                })
                .collect(Collectors.toSet());

        insertionSequence.setInsertionTargetedExons(exonSet);
    }

}
