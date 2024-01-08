package org.gentar.report.collection.gene_interest.project;

import org.gentar.biology.project.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
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
            "INNER JOIN Status plan_summary_status ON plan.summaryStatus = plan_summary_status " + // "LEFT JOIN CrisprAttempt ca ON plan = ca.plan " +  // Require LEFT JOIN as not all plans have attempts
            "where " +
            "priv.name='public' and " +
            "pt.name='production' and " +
            "at.name IN ('crispr', 'crispr allele modification') ") // and " + "(ca.experimental IS NULL OR ca.experimental=false) ") // Critera used in iMits to exclude some data
    List<GeneInterestReportProjectProjection> findAllGeneInterestReportCrisprProjectProjections();

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
            "INNER JOIN WorkGroup wg ON plan.workGroup = wg " + // "LEFT JOIN EsCellAttempt esa ON plan = esa.plan " +  // Require LEFT JOIN as not all plans have attempts
            "where " +
            "priv.name='public' and " +
            "pt.name='production' and " +
            "at.name in ('es cell', 'es cell allele modification') and " + //           "(esa.experimental IS NULL OR esa.experimental=false) and " +   // Critera used in iMits to exclude some data
            "p.esCellQcOnly=false and " +                                   // Critera used in iMits to exclude some data
            "wg.name <> 'EUCOMMToolsCre' ")                                 // Critera used in iMits to exclude some data
    List<GeneInterestReportProjectProjection> findAllGeneInterestReportEsCellProjectProjections();
}
