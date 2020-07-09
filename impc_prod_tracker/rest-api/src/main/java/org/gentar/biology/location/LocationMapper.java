package org.gentar.biology.location;

import org.gentar.Mapper;
import org.gentar.biology.strain.StrainMapper;
import org.gentar.biology.species.SpeciesMapper;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper implements Mapper<Location, LocationDTO>
{
    private final StrainMapper strainMapper;
    private final SpeciesMapper speciesMapper;

    public LocationMapper(StrainMapper strainMapper, SpeciesMapper speciesMapper)
    {
        this.strainMapper = strainMapper;
        this.speciesMapper = speciesMapper;
    }

    public LocationDTO toDto(Location location)
    {
        LocationDTO locationDTO = new LocationDTO();
        if (location != null)
        {
            locationDTO.setId(location.getId());
            locationDTO.setChr(location.getChr());
            locationDTO.setStart(location.getStart());
            locationDTO.setStop(location.getStop());
            locationDTO.setStrand(location.getStrand());
            locationDTO.setGenomeBuild(location.getGenomeBuild());
            if (location.getStrain() != null)
            {
                locationDTO.setStrainName(location.getStrain().getName());
            }
            if (location.getSpecies() != null)
            {
                locationDTO.setSpeciesName(location.getSpecies().getName());
            }
        }
        return locationDTO;
    }

    public Location toEntity(LocationDTO locationDTO)
    {
        Location location = new Location();
        if (locationDTO != null)
        {
            location.setId(locationDTO.getId());
            location.setChr(locationDTO.getChr());
            location.setStart(locationDTO.getStart());
            location.setStop(locationDTO.getStop());
            location.setStrand(locationDTO.getStrand());
            location.setGenomeBuild(locationDTO.getGenomeBuild());
            location.setStrain(strainMapper.toEntity(locationDTO.getStrainName()));
            location.setSpecies(speciesMapper.toEntity(locationDTO.getSpeciesName()));
        }
        return location;
    }

}
