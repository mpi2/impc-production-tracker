package uk.ac.ebi.impc_prod_tracker.web.mapping.sequence;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.sequence.Sequence;
import uk.ac.ebi.impc_prod_tracker.data.biology.sequence_location.SequenceLocation;
import uk.ac.ebi.impc_prod_tracker.data.biology.sequence_location.SequenceLocationRepository;
import uk.ac.ebi.impc_prod_tracker.web.dto.sequence.SequenceDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.sequence.SequenceLocationDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.EntityMapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class SequenceMapper
{
    private EntityMapper entityMapper;
    private SequenceLocationRepository sequenceLocationRepository;
    private SequenceLocationMapper sequenceLocationMapper;

    public SequenceMapper(
        EntityMapper entityMapper,
        SequenceLocationRepository sequenceLocationRepository,
        SequenceLocationMapper sequenceLocationMapper)
    {
        this.entityMapper = entityMapper;
        this.sequenceLocationRepository = sequenceLocationRepository;
        this.sequenceLocationMapper = sequenceLocationMapper;
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
        return entityMapper.toTarget(sequenceDTO, Sequence.class);
    }
}
