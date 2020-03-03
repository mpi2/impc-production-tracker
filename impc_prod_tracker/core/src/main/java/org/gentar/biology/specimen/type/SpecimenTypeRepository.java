package org.gentar.biology.specimen.type;

import org.springframework.data.repository.CrudRepository;

public interface SpecimenTypeRepository extends CrudRepository<SpecimenType, Long>
{
    SpecimenType findByNameIgnoreCase(String name);
}
