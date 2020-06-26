package org.gentar.biology.sequence;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.location.Location;
import org.gentar.biology.location.LocationMapper;
import org.springframework.stereotype.Component;
import org.gentar.biology.sequence_location.SequenceLocation;

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
        sequenceLocationDTO.setId(sequenceLocation.getId());
        sequenceLocationDTO.setLocationDTO(locationMapper.toDto(sequenceLocation.getLocation()));
        sequenceLocationDTO.setLocationIndex(sequenceLocation.getIndex());
        return sequenceLocationDTO;
    }

    public SequenceLocation toEntity(SequenceLocationDTO sequenceLocationDTO)
    {
        SequenceLocation sequenceLocation =
                entityMapper.toTarget(sequenceLocationDTO, SequenceLocation.class);
        sequenceLocation.setId(sequenceLocationDTO.getId());
        sequenceLocation.setIndex(sequenceLocationDTO.getLocationIndex());
        setLocations(sequenceLocation, sequenceLocationDTO);
        return sequenceLocation;
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
