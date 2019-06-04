package uk.ac.ebi.impc_prod_tracker.data.biology.tissue_distribution_centre;

import org.springframework.data.repository.CrudRepository;
import uk.ac.ebi.impc_prod_tracker.data.biology.phenotyping_production.PhenotypingProduction;
import java.util.List;

public interface TissueDistributionCentreRepository extends CrudRepository<TissueDistributionCentre, Long>
{
    List<TissueDistributionCentre> findAllByPhenotypingProduction(
        PhenotypingProduction phenotypingProduction);
}
