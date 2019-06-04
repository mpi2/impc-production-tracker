package uk.ac.ebi.impc_prod_tracker.service.plan;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.phenotyping_production.PhenotypingProduction;
import uk.ac.ebi.impc_prod_tracker.data.biology.phenotyping_production.PhenotypingProductionRepository;
import uk.ac.ebi.impc_prod_tracker.data.biology.tissue_distribution_centre.TissueDistributionCentre;
import uk.ac.ebi.impc_prod_tracker.data.biology.tissue_distribution_centre.TissueDistributionCentreRepository;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import java.util.List;
import java.util.Optional;

@Component
public class PhenotypingProductionServiceImpl implements PhenotypingProductionService
{
    private PhenotypingProductionRepository phenotypingProductionRepository;
    private TissueDistributionCentreRepository tissueDistributionCentreRepository;

    public PhenotypingProductionServiceImpl(
        PhenotypingProductionRepository phenotypingProductionRepository,
        TissueDistributionCentreRepository tissueDistributionCentreRepository)
    {
        this.phenotypingProductionRepository = phenotypingProductionRepository;
        this.tissueDistributionCentreRepository = tissueDistributionCentreRepository;
    }

    public Optional<PhenotypingProduction> getPhenotypingProductionByPlan(Plan plan)
    {
        Optional<PhenotypingProduction> phenotypingProductionOpt =
            phenotypingProductionRepository.findById(plan.getId());
        return phenotypingProductionOpt;
    }

    public List<TissueDistributionCentre> getTissueDistributionCentresByPhenotypingProduction(
        PhenotypingProduction phenotypingProduction)
    {
        return
            tissueDistributionCentreRepository.findAllByPhenotypingProduction(phenotypingProduction);
    }
}
