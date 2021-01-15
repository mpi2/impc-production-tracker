package org.gentar.report.geneInterest.gene;

import org.gentar.biology.gene.Gene;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GeneInterestReportGeneRepository extends CrudRepository<Gene, Long> {
    @Query("select " +
            "p.id as projectId, " +
            "p.tpn as projectTpn, " +
            "assign.name as assignmentName, " +
            "plan.pin as planIdentificationNumber, " +
            "plan_summary_status.name as planSummaryStatus, " +
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
            "INNER JOIN AttemptType at ON plan.attemptType = at " +
            "INNER JOIN Project p on plan.project=p " +
            "INNER JOIN Privacy priv on p.privacy=priv " +
            "INNER JOIN AssignmentStatus assign on p.assignmentStatus = assign " +
            "where " +
            "priv.name='public' and " +
            "at.name='crispr' and " + // Ensure no haplo-essential production plans included (production plans are implicit)
            "ot.name = 'Colony'  and " +
            "colony_status.name <> 'Colony Aborted' " // Make sure no mutations are linked to aborted colonies when looking for projects
    )
    List<GeneInterestReportGeneProjection> findAllGeneInterestReportCrisprGeneProjections();
}
