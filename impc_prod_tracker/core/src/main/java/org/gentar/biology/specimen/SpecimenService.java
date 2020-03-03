package org.gentar.biology.specimen;

import org.gentar.biology.specimen.type.SpecimenType;

public interface SpecimenService
{
    SpecimenType getSpecimenTypeByName(String name);
}
