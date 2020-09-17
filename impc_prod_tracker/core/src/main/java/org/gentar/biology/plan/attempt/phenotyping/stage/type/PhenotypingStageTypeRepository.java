package org.gentar.biology.plan.attempt.phenotyping.stage.type;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PhenotypingStageTypeRepository extends CrudRepository<PhenotypingStageType, Long>
{
    List<PhenotypingStageType> findAll();
    PhenotypingStageType findByNameIgnoreCase(String name);
}
