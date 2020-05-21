package org.gentar.biology.species;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class SpeciesServiceImpl implements SpeciesService
{
    private SpeciesRepository speciesRepository;

    public SpeciesServiceImpl (SpeciesRepository speciesRepository) {
        this.speciesRepository = speciesRepository;
    }

    @Override
    @Cacheable("speciesNames")
    public Species getSpeciesByName(String name)
    {
        return speciesRepository.findSpeciesByNameIgnoreCase(name);
    }
}
