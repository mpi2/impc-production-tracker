package org.gentar.report.collection.mgi_modification_allele.modification_colony;

import org.gentar.biology.colony.Colony;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface MgiModificationAlleleReportColonyRepository extends CrudRepository<Colony, Long> {
    @Query("select " +
            "es_prod_plan.id as productionPlanId, " +
            "es_c.name as productionColonyName, " +
            "es_s.name as productionStrainName, " +
            "es_prod_w.name as productionWorkUnit, " +
            "es_o.id as productionOutcomeId, " +
            "targ_rep_es_cell.name as esCellName, " +
            "targ_rep_es_cell.parentalCellLine as parentalEsCellName, " +
            "targ_rep_es_cell_mutation.mgiAlleleSymbolSuperscript as esCellAlleleSymbol, " +
            "targ_rep_es_cell_mutation.mgiAlleleAccessionId as esCellAlleleAccessionId, " +
            "mod_attempt.tatCre as tatCre, " +
            "del_s.name as deleterStrainName, " +
            "c.name as modificationColonyName, " +
            "s.name as modificationStrainName, " +
            "prod_plan.id as modificationPlanId, " +
            "prod_w.name as modificationWorkUnit, " +
            "o.id as modificationOutcomeId " +
            "from " +
            "Colony c " +
            "INNER JOIN Outcome o ON c.outcome = o " +
            "INNER JOIN Strain s ON c.strain = s " +
            "INNER JOIN OutcomeType ot ON o.outcomeType = ot " +
            "INNER JOIN Plan prod_plan ON o.plan = prod_plan " +
            "INNER JOIN EsCellAlleleModificationAttempt mod_attempt ON mod_attempt.plan = prod_plan " +
            "LEFT JOIN Strain del_s ON mod_attempt.deleterStrain = del_s " +
            "INNER JOIN Project prod_proj ON prod_plan.project = prod_proj " +
            "INNER JOIN Privacy prod_priv ON prod_proj.privacy = prod_priv " +
            "INNER JOIN PlanType prod_pt ON prod_plan.planType = prod_pt " +
            "INNER JOIN Status prod_status ON prod_plan.status = prod_status " +
            "INNER JOIN AttemptType prod_at ON prod_plan.attemptType = prod_at " +
            "INNER JOIN WorkUnit prod_w ON prod_plan.workUnit = prod_w " +
            "INNER JOIN WorkGroup prod_wg ON prod_plan.workGroup = prod_wg " +
            "LEFT OUTER JOIN PlanStartingPoint mod_psp ON prod_plan=mod_psp.plan " +
            "INNER JOIN Outcome es_o ON mod_psp.outcome = es_o " +
            "INNER JOIN Colony es_c ON es_c.outcome = es_o " +
            "INNER JOIN Strain es_s ON es_c.strain = es_s " +
            "INNER JOIN OutcomeType es_ot ON es_o.outcomeType = es_ot " +
            "INNER JOIN Plan es_prod_plan ON es_o.plan = es_prod_plan " +
            "INNER JOIN Project es_prod_proj ON es_prod_plan.project = es_prod_proj " +
            "INNER JOIN Privacy es_prod_priv ON es_prod_proj.privacy = es_prod_priv " +
            "INNER JOIN PlanType es_prod_pt ON es_prod_plan.planType = es_prod_pt " +
            "INNER JOIN Status es_prod_status ON es_prod_plan.status = es_prod_status " +
            "INNER JOIN AttemptType es_prod_at ON es_prod_plan.attemptType = es_prod_at " +
            "INNER JOIN EsCellAttempt es_prod_es_attempt on es_prod_plan=es_prod_es_attempt.plan " +
            "INNER JOIN TargRepEsCell targ_rep_es_cell on es_prod_es_attempt.targRepEsCellId = targ_rep_es_cell.id " +
            "LEFT JOIN TargRepEsCellMutation targ_rep_es_cell_mutation on targ_rep_es_cell_mutation.esCell = targ_rep_es_cell " +
            "INNER JOIN WorkUnit es_prod_w ON es_prod_plan.workUnit = es_prod_w " +
            "INNER JOIN WorkGroup es_prod_wg ON es_prod_plan.workGroup = es_prod_wg " +
            "where " +
            "ot.name='Colony' and " +
            "prod_priv.name='public' and " +
            "prod_pt.name='production' and " +
            "prod_at.name='es cell allele modification' and " +
            "prod_status.name <> 'Plan Abandoned' and " +
            "es_prod_priv.name='public' and " +
            "es_prod_pt.name='production' and " +
            "es_prod_at.name='es cell' and " +
            "es_prod_wg.name <> 'EUCOMMToolsCre' and " +
            "es_prod_proj.esCellQcOnly = false " +
            "group by " +
            "es_prod_plan.id, " +
            "es_c.name, " +
            "es_s.name, " +
            "es_prod_w.name, " +
            "es_o.id, " +
            "targ_rep_es_cell.name, " +
            "targ_rep_es_cell.parentalCellLine, " +
            "targ_rep_es_cell_mutation.mgiAlleleSymbolSuperscript, " +
            "targ_rep_es_cell_mutation.mgiAlleleAccessionId, " +
            "mod_attempt.tatCre, " +
            "del_s.name, " +
            "c.name, " +
            "s.name, " +
            "prod_plan.id, " +
            "prod_w.name, " +
            "o.id " +
            "having " +
            "count(mod_psp.id)=1 ")
    List<MgiModificationAlleleReportColonyProjection> findMgiModificationAlleleReportProjections();
}
