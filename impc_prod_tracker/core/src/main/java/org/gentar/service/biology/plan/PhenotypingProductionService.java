package org.gentar.service.biology.plan;

import org.gentar.biology.phenotyping_attempt.PhenotypingAttempt;
import org.gentar.biology.phenotyping_attempt.tissue_distribution.TissueDistribution;
import org.gentar.biology.plan.Plan;

import java.util.List;
import java.util.Optional;

public interface PhenotypingProductionService
{
    Optional<PhenotypingAttempt> getPhenotypingProductionByPlan(Plan plan);

    List<TissueDistribution> getTissueDistributionCentresByPhenotypingProduction(
        PhenotypingAttempt phenotypingProduction);
}
