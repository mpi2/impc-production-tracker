package org.gentar.report.collection.mgi_crispr_allele.colony;

import org.gentar.biology.colony.Colony;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface MgiCrisprAlleleReportColonyRepository extends CrudRepository<Colony, Long> {

    @Query("select c.name as colonyName, s.name as strainName, pw.name as productionWorkUnit, o.id as outcomeId  " +
            "from " +
            "Colony c " +
            "INNER JOIN Strain s ON c.strain = s " +
            "INNER JOIN Status c_status ON c.status = c_status " +
            "INNER JOIN Outcome o ON c.outcome = o " +
            "INNER JOIN Plan p ON o.plan = p " +
            "INNER JOIN Project proj ON p.project = proj " +
            "INNER JOIN Privacy priv ON proj.privacy = priv " +
            "INNER JOIN PlanType pt ON p.planType = pt " +
            "INNER JOIN Status p_status ON p.status = p_status  " +
            "INNER JOIN AttemptType at ON p.attemptType = at " +
            "INNER JOIN WorkUnit pw ON p.workUnit = pw " +
            "INNER JOIN WorkGroup wg ON p.workGroup = wg " +
            "where " +
            "priv.name='public' and " +
            "pt.name='production' and " +
            "at.name='crispr' and " +
            "c_status.name <> 'Colony Aborted' and " +
            "p_status.name <> 'Plan Abandoned'")
    List<MgiCrisprAlleleReportColonyProjection> findAllColonyReportProjections();

}
