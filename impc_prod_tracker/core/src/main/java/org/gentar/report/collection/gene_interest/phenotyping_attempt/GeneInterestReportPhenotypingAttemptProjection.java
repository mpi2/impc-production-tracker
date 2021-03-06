package org.gentar.report.collection.gene_interest.phenotyping_attempt;

import org.springframework.beans.factory.annotation.Value;

public interface GeneInterestReportPhenotypingAttemptProjection {

    @Value("#{target.projectId}")
    Long getProjectId();

    @Value("#{target.projectTpn}")
    String getProjectTpn();

    @Value("#{target.planIdentificationNumber}")
    String getPlanIdentificationNumber();

    @Value("#{target.phenotypingWorkUnit}")
    String getPhenotypingWorkUnit();

    @Value("#{target.phenotypingWorkGroup}")
    String getPhenotypingWorkGroup();

    @Value("#{target.cohortProductionWorkUnit}")
    String getCohortProductionWorkUnit();

    @Value("#{target.phenotypingStageStatus}")
    String getPhenotypingStageStatus();

    @Value("#{target.outcomeId}")
    Long getOutcomeId();
}
