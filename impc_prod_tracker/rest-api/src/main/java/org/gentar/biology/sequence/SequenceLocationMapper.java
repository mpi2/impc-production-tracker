package org.gentar.biology.sequence;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.location.Location;
import org.gentar.biology.location.LocationMapper;
import org.springframework.stereotype.Component;
import org.gentar.biology.sequence_location.SequenceLocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class SequenceLocationMapper implements Mapper<SequenceLocation, SequenceLocationDTO>
{
    private LocationMapper locationMapper;
    private EntityMapper entityMapper;

    public SequenceLocationMapper(LocationMapper locationMapper, EntityMapper entityMapper)
    {
        this.locationMapper = locationMapper;
        this.entityMapper = entityMapper;
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

    public SequenceLocation toEntity(SequenceLocationDTO sequenceLocationDTO)
    {
        SequenceLocation sequenceLocation =
                entityMapper.toTarget(sequenceLocationDTO, SequenceLocation.class);
        sequenceLocation.setIndex(sequenceLocationDTO.getLocationIndex());
        setLocations(sequenceLocation, sequenceLocationDTO);
        return sequenceLocation;
    }

    public List<SequenceLocation> toEntities(Collection<SequenceLocationDTO> sequenceLocationDTOS)
    {
        List<SequenceLocation> sequenceLocations = new ArrayList<>();
        if (sequenceLocationDTOS != null)
        {
            sequenceLocationDTOS.forEach(x -> sequenceLocations.add(toEntity(x)));
        }
        return sequenceLocations;
    }

    private void setLocations(SequenceLocation sequenceLocation, SequenceLocationDTO sequenceLocationDTO)
    {
        if (sequenceLocationDTO.getLocationDTO() != null)
        {
            Location location = locationMapper.toEntity(sequenceLocationDTO.getLocationDTO());
            sequenceLocation.setLocation(location);
        }
    }
}
