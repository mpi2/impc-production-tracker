package org.gentar.biology.location;

import org.gentar.biology.gene.Gene;
import org.gentar.biology.gene.GeneDTO;
import org.gentar.biology.location.LocationDTO;
import org.gentar.EntityMapper;
import org.gentar.biology.plan.attempt.crispr_attempt.StrainMapper;
import org.gentar.biology.species.SpeciesMapper;
import org.springframework.stereotype.Component;
import org.gentar.biology.location.Location;

import java.util.*;

@Component
public class LocationMapper
{
    private EntityMapper entityMapper;
    private StrainMapper strainMapper;
    private SpeciesMapper speciesMapper;

    public LocationMapper(EntityMapper entityMapper, StrainMapper strainMapper, SpeciesMapper speciesMapper)
    {
        this.entityMapper = entityMapper;
        this.strainMapper = strainMapper;
        this.speciesMapper = speciesMapper;
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
        Location location = entityMapper.toTarget(locationDTO, Location.class);
        location.setStrain(strainMapper.toEntity(locationDTO.getStrainName()));
        location.setSpecies(speciesMapper.toEntity(locationDTO.getSpeciesName().getSpeciesName()));
        return location;
    }
}
