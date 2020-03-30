package org.gentar.biology.plan.attempt;

import org.gentar.biology.plan.Plan;
import org.springframework.stereotype.Component;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttempt;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttemptRepository;
import org.gentar.biology.plan.attempt.phenotyping.stage.tissue_distribution.TissueDistribution;
import org.gentar.biology.plan.attempt.phenotyping.stage.tissue_distribution.TissueDistributionRepository;

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
