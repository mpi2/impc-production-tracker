package org.gentar.biology.plan.attempt.phenotyping.tissue_distribution;

import org.springframework.data.repository.CrudRepository;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttempt;

import java.util.List;

public interface TissueDistributionRepository extends CrudRepository<TissueDistribution, Long>
{
    List<TissueDistribution> findAllByPhenotypingAttempt(
        PhenotypingAttempt phenotypingAttempt);
}
