package org.gentar.report.collection.phenotyping_colony.phenotyping.phenotyping_attempt;

import org.springframework.beans.factory.annotation.Value;

public interface PhenotypingColonyReportPhenotypingAttemptProjection {

    @Value("#{target.colonyName}")
    String getColonyName();

    @Value("#{target.strainName}")
    String getStrainName();

    @Value("#{target.strainAccId}")
    String getStrainAccId();

    @Value("#{target.esCellName}")
    String getEsCellName();

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
