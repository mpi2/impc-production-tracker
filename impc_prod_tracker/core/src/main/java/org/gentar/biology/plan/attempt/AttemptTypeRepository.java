package org.gentar.biology.plan.attempt;

import org.springframework.data.repository.CrudRepository;

public interface AttemptTypeRepository extends CrudRepository<AttemptType, Long>
{
    AttemptType findByNameIgnoreCase(String attemptTypeName);
}
