package org.gentar.biology.sequence;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.springframework.stereotype.Component;
import org.gentar.biology.sequence_location.SequenceLocation;
import org.gentar.biology.sequence_location.SequenceLocationRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class SequenceMapper implements Mapper<Sequence, SequenceDTO>
{
    private EntityMapper entityMapper;
    private SequenceLocationRepository sequenceLocationRepository;
    private SequenceLocationMapper sequenceLocationMapper;
    private SequenceCategoryMapper sequenceCategoryMapper;
    private SequenceTypeMapper sequenceTypeMapper;


    public SequenceMapper(
        EntityMapper entityMapper,
        SequenceLocationRepository sequenceLocationRepository,
        SequenceLocationMapper sequenceLocationMapper,
        SequenceCategoryMapper sequenceCategoryMapper,
        SequenceTypeMapper sequenceTypeMapper)
    {
        this.entityMapper = entityMapper;
        this.sequenceLocationRepository = sequenceLocationRepository;
        this.sequenceLocationMapper = sequenceLocationMapper;
        this.sequenceCategoryMapper = sequenceCategoryMapper;
        this.sequenceTypeMapper = sequenceTypeMapper;
    }

    public SequenceDTO toDto(Sequence sequence)
    {
        SequenceDTO sequenceDTO = entityMapper.toTarget(sequence,SequenceDTO.class);
        List<SequenceLocation> sequenceLocations =
            sequenceLocationRepository.findAllBySequence(sequence);
        List<SequenceLocationDTO> sequenceLocationDTOS = sequenceLocationMapper.toDtos(sequenceLocations);
        sequenceDTO.setSequenceLocationDTOS(sequenceLocationDTOS);
        return sequenceDTO;
    }

    public List<SequenceDTO> toDtos(Collection<Sequence> sequences)
    {
        List<SequenceDTO> sequenceDTOS = new ArrayList<>();
        if (sequences != null)
        {
            sequences.forEach(x -> sequenceDTOS.add(toDto(x)));
        }
        return sequenceDTOS;
    }

    public Sequence toEntity(SequenceDTO sequenceDTO)
    {
        Sequence sequence = entityMapper.toTarget(sequenceDTO, Sequence.class);
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
                sequenceLocationMapper.toEntities(sequenceDTO.getSequenceLocationDTOS());
            sequenceLocations.forEach(x -> x.setSequence(sequence));
            sequence.setSequenceLocations(sequenceLocations);
        }

    }
}
