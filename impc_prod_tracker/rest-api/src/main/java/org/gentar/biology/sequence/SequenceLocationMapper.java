package org.gentar.biology.sequence;

import org.gentar.biology.location.LocationMapper;
import org.springframework.stereotype.Component;
import org.gentar.biology.sequence_location.SequenceLocation;

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
