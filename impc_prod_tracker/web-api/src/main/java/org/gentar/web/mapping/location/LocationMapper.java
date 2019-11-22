package org.gentar.web.mapping.location;

import org.gentar.web.dto.location.LocationDTO;
import org.gentar.web.mapping.EntityMapper;
import org.springframework.stereotype.Component;
import org.gentar.biology.location.Location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class LocationMapper
{
    private EntityMapper entityMapper;

    public LocationMapper(EntityMapper entityMapper)
    {
        this.entityMapper = entityMapper;
    }

    public LocationDTO toDto(Location location)
    {
        return entityMapper.toTarget(location, LocationDTO.class);
    }

    public List<LocationDTO> toDtos(Collection<Location> locations)
    {
        List<LocationDTO> locationDTOS = new ArrayList<>();
        locations.forEach(location -> locationDTOS.add(toDto(location)));
        return locationDTOS;
    }

    public Location toEntity(LocationDTO locationDTO)
    {
        return entityMapper.toTarget(locationDTO, Location.class);
    }
}
