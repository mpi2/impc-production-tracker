package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PhenotypingStageRepository extends CrudRepository<PhenotypingStage, Long>
{
    PhenotypingStage findByPsn(String psn);

    List<PhenotypingStage> findAll();
}
