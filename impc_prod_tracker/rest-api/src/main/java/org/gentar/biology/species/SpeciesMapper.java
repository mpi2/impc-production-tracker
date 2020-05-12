package org.gentar.biology.species;

import org.gentar.Mapper;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.List;

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
    public String toDto(Species entity) {
        return null;
    }

    @Override
    public List<String> toDtos(Collection<Species> entities) {
        return null;
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
