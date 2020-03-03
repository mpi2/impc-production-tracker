package org.gentar.biology.outcome.type;

import org.springframework.data.repository.CrudRepository;

public interface OutcomeTypeRepository extends CrudRepository<OutcomeType, Long>
{
    OutcomeType findByNameIgnoreCase(String name);
}
