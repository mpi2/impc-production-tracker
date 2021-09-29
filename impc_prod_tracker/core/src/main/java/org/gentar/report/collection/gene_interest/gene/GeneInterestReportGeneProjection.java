package org.gentar.report.collection.gene_interest.gene;

import org.springframework.beans.factory.annotation.Value;

public interface GeneInterestReportGeneProjection {

    @Value("#{target.projectId}")
    Long getProjectId();

    @Value("#{target.projectTpn}")
    String getProjectTpn();

    @Value("#{target.assignmentName}")
    String getAssignmentName();

    @Value("#{target.planIdentificationNumber}")
    String getPlanIdentificationNumber();

    @Value("#{target.planSummaryStatus}")
    String getPlanSummaryStatus();

    @Value("#{target.outcomeId}")
    Long getOutcomeId();

    @Value("#{target.mutationIdentificationNumber}")
    String getMutationIdentificationNumber();

    @Value("#{target.mutationSymbol}")
    String getMutationSymbol();

    @Value("#{target.geneAccId}")
    String getGeneAccId();

    @Value("#{target.geneSymbol}")
    String getGeneSymbol();

}
