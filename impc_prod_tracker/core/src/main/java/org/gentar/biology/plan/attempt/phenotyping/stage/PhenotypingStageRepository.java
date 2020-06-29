package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PhenotypingStageRepository extends CrudRepository<PhenotypingStage, Long>
{
    @Query("SELECT max(ps.psn) FROM PhenotypingStage ps")
    String getMaxPsn();

    PhenotypingStage findByPsn(String psn);

    List<PhenotypingStage> findAll();
}
