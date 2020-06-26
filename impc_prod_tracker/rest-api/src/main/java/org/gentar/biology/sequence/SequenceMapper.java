package org.gentar.biology.sequence;

import org.gentar.Mapper;
import org.springframework.stereotype.Component;
import org.gentar.biology.sequence_location.SequenceLocation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class SequenceMapper implements Mapper<Sequence, SequenceDTO>
{
    private SequenceLocationMapper sequenceLocationMapper;
    private SequenceCategoryMapper sequenceCategoryMapper;
    private SequenceTypeMapper sequenceTypeMapper;

    public SequenceMapper(
        SequenceLocationMapper sequenceLocationMapper,
        SequenceCategoryMapper sequenceCategoryMapper,
        SequenceTypeMapper sequenceTypeMapper)
    {
        this.sequenceLocationMapper = sequenceLocationMapper;
        this.sequenceCategoryMapper = sequenceCategoryMapper;
        this.sequenceTypeMapper = sequenceTypeMapper;
    }

    public SequenceDTO toDto(Sequence sequence)
    {
        SequenceDTO sequenceDTO = new SequenceDTO();
        sequenceDTO.setId(sequence.getId());
        sequenceDTO.setSequence(sequence.getSequence());
        if (sequence.getSequenceType() != null)
        {
            sequenceDTO.setSequenceTypeName(sequence.getSequenceType().getName());
        }
        if (sequence.getSequenceCategory() != null)
        {
            sequenceDTO.setSequenceCategoryName(sequence.getSequenceCategory().getName());
        }
        List<SequenceLocationDTO> sequenceLocationDTOS =
            sequenceLocationMapper.toDtos(sequence.getSequenceLocations());
        sequenceDTO.setSequenceLocationDTOS(sequenceLocationDTOS);
        return sequenceDTO;
    }

    public Sequence toEntity(SequenceDTO sequenceDTO)
    {
        Sequence sequence = new Sequence();
        sequence.setId(sequenceDTO.getId());
        sequence.setSequence(sequenceDTO.getSequence());
        sequence.setSequenceCategory(
            sequenceCategoryMapper.toEntity(sequenceDTO.getSequenceCategoryName()));
        sequence.setSequenceType(
            sequenceTypeMapper.toEntity(sequenceDTO.getSequenceTypeName()));
        setSequenceLocations(sequence, sequenceDTO);
        return sequence;
    }

    private void setSequenceLocations(Sequence sequence, SequenceDTO sequenceDTO)
    {
        if (sequenceDTO.getSequenceLocationDTOS() != null)
        {
            List<SequenceLocation> sequenceLocations =
              new ArrayList<>(sequenceLocationMapper.toEntities(sequenceDTO.getSequenceLocationDTOS())) ;
            sequenceLocations.forEach(x -> x.setSequence(sequence));
            sequence.setSequenceLocations(sequenceLocations);
        }
    }
}
