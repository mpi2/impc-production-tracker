package uk.ac.ebi.impc_prod_tracker.data.biology.attempt.phenotyping_attempt.tissue_distribution;

import org.springframework.data.repository.CrudRepository;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.phenotyping_attempt.PhenotypingAttempt;

import java.util.List;

public interface TissueDistributionRepository extends CrudRepository<TissueDistribution, Long>
{
    List<TissueDistribution> findAllByPhenotypingAttempt(
        PhenotypingAttempt phenotypingAttempt);
}
