package uk.ac.ebi.impc_prod_tracker.service.plan;

import uk.ac.ebi.impc_prod_tracker.data.biology.phenotyping_production.PhenotypingProduction;
import uk.ac.ebi.impc_prod_tracker.data.biology.tissue_distribution_centre.TissueDistributionCentre;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;

import java.util.List;
import java.util.Optional;

public interface PhenotypingProductionService
{
    Optional<PhenotypingProduction> getPhenotypingProductionByPlan(Plan plan);

    List<TissueDistributionCentre> getTissueDistributionCentresByPhenotypingProduction(
        PhenotypingProduction phenotypingProduction);
}
