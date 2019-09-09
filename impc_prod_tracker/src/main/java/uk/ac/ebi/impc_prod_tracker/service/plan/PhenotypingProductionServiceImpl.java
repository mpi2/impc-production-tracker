package uk.ac.ebi.impc_prod_tracker.service.plan;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.phenotyping_attempt.PhenotypingAttempt;
import uk.ac.ebi.impc_prod_tracker.data.biology.phenotyping_attempt.PhenotypingAttemptRepository;
import uk.ac.ebi.impc_prod_tracker.data.biology.phenotyping_attempt.tissue_distribution.TissueDistribution;
import uk.ac.ebi.impc_prod_tracker.data.biology.phenotyping_attempt.tissue_distribution.TissueDistributionRepository;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;
import java.util.List;
import java.util.Optional;

@Component
public class PhenotypingProductionServiceImpl implements PhenotypingProductionService
{
    private PhenotypingAttemptRepository phenotypingProductionRepository;
    private TissueDistributionRepository tissueDistributionRepository;

    public PhenotypingProductionServiceImpl(
        PhenotypingAttemptRepository phenotypingProductionRepository,
        TissueDistributionRepository tissueDistributionRepository)
    {
        this.phenotypingProductionRepository = phenotypingProductionRepository;
        this.tissueDistributionRepository = tissueDistributionRepository;
    }

    public Optional<PhenotypingAttempt> getPhenotypingProductionByPlan(Plan plan)
    {
        Optional<PhenotypingAttempt> phenotypingProductionOpt =
            phenotypingProductionRepository.findById(plan.getId());
        return phenotypingProductionOpt;
    }

    public List<TissueDistribution> getTissueDistributionCentresByPhenotypingProduction(
        PhenotypingAttempt phenotypingProduction)
    {
//        return
//            tissueDistributionRepository.findAllByPhenotypingProduction(phenotypingProduction);
        return null;
    }
}
