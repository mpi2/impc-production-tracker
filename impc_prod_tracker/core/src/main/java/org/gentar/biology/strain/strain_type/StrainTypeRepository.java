package org.gentar.biology.strain.strain_type;

import org.springframework.data.repository.CrudRepository;

public interface StrainTypeRepository extends CrudRepository<StrainType, Long> {
    StrainType findByName(String name);
}
