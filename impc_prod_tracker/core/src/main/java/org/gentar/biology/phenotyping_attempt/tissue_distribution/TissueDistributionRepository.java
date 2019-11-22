package org.gentar.biology.phenotyping_attempt.tissue_distribution;

import org.springframework.data.repository.CrudRepository;
import org.gentar.biology.phenotyping_attempt.PhenotypingAttempt;

import java.util.List;

public interface TissueDistributionRepository extends CrudRepository<TissueDistribution, Long>
{
    List<TissueDistribution> findAllByPhenotypingAttempt(
        PhenotypingAttempt phenotypingAttempt);
}
