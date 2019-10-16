package uk.ac.ebi.impc_prod_tracker.web.mapping.sequence;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.sequence_location.SequenceLocation;
import uk.ac.ebi.impc_prod_tracker.web.dto.sequence.SequenceLocationDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.location.LocationMapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class SequenceLocationMapper
{
    private LocationMapper locationMapper;

    public SequenceLocationMapper(LocationMapper locationMapper)
    {
        this.locationMapper = locationMapper;
    }

    public SequenceLocationDTO toDto(SequenceLocation sequenceLocation)
    {
        SequenceLocationDTO sequenceLocationDTO = new SequenceLocationDTO();
        sequenceLocationDTO.setLocationDTO(locationMapper.toDto(sequenceLocation.getLocation()));
        sequenceLocationDTO.setLocationIndex(sequenceLocation.getIndex());
        return sequenceLocationDTO;
    }

    public List<SequenceLocationDTO> toDtos(Collection<SequenceLocation> sequenceLocations)
    {
        List<SequenceLocationDTO> sequenceLocationDTOS = new ArrayList<>();
        if (sequenceLocations != null)
        {
            sequenceLocations.forEach(x -> sequenceLocationDTOS.add(toDto(x)));
        }
        return sequenceLocationDTOS;
    }
}
