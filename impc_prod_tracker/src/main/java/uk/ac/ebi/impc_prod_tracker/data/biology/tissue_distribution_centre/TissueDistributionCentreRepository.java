package uk.ac.ebi.impc_prod_tracker.data.biology.tissue_distribution_centre;

import org.springframework.data.repository.CrudRepository;
import uk.ac.ebi.impc_prod_tracker.data.biology.phenotyping_production.PhenotypingProduction;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;

public interface TissueDistributionCentreRepository extends CrudRepository<TissueDistributionCentre, Long>
{
    Iterable<TissueDistributionCentre> findAllByPhenotypingProduction(PhenotypingProduction phenotypingProduction);
}
