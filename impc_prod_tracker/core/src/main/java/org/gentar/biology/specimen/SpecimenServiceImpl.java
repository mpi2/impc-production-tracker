package org.gentar.biology.specimen;

import org.gentar.biology.specimen.type.SpecimenType;
import org.gentar.biology.specimen.type.SpecimenTypeRepository;
import org.springframework.stereotype.Component;

@Component
public class SpecimenServiceImpl implements SpecimenService
{
    private SpecimenTypeRepository specimenTypeRepository;

    public SpecimenServiceImpl(SpecimenTypeRepository specimenTypeRepository)
    {
        this.specimenTypeRepository = specimenTypeRepository;
    }

    public SpecimenType getSpecimenTypeByName(String name)
    {
        return specimenTypeRepository.findByNameIgnoreCase(name);
    }
}
