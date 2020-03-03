package org.gentar.biology.species;

import org.springframework.stereotype.Component;

@Component
public class SpeciesServiceImpl implements SpeciesService
{
    private SpeciesRepository speciesRepository;

    public SpeciesServiceImpl (SpeciesRepository speciesRepository) {
        this.speciesRepository = speciesRepository;
    }

    @Override
    public Species getSpeciesByName(String name)
    {
        return speciesRepository.findSpeciesByName(name);
    }
}
