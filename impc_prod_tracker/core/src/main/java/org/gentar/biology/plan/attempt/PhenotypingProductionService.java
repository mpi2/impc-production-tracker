package org.gentar.biology.plan.attempt;

import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttempt;
import org.gentar.biology.plan.attempt.phenotyping.stage.tissue_distribution.TissueDistribution;
import org.gentar.biology.plan.Plan;

import java.util.List;
import java.util.Optional;

public interface PhenotypingProductionService
{
    Optional<PhenotypingAttempt> getPhenotypingProductionByPlan(Plan plan);

    List<TissueDistribution> getTissueDistributionCentresByPhenotypingProduction(
        PhenotypingAttempt phenotypingProduction);
}
