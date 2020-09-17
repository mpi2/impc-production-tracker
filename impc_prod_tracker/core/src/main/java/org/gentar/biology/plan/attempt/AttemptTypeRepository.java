package org.gentar.biology.plan.attempt;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AttemptTypeRepository extends CrudRepository<AttemptType, Long>
{
    List<AttemptType> findAll();
    AttemptType findByNameIgnoreCase(String attemptTypeName);
}
