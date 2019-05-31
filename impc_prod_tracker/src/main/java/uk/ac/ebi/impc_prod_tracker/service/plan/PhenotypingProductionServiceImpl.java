package uk.ac.ebi.impc_prod_tracker.service.plan;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.phenotyping_production.PhenotypingProduction;
import uk.ac.ebi.impc_prod_tracker.data.biology.phenotyping_production.PhenotypingProductionRepository;

import java.util.Optional;

@Component
public class PhenotypingProductionServiceImpl implements PhenotypingProductionService
{
    private PhenotypingProductionRepository phenotypingProductionRepository;

    public PhenotypingProductionServiceImpl(
        PhenotypingProductionRepository phenotypingProductionRepository)
    {
        this.phenotypingProductionRepository = phenotypingProductionRepository;
    }

    public Optional<PhenotypingProduction> getPhenotypingProductionByPlanId(Long planId)
    {
        Optional<PhenotypingProduction> phenotypingProductionOpt =
            phenotypingProductionRepository.findById(planId);
        return phenotypingProductionOpt;
    }
}
