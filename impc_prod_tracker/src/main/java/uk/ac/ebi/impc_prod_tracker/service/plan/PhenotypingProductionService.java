package uk.ac.ebi.impc_prod_tracker.service.plan;

import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.phenotyping_attempt.PhenotypingAttempt;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.phenotyping_attempt.tissue_distribution.TissueDistribution;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;

import java.util.List;
import java.util.Optional;

public interface PhenotypingProductionService
{
    Optional<PhenotypingAttempt> getPhenotypingProductionByPlan(Plan plan);

    List<TissueDistribution> getTissueDistributionCentresByPhenotypingProduction(
        PhenotypingAttempt phenotypingProduction);
}
