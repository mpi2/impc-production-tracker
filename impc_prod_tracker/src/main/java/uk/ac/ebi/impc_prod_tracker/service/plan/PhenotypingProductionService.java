package uk.ac.ebi.impc_prod_tracker.service.plan;

import uk.ac.ebi.impc_prod_tracker.data.biology.phenotyping_production.PhenotypingProduction;

import java.util.Optional;

public interface PhenotypingProductionService
{
    Optional<PhenotypingProduction> getPhenotypingProductionByPlanId(Long planId);
}
