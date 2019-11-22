package org.gentar.service.biology.plan;

import org.gentar.biology.plan.Plan;
import org.springframework.stereotype.Component;
import org.gentar.biology.phenotyping_attempt.PhenotypingAttempt;
import org.gentar.biology.phenotyping_attempt.PhenotypingAttemptRepository;
import org.gentar.biology.phenotyping_attempt.tissue_distribution.TissueDistribution;
import org.gentar.biology.phenotyping_attempt.tissue_distribution.TissueDistributionRepository;

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
