package org.gentar.biology.plan.attempt.phenotyping;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PhenotypingAttemptRepository extends CrudRepository<PhenotypingAttempt, Long>
{

    @Query("select pa.phenotypingExternalRef as colonyName, s.name as strainName,s.mgiStrainAccId as strainAccId, prod_w.name as productionWorkUnit, prod_wg.name as productionWorkGroup, pw.name as phenotypingWorkUnit, wg.name as phenotypingWorkGroup, w.name as cohortProductionWorkUnit, o.id as outcomeId from PhenotypingAttempt pa INNER JOIN Strain s ON pa.strain = s INNER JOIN Plan p ON pa.plan = p INNER JOIN Project proj ON p.project = proj INNER JOIN Privacy priv ON proj.privacy = priv INNER JOIN PlanType pt ON p.planType = pt INNER JOIN Status p_status ON p.status = p_status  INNER JOIN AttemptType at ON p.attemptType = at INNER JOIN WorkUnit pw ON p.workUnit = pw INNER JOIN WorkGroup wg ON p.workGroup = wg LEFT OUTER JOIN WorkUnit w ON pa.cohortWorkUnit=w LEFT OUTER JOIN PlanStartingPoint psp ON p.id=psp.id INNER JOIN Outcome o ON psp.outcome = o INNER JOIN OutcomeType ot ON o.outcomeType = ot INNER JOIN Plan prod_plan ON o.plan = prod_plan INNER JOIN Project prod_proj ON prod_plan.project = prod_proj INNER JOIN Privacy prod_priv ON prod_proj.privacy = prod_priv INNER JOIN PlanType prod_pt ON prod_plan.planType = prod_pt INNER JOIN Status prod_status ON prod_plan.status = prod_status INNER JOIN AttemptType prod_at ON prod_plan.attemptType = prod_at INNER JOIN WorkUnit prod_w ON prod_plan.workUnit = prod_w INNER JOIN WorkGroup prod_wg ON prod_plan.workGroup = prod_wg INNER JOIN PhenotypingStage ps ON ps.phenotypingAttempt = pa INNER JOIN PhenotypingStageType pst ON ps.phenotypingStageType = pst INNER JOIN Status ps_status ON ps.status = ps_status \n" +
            "where priv.name='public' and pt.name='phenotyping' and at.name='adult and embryo phenotyping' and ot.name='Colony' and prod_priv.name='public' and prod_pt.name='production' and prod_at.name='crispr' and pst.name='early adult and embryo' and ps_status.name <> 'Phenotype Production Aborted' and p_status.name <> 'Phenotyping Plan Aborted' and prod_status.name <> 'Plan Abandoned' and p.pin='PIN:0000001586' \n" +
            "group by pa.phenotypingExternalRef, s.name,s.mgiStrainAccId, prod_w.name, prod_wg.name, pw.name, wg.name, w.name, o.id \n" +
            "having count(psp.id)=1")
    List<PhenotypingAttemptProjectionForMgi> findAllPhenotypingAttemptProjectionsForMgi();

}
