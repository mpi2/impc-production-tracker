package org.gentar.biology.species;

import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SpeciesMapper {
    private SpeciesService speciesService;

    private static final String SPECIES_NOT_FOUND_ERROR = "Species '%s' does not exist.";

    public SpeciesMapper(SpeciesService speciesService)
    {
        this.speciesService = speciesService;
    }

    public Species toEntity(String name)
    {
        Species species = speciesService.getSpeciesByName(name);
        if (species == null)
        {
            throw new UserOperationFailedException(String.format(SPECIES_NOT_FOUND_ERROR, name));
        }
        return species;
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
