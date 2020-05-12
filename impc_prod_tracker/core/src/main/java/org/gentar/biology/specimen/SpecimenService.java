package org.gentar.biology.specimen;

import org.gentar.biology.specimen.type.SpecimenType;
import org.gentar.statemachine.ProcessEvent;

public interface SpecimenService
{
    SpecimenType getSpecimenTypeByName(String name);

    /**
     * Gets the Process event that corresponds to the name of the transition 'name'.
     * @param specimen specimen to evaluate.
     * @param name The name of the transition that is going to be evaluated on the specimen.
     * @return A {@link ProcessEvent} corresponding to the given 'name'.
     */
    ProcessEvent getProcessEventByName(Specimen specimen, String name);
}
