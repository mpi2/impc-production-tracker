package org.gentar.biology.species;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SpeciesMapper {
    private SpeciesService speciesService;

    public SpeciesMapper(SpeciesService speciesService)
    {
        this.speciesService = speciesService;
    }

    public Species toEntity(String name)
    {
        return speciesService.getSpeciesByName(name);
    }

    public Set<Species> toEntities(List<SpeciesDTO> speciesNames)
    {
        Set<Species> species = new HashSet<>();
        if(speciesNames != null)
        {
            speciesNames.forEach(x -> species.add(toEntity(x.getSpeciesName())));
        }

        return  species;
    }
}
