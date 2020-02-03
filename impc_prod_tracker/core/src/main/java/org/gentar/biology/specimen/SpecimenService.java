package org.gentar.biology.specimen;

import org.springframework.stereotype.Component;

@Component
public class SpecimenService
{
    private SpecimenTypeRepository specimenTypeRepository;

    public SpecimenService(SpecimenTypeRepository specimenTypeRepository)
    {
        this.specimenTypeRepository = specimenTypeRepository;
    }

    public SpecimenType getSpecimenTypeByName(String name)
    {
        return specimenTypeRepository.findByNameIgnoreCase(name);
    }
}
