package uk.ac.ebi.impc_prod_tracker.data.biology.attempt.phenotyping_attempt.tissue_distribution_centre;

import org.springframework.data.repository.CrudRepository;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.phenotyping_attempt.PhenotypingAttempt;

import java.util.List;

public interface TissueDistributionCentreRepository extends CrudRepository<TissueDistributionCentre, Long>
{
    List<TissueDistributionCentre> findAllByPhenotypingAttempt(
        PhenotypingAttempt phenotypingAttempt);
}
