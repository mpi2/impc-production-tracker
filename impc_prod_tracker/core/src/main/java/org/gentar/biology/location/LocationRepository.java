package org.gentar.biology.location;

import org.springframework.data.repository.CrudRepository;

public interface LocationRepository extends CrudRepository<Location, Long>
{
    Location findFirstById(Long id);
}
