package org.gentar.biology.plan.attempt.phenotyping.stage.tissue_distribution;

import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TissueDistributionRepository extends CrudRepository<TissueDistribution, Long>
{
    List<TissueDistribution> findAllByPhenotypingStage(PhenotypingStage phenotypingStage);
}
