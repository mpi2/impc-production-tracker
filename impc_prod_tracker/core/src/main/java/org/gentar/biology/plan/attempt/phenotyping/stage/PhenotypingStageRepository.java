package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PhenotypingStageRepository extends CrudRepository<PhenotypingStage, Long>
{
    @Query("SELECT max(ps.pps) FROM PhenotypingStage ps")
    String getMaxPps();
}
