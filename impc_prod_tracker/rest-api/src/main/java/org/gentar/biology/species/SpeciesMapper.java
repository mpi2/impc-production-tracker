package org.gentar.biology.species;

import org.gentar.Mapper;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

@Component
public class SpeciesMapper implements Mapper<Species, String>
{
    private SpeciesService speciesService;

    private static final String SPECIES_NOT_FOUND_ERROR = "Species '%s' does not exist.";

    public SpeciesMapper(SpeciesService speciesService)
    {
        this.speciesService = speciesService;
    }

    @Override
    public String toDto(Species species)
    {
        String speciesName = null;
        if (species != null)
        {
            speciesName = species.getName();
        }
        return speciesName;
    }

    @Override
    public Species toEntity(String name)
    {
        Species species = speciesService.getSpeciesByName(name);
        if (species == null)
        {
            throw new UserOperationFailedException(String.format(SPECIES_NOT_FOUND_ERROR, name));
        }
        return species;
    }
}
