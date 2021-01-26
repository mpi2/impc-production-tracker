package org.gentar.report.collection.gene_interest.project;

import org.gentar.biology.project.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GeneInterestReportProjectRepository extends CrudRepository<Project, Long> {
    @Query("select " +
            "p.id as projectId, " +
            "p.tpn as projectTpn, " +
            "assign.name as assignmentName, " +
            "g.accId as geneAccId, " +
            "g.symbol as geneSymbol, " +
            "plan.pin as planIdentificationNumber, " +
            "plan_summary_status.name as planSummaryStatus " +
            "FROM Project p " +
            "INNER JOIN Privacy priv on p.privacy=priv " +
            "INNER JOIN AssignmentStatus assign on p.assignmentStatus = assign " +
            "INNER JOIN ProjectIntention pi on p = pi.project " +
            "INNER JOIN ProjectIntentionGene pig on pi=pig.projectIntention " +
            "INNER JOIN Gene g on pig.gene=g " +
            "INNER JOIN Plan plan on plan.project = p " +
            "INNER JOIN PlanType pt ON plan.planType = pt " +
            "INNER JOIN AttemptType at ON plan.attemptType = at " +
            "INNER JOIN Status plan_summary_status ON plan.summaryStatus = plan_summary_status " +
            "where " +
            "priv.name='public' and " +
            "pt.name='production' and " +
            "at.name='crispr' ")
    List<GeneInterestReportProjectProjection> findAllGeneInterestReportCrisprProjectProjections();
}
