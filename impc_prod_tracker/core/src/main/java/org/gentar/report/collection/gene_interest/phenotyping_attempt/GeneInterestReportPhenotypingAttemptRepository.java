package org.gentar.report.collection.gene_interest.phenotyping_attempt;

import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttempt;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GeneInterestReportPhenotypingAttemptRepository extends CrudRepository<PhenotypingAttempt, Long> {
    @Query("select proj.id as projectId, " +
            "proj.tpn as projectTpn, " +
            "p.pin as planIdentificationNumber, " +
            "pw.name as phenotypingWorkUnit, " +
            "wg.name as phenotypingWorkGroup, " +
            "w.name as cohortProductionWorkUnit, " +
            "ps_status.name as phenotypingStageStatus, " +
            "o.id as outcomeId " +
            "FROM " +
            "PhenotypingAttempt pa " +
            "INNER JOIN Plan p ON pa.plan = p " +
            "INNER JOIN Project proj ON p.project = proj " +
            "INNER JOIN Privacy priv ON proj.privacy = priv " +
            "INNER JOIN PlanType pt ON p.planType = pt " +
            "INNER JOIN Status p_status ON p.status = p_status " +
            "INNER JOIN AttemptType at ON p.attemptType = at " +
            "INNER JOIN WorkUnit pw ON p.workUnit = pw " +
            "INNER JOIN WorkGroup wg ON p.workGroup = wg " +
            "LEFT OUTER JOIN WorkUnit w ON pa.cohortWorkUnit=w " +
            "LEFT OUTER JOIN PlanStartingPoint psp ON p=psp.plan " +
            "INNER JOIN Outcome o ON psp.outcome = o " +
            "INNER JOIN OutcomeType ot ON o.outcomeType = ot " +
            "INNER JOIN PhenotypingStage ps ON ps.phenotypingAttempt = pa " +
            "INNER JOIN PhenotypingStageType pst ON ps.phenotypingStageType = pst " +
            "INNER JOIN Status ps_status ON ps.status = ps_status " +
            "WHERE " +
            "priv.name='public' and pt.name='phenotyping' and " +
            "at.name='adult and embryo phenotyping' and " +
            "ot.name='Colony' and " +
            "pst.name='early adult and embryo' and " +
            "ps_status.name <> 'Phenotype Production Aborted' and " +
            "p_status.name <> 'Phenotyping Plan Aborted' " +
            "GROUP BY " +
            "proj.id, proj.tpn, p.pin, pw.name, wg.name, w.name, ps_status.name, o.id " +
            "HAVING " +
            "count(psp.id)=1")
    List<GeneInterestReportPhenotypingAttemptProjection> findAllGeneInterestReportPhenotypingAttemptProjections();
}
