package org.gentar.report.collection.mgi_crispr_allele.nuclease;

import org.gentar.biology.plan.attempt.crispr.nuclease.Nuclease;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface MgiCrisprAlleleReportNucleaseRepository extends CrudRepository<Nuclease, Long> {

    @Query("select " +
            "n.id as nucleaseId, n.crisprAttempt.id as planId, t.name as nucleaseType, c.name as nucleaseClass " +
            "from " +
            "Nuclease n LEFT OUTER JOIN n.nucleaseType t  LEFT OUTER JOIN n.nucleaseClass c  " +
            "where " +
            "n.crisprAttempt.id IN :id")
    List<MgiCrisprAlleleReportNucleaseProjection> findSelectedNucleaseProjections(@Param("id") List planIds );

}
