package org.gentar.report.collection.common.phenotyping.phenotyping_attempt;

import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttempt;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface CommonPhenotypingColonyReportPhenotypingAttemptRepository extends CrudRepository<PhenotypingAttempt, Long> {

    @Query("select " +
            "pa.phenotypingExternalRef as colonyName, " +
            "s.name as strainName, " +
            "s.mgiStrainAccId as strainAccId, " +
            "'' as esCellName, " +
            "prod_plan.id as productionPlanId, " +
            "prod_w.name as productionWorkUnit, " +
            "prod_wg.name as productionWorkGroup, " +
            "pw.name as phenotypingWorkUnit, " +
            "wg.name as phenotypingWorkGroup, " +
            "w.name as cohortProductionWorkUnit, " +
            "o.id as outcomeId " +
            "from " +
            "PhenotypingAttempt pa " +
            "INNER JOIN Strain s ON pa.strain = s " +
            "INNER JOIN Plan p ON pa.plan = p " +
            "INNER JOIN Project proj ON p.project = proj " +
            "INNER JOIN Privacy priv ON proj.privacy = priv " +
            "INNER JOIN PlanType pt ON p.planType = pt " +
            "INNER JOIN Status p_status ON p.status = p_status  " +
            "INNER JOIN AttemptType at ON p.attemptType = at " +
            "INNER JOIN WorkUnit pw ON p.workUnit = pw " +
            "INNER JOIN WorkGroup wg ON p.workGroup = wg " +
            "LEFT OUTER JOIN WorkUnit w ON pa.cohortWorkUnit=w " +
            "LEFT OUTER JOIN PlanStartingPoint psp ON p=psp.plan " +
            "INNER JOIN Outcome o ON psp.outcome = o " +
            "INNER JOIN OutcomeType ot ON o.outcomeType = ot " +
            "INNER JOIN Plan prod_plan ON o.plan = prod_plan " +
            "INNER JOIN Project prod_proj ON prod_plan.project = prod_proj " +
            "INNER JOIN Privacy prod_priv ON prod_proj.privacy = prod_priv " +
            "INNER JOIN PlanType prod_pt ON prod_plan.planType = prod_pt " +
            "INNER JOIN Status prod_status ON prod_plan.status = prod_status " +
            "INNER JOIN AttemptType prod_at ON prod_plan.attemptType = prod_at " +
            "INNER JOIN WorkUnit prod_w ON prod_plan.workUnit = prod_w " +
            "INNER JOIN WorkGroup prod_wg ON prod_plan.workGroup = prod_wg " +
            "INNER JOIN PhenotypingStage ps ON ps.phenotypingAttempt = pa " +
            "INNER JOIN PhenotypingStageType pst ON ps.phenotypingStageType = pst " +
            "INNER JOIN Status ps_status ON ps.status = ps_status " +
            "where " +
            "priv.name='public' and " +
            "pt.name='phenotyping' and " +
            "at.name='adult and embryo phenotyping' and " +
            "ot.name='Colony' and " +
            "prod_priv.name='public' and " +
            "prod_pt.name='production' and " +
            "prod_at.name='crispr' and " +
            "pst.name='early adult and embryo' and " +
            "ps_status.name <> 'Phenotype Production Aborted' and " +
            "p_status.name <> 'Phenotyping Plan Aborted' and prod_status.name <> 'Plan Abandoned' " +
            "group by " +
            "pa.phenotypingExternalRef, " +
            "s.name, " +
            "s.mgiStrainAccId, " +
            "prod_plan.id, " +
            "prod_w.name, " +
            "prod_wg.name, " +
            "pw.name, " +
            "wg.name, " +
            "w.name, " +
            "o.id " +
            "having " +
            "count(psp.id)=1")
    List<CommonPhenotypingColonyReportPhenotypingAttemptProjection> findAllPhenotypingAttemptProjections();

    @Query("select " +
            "pa.phenotypingExternalRef as colonyName, " +
            "s.name as strainName, " +
            "s.mgiStrainAccId as strainAccId, " +
            "'' as esCellName, " +
            "prod_plan.id as productionPlanId, " +
            "prod_w.name as productionWorkUnit, " +
            "prod_wg.name as productionWorkGroup, " +
            "pw.name as phenotypingWorkUnit, " +
            "wg.name as phenotypingWorkGroup, " +
            "w.name as cohortProductionWorkUnit, " +
            "o.id as outcomeId " +
            "from " +
            "PhenotypingAttempt pa " +
            "INNER JOIN Strain s ON pa.strain = s " +
            "INNER JOIN Plan p ON pa.plan = p " +
            "INNER JOIN Project proj ON p.project = proj " +
            "INNER JOIN Privacy priv ON proj.privacy = priv " +
            "INNER JOIN PlanType pt ON p.planType = pt " +
            "INNER JOIN Status p_status ON p.status = p_status  " +
            "INNER JOIN AttemptType at ON p.attemptType = at " +
            "INNER JOIN WorkUnit pw ON p.workUnit = pw " +
            "INNER JOIN WorkGroup wg ON p.workGroup = wg " +
            "LEFT OUTER JOIN WorkUnit w ON pa.cohortWorkUnit=w " +
            "LEFT OUTER JOIN PlanStartingPoint psp ON p=psp.plan " +
            "INNER JOIN Outcome o ON psp.outcome = o " +
            "INNER JOIN OutcomeType ot ON o.outcomeType = ot " +
            "INNER JOIN Plan prod_plan ON o.plan = prod_plan " +
            "INNER JOIN CrisprAttempt prod_crispr_attempt on prod_plan=prod_crispr_attempt.plan " +
            "INNER JOIN Project prod_proj ON prod_plan.project = prod_proj " +
            "INNER JOIN Privacy prod_priv ON prod_proj.privacy = prod_priv " +
            "INNER JOIN PlanType prod_pt ON prod_plan.planType = prod_pt " +
            "INNER JOIN Status prod_status ON prod_plan.status = prod_status " +
            "INNER JOIN AttemptType prod_at ON prod_plan.attemptType = prod_at " +
            "INNER JOIN WorkUnit prod_w ON prod_plan.workUnit = prod_w " +
            "INNER JOIN WorkGroup prod_wg ON prod_plan.workGroup = prod_wg " +
            "INNER JOIN PhenotypingStage ps ON ps.phenotypingAttempt = pa " +
            "INNER JOIN PhenotypingStageType pst ON ps.phenotypingStageType = pst " +
            "INNER JOIN Status ps_status ON ps.status = ps_status " +
            "where " +
            "priv.name='public' and " +
            "pt.name='phenotyping' and " +
            "at.name='adult and embryo phenotyping' and " +
            "ot.name='Colony' and " +
            "prod_priv.name='public' and " +
            "prod_pt.name='production' and " +
            "prod_at.name='crispr' and " + // Ensure no haplo-essential production plans included (production plans are implicit)
            "prod_crispr_attempt.experimental=false and " +    // Critera used in iMits to exclude some data
            "pst.name='early adult and embryo' and " +
            "ps_status.name <> 'Phenotype Production Aborted' and " +
            "p_status.name <> 'Phenotyping Plan Aborted' and prod_status.name <> 'Plan Abandoned' " +
            "group by " +
            "pa.phenotypingExternalRef, " +
            "s.name, " +
            "s.mgiStrainAccId, " +
            "prod_plan.id, " +
            "prod_w.name, " +
            "prod_wg.name, " +
            "pw.name, " +
            "wg.name, " +
            "w.name, " +
            "o.id " +
            "having " +
            "count(psp.id)=1")
    List<CommonPhenotypingColonyReportPhenotypingAttemptProjection> findPhenotypingAttemptProjectionsFromCrisprProduction();



    @Query("select " +
            "pa.phenotypingExternalRef as colonyName, " +
            "s.name as strainName, " +
            "s.mgiStrainAccId as strainAccId, " +
            "targ_rep_es_cell.name as esCellName, " +
            "prod_w.name as productionWorkUnit, " +
            "prod_wg.name as productionWorkGroup, " +
            "pw.name as phenotypingWorkUnit, " +
            "wg.name as phenotypingWorkGroup, " +
            "w.name as cohortProductionWorkUnit, " +
            "o.id as outcomeId " +
            "from " +
            "PhenotypingAttempt pa " +
            "INNER JOIN Strain s ON pa.strain = s " +
            "INNER JOIN Plan p ON pa.plan = p " +
            "INNER JOIN Project proj ON p.project = proj " +
            "INNER JOIN Privacy priv ON proj.privacy = priv " +
            "INNER JOIN PlanType pt ON p.planType = pt " +
            "INNER JOIN Status p_status ON p.status = p_status  " +
            "INNER JOIN AttemptType at ON p.attemptType = at " +
            "INNER JOIN WorkUnit pw ON p.workUnit = pw " +
            "INNER JOIN WorkGroup wg ON p.workGroup = wg " +
            "LEFT OUTER JOIN WorkUnit w ON pa.cohortWorkUnit=w " +
            "LEFT OUTER JOIN PlanStartingPoint psp ON p=psp.plan " +
            "INNER JOIN Outcome o ON psp.outcome = o " +
            "INNER JOIN OutcomeType ot ON o.outcomeType = ot " +
            "INNER JOIN Plan prod_plan ON o.plan = prod_plan " +
            "INNER JOIN EsCellAttempt prod_es_attempt on prod_plan=prod_es_attempt.plan " +
            "INNER JOIN TargRepEsCell targ_rep_es_cell on prod_es_attempt.TargRepEsCellId = targ_rep_es_cell.id " +
            "INNER JOIN Project prod_proj ON prod_plan.project = prod_proj " +
            "INNER JOIN Privacy prod_priv ON prod_proj.privacy = prod_priv " +
            "INNER JOIN PlanType prod_pt ON prod_plan.planType = prod_pt " +
            "INNER JOIN Status prod_status ON prod_plan.status = prod_status " +
            "INNER JOIN AttemptType prod_at ON prod_plan.attemptType = prod_at " +
            "INNER JOIN WorkUnit prod_w ON prod_plan.workUnit = prod_w " +
            "INNER JOIN WorkGroup prod_wg ON prod_plan.workGroup = prod_wg " +
            "INNER JOIN PhenotypingStage ps ON ps.phenotypingAttempt = pa " +
            "INNER JOIN PhenotypingStageType pst ON ps.phenotypingStageType = pst " +
            "INNER JOIN Status ps_status ON ps.status = ps_status " +
            "where " +
            "priv.name='public' and " +
            "pt.name='phenotyping' and " +
            "at.name='adult and embryo phenotyping' and " +
            "ot.name='Colony' and " +
            "prod_priv.name='public' and " +
            "prod_pt.name='production' and " +
            "prod_at.name='es cell' and " +
            "prod_es_attempt.experimental = false  and " +    // Critera used in iMits to exclude some data
            "prod_wg.name <> 'EUCOMMToolsCre' and " +  // Critera used in iMits to exclude some data
            "prod_proj.esCellQcOnly = false and " +       // Critera used in iMits to exclude some data
            "pst.name='early adult and embryo' and " +
            "ps_status.name <> 'Phenotype Production Aborted' and " +
            "p_status.name <> 'Phenotyping Plan Aborted' and prod_status.name <> 'Plan Abandoned' " +
            "group by " +
            "pa.phenotypingExternalRef, " +
            "s.name, " +
            "s.mgiStrainAccId, " +
            "targ_rep_es_cell.name, " +
            "prod_w.name, " +
            "prod_wg.name, " +
            "pw.name, " +
            "wg.name, " +
            "w.name, " +
            "o.id " +
            "having " +
            "count(psp.id)=1")
    List<CommonPhenotypingColonyReportPhenotypingAttemptProjection> findPhenotypingAttemptProjectionsFromEsCellProduction();


    @Query("select " +
            "pa.phenotypingExternalRef as colonyName, " +
            "s.name as strainName, " +
            "s.mgiStrainAccId as strainAccId, " +
            "targ_rep_es_cell.name as esCellName, " +
            "prod_plan.id as productionPlanId, " +
            "prod_w.name as productionWorkUnit, " +
            "prod_wg.name as productionWorkGroup, " +
            "pw.name as phenotypingWorkUnit, " +
            "wg.name as phenotypingWorkGroup, " +
            "w.name as cohortProductionWorkUnit, " +
            "o.id as outcomeId " +
            "from " +
            "PhenotypingAttempt pa " +
            "INNER JOIN Strain s ON pa.strain = s " +
            "INNER JOIN Plan p ON pa.plan = p " +
            "INNER JOIN Project proj ON p.project = proj " +
            "INNER JOIN Privacy priv ON proj.privacy = priv " +
            "INNER JOIN PlanType pt ON p.planType = pt " +
            "INNER JOIN Status p_status ON p.status = p_status  " +
            "INNER JOIN AttemptType at ON p.attemptType = at " +
            "INNER JOIN WorkUnit pw ON p.workUnit = pw " +
            "INNER JOIN WorkGroup wg ON p.workGroup = wg " +
            "LEFT OUTER JOIN WorkUnit w ON pa.cohortWorkUnit = w " +
            "LEFT OUTER JOIN PlanStartingPoint psp ON p=psp.plan " +
            "INNER JOIN Outcome o ON psp.outcome = o " +
            "INNER JOIN OutcomeType ot ON o.outcomeType = ot " +
            "INNER JOIN Plan prod_plan ON o.plan = prod_plan " +
            "INNER JOIN Project prod_proj ON prod_plan.project = prod_proj " +
            "INNER JOIN Privacy prod_priv ON prod_proj.privacy = prod_priv " +
            "INNER JOIN PlanType prod_pt ON prod_plan.planType = prod_pt " +
            "INNER JOIN Status prod_status ON prod_plan.status = prod_status " +
            "INNER JOIN AttemptType prod_at ON prod_plan.attemptType = prod_at " +
            "INNER JOIN WorkUnit prod_w ON prod_plan.workUnit = prod_w " +
            "INNER JOIN WorkGroup prod_wg ON prod_plan.workGroup = prod_wg " +
            "INNER JOIN PhenotypingStage ps ON ps.phenotypingAttempt = pa " +
            "INNER JOIN PhenotypingStageType pst ON ps.phenotypingStageType = pst " +
            "INNER JOIN Status ps_status ON ps.status = ps_status " +
            "LEFT OUTER JOIN PlanStartingPoint mod_psp ON prod_plan=mod_psp.plan " +
            "INNER JOIN Outcome es_o ON mod_psp.outcome = es_o " +
            "INNER JOIN OutcomeType es_ot ON es_o.outcomeType = es_ot " +
            "INNER JOIN Plan es_prod_plan ON es_o.plan = es_prod_plan " +
            "INNER JOIN Project es_prod_proj ON es_prod_plan.project = es_prod_proj " +
            "INNER JOIN Privacy es_prod_priv ON es_prod_proj.privacy = es_prod_priv " +
            "INNER JOIN PlanType es_prod_pt ON es_prod_plan.planType = es_prod_pt " +
            "INNER JOIN Status es_prod_status ON es_prod_plan.status = es_prod_status " +
            "INNER JOIN AttemptType es_prod_at ON es_prod_plan.attemptType = es_prod_at " +
            "INNER JOIN EsCellAttempt es_prod_es_attempt on es_prod_plan=es_prod_es_attempt.plan " +
            "INNER JOIN TargRepEsCell targ_rep_es_cell on es_prod_es_attempt.TargRepEsCellId = targ_rep_es_cell.id " +
            "INNER JOIN WorkUnit es_prod_w ON es_prod_plan.workUnit = es_prod_w " +
            "INNER JOIN WorkGroup es_prod_wg ON es_prod_plan.workGroup = es_prod_wg " +
            "where " +
            "priv.name='public' and " +
            "pt.name='phenotyping' and " +
            "at.name='adult and embryo phenotyping' and " +
            "ot.name='Colony' and " +
            "prod_priv.name='public' and " +
            "prod_pt.name='production' and " +
            "prod_at.name='cre allele modification' and " +
            "pst.name='early adult and embryo' and " +
            "ps_status.name <> 'Phenotype Production Aborted' and " +
            "p_status.name <> 'Phenotyping Plan Aborted' and " +
            "prod_status.name <> 'Plan Abandoned' and " +
            "es_prod_priv.name='public' and " +
            "es_prod_pt.name='production' and " +
            "es_prod_at.name='es cell' and " +
            "es_prod_es_attempt.experimental = false  and " +
            "es_prod_wg.name <> 'EUCOMMToolsCre' and " +
            "es_prod_proj.esCellQcOnly = false " +
            "group by " +
            "pa.phenotypingExternalRef, " +
            "s.name, " +
            "s.mgiStrainAccId, " +
            "targ_rep_es_cell.name, " +
            "prod_plan.id, " +
            "prod_w.name, " +
            "prod_wg.name, " +
            "pw.name, " +
            "wg.name, " +
            "w.name, " +
            "o.id " +
            "having " +
            "count(psp.id)=1 ")
    List<CommonPhenotypingColonyReportPhenotypingAttemptProjection> findPhenotypingAttemptProjectionsFromEsCellModificationProduction();

}
