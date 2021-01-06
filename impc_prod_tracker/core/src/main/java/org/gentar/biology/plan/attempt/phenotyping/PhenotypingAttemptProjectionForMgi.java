package org.gentar.biology.plan.attempt.phenotyping;

import org.springframework.beans.factory.annotation.Value;

public interface PhenotypingAttemptProjectionForMgi {

    @Value("#{target.colonyName}")
    String getColonyName();

    @Value("#{target.strainName}")
    String getStrainName();

    @Value("#{target.strainAccId}")
    String getStrainAccId();

    @Value("#{target.productionWorkUnit}")
    String getProductionWorkUnit();

    @Value("#{target.productionWorkGroup}")
    String getProductionWorkGroup();

    @Value("#{target.phenotypingWorkUnit}")
    String getPhenotypingWorkUnit();

    @Value("#{target.phenotypingWorkGroup}")
    String getPhenotypingWorkGroup();

    @Value("#{target.cohortProductionWorkUnit}")
    String getCohortProductionWorkUnit();

    @Value("#{target.outcomeId}")
    Long getOutcomeId();
}
