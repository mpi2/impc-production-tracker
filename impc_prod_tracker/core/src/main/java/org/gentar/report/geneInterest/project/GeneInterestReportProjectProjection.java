package org.gentar.report.geneInterest.project;

import org.springframework.beans.factory.annotation.Value;

public interface GeneInterestReportProjectProjection {

    @Value("#{target.projectId}")
    Long getProjectId();

    @Value("#{target.projectTpn}")
    String getProjectTpn();

    @Value("#{target.assignmentName}")
    String getAssignmentName();

    @Value("#{target.geneAccId}")
    String getGeneAccId();

    @Value("#{target.geneSymbol}")
    String getGeneSymbol();

    @Value("#{target.planIdentificationNumber}")
    String getPlanIdentificationNumber();

    @Value("#{target.planSummaryStatus}")
    String getPlanSummaryStatus();

}
