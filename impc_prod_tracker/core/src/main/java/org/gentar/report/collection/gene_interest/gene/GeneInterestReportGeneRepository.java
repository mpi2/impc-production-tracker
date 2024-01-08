package org.gentar.report.collection.gene_interest.gene;

import org.gentar.biology.gene.Gene;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface GeneInterestReportGeneRepository extends CrudRepository<Gene, Long> {
    @Query("select " +
            "p.id as projectId, " +
            "p.tpn as projectTpn, " +
            "assign.name as assignmentName, " +
            "plan.pin as planIdentificationNumber, " +
            "plan_summary_status.name as planSummaryStatus, " +
            "o.id as outcomeId, " +
            "m.min as mutationIdentificationNumber, " +
            "m.symbol as mutationSymbol, " +
            "g.accId as geneAccId, " +
            "g.symbol as geneSymbol " +
            "FROM Gene g " +
            "INNER JOIN g.mutations m " +
            "INNER JOIN m.outcomes o " +
            "INNER JOIN OutcomeType ot ON o.outcomeType = ot " +
            "INNER JOIN Colony c ON o.colony = c " +
            "INNER JOIN Status colony_status ON c.status = colony_status " +
            "INNER JOIN Plan plan on o.plan=plan " +
            "INNER JOIN CrisprAttempt crispr_attempt on plan=crispr_attempt.plan " +
            "INNER JOIN Status plan_summary_status ON plan.summaryStatus = plan_summary_status " +
            "INNER JOIN AttemptType at ON plan.attemptType = at " +
            "INNER JOIN Project p on plan.project=p " +
            "INNER JOIN Privacy priv on p.privacy=priv " +
            "INNER JOIN AssignmentStatus assign on p.assignmentStatus = assign " +
            "where " +
            "priv.name='public' and " +
            "at.name IN ('crispr', 'crispr allele modification') and " + // Ensure no haplo-essential production plans included (production plans are implicit) //            "crispr_attempt.experimental=false and " +    // Critera used in iMits to exclude some data
            "ot.name = 'Colony'  and " +
            "colony_status.name <> 'Colony Aborted' " // Make sure no mutations are linked to aborted colonies when looking for projects
    )
    List<GeneInterestReportGeneProjection> findAllGeneInterestReportCrisprGeneProjections();

    @Query("select " +
            "p.id as projectId, " +
            "p.tpn as projectTpn, " +
            "assign.name as assignmentName, " +
            "plan.pin as planIdentificationNumber, " +
            "plan_summary_status.name as planSummaryStatus, " +
            "o.id as outcomeId, " +
            "m.min as mutationIdentificationNumber, " +
            "m.symbol as mutationSymbol, " +
            "g.accId as geneAccId, " +
            "g.symbol as geneSymbol " +
            "FROM Gene g " +
            "INNER JOIN g.mutations m " +
            "INNER JOIN m.outcomes o " +
            "INNER JOIN OutcomeType ot ON o.outcomeType = ot " +
            "INNER JOIN Colony c ON o.colony = c " +
            "INNER JOIN Status colony_status ON c.status = colony_status " +
            "INNER JOIN Plan plan on o.plan=plan " +
            "INNER JOIN Status plan_summary_status ON plan.summaryStatus = plan_summary_status " +
            "INNER JOIN AttemptType at ON plan.attemptType = at " + //           "INNER JOIN EsCellAttempt esa ON plan = esa.plan " +
            "INNER JOIN WorkGroup wg ON plan.workGroup = wg " +
            "INNER JOIN Project p on plan.project=p " +
            "INNER JOIN Privacy priv on p.privacy=priv " +
            "INNER JOIN AssignmentStatus assign on p.assignmentStatus = assign " +
            "where " +
            "priv.name='public' and " +
            "at.name in ('es cell', 'es cell allele modification') and " + // Ensure no haplo-essential production plans included (production plans are implicit) //           "esa.experimental = false  and " +    // Critera used in iMits to exclude some data
            "wg.name <> 'EUCOMMToolsCre' and " +  // Critera used in iMits to exclude some data
            "p.esCellQcOnly = false and " +       // Critera used in iMits to exclude some data
            "ot.name = 'Colony'  and " +
            "colony_status.name <> 'Colony Aborted' " // Make sure no mutations are linked to aborted colonies when looking for projects
    )
    List<GeneInterestReportGeneProjection> findAllGeneInterestReportEsCellGeneProjections();
}
