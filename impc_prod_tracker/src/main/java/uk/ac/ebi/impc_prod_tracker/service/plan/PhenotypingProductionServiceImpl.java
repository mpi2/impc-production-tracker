package uk.ac.ebi.impc_prod_tracker.service.plan;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.phenotyping_attempt.PhenotypingAttempt;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.phenotyping_attempt.PhenotypingAttemptRepository;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.phenotyping_attempt.tissue_distribution_centre.TissueDistributionCentre;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.phenotyping_attempt.tissue_distribution_centre.TissueDistributionCentreRepository;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;
import java.util.List;
import java.util.Optional;

@Component
public class PhenotypingProductionServiceImpl implements PhenotypingProductionService
{
    private PhenotypingAttemptRepository phenotypingProductionRepository;
    private TissueDistributionCentreRepository tissueDistributionCentreRepository;

    public PhenotypingProductionServiceImpl(
        PhenotypingAttemptRepository phenotypingProductionRepository,
        TissueDistributionCentreRepository tissueDistributionCentreRepository)
    {
        this.phenotypingProductionRepository = phenotypingProductionRepository;
        this.tissueDistributionCentreRepository = tissueDistributionCentreRepository;
    }

    public Optional<PhenotypingAttempt> getPhenotypingProductionByPlan(Plan plan)
    {
        Optional<PhenotypingAttempt> phenotypingProductionOpt =
            phenotypingProductionRepository.findById(plan.getId());
        return phenotypingProductionOpt;
    }

    public List<TissueDistributionCentre> getTissueDistributionCentresByPhenotypingProduction(
        PhenotypingAttempt phenotypingProduction)
    {
//        return
//            tissueDistributionCentreRepository.findAllByPhenotypingProduction(phenotypingProduction);
        return null;
    }
}
