package uk.ac.ebi.impc_prod_tracker.web.mapping.location;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.location.Location;
import uk.ac.ebi.impc_prod_tracker.web.dto.location.LocationDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class LocationMapper
{
    public LocationDTO toDto (Location location)
    {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setChr(location.getChr());
        locationDTO.setStart(location.getStart());
        locationDTO.setStop(location.getStop());
        locationDTO.setStrand(location.getStrand());
        locationDTO.setGenomeBuild(location.getGenomeBuild());
        locationDTO.setSpecieName(location.getSpecie().getName());
        return locationDTO;
    }

    public List<LocationDTO> toDtos (Set<Location> locations)
    {
        List<LocationDTO> locationDTOS = new ArrayList<>();
        locations.forEach(location -> locationDTOS.add(toDto(location)));
        return locationDTOS;
    }
}
