package org.gentar.biology.species;

public interface SpeciesService
{
    /**
     * Get a {@link Species} object with the information of the species identified by the given name.
     * @return Species identified by name. Null if not found.
     */
    Species getSpeciesByName(String name);
}
