package org.gentar.report.geneInterest.project;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

public interface GeneInterestReportProjectProjection {

    @Value("#{target.projectId}")
    Long getProjectId();

    @Value("#{target.assignmentName}")
    String getAssignmentName();

    @Value("#{target.assignmentDate}")
    LocalDateTime getAssignmentDate();

    @Value("#{target.geneAccId}")
    String getGeneAccId();

    @Value("#{target.geneSymbol}")
    String getGeneSymbol();


}
