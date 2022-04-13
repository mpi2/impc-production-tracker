package org.gentar.report.collection.mgi_crispr_allele.genotype_primer;

import org.gentar.biology.plan.attempt.crispr.genotype_primer.GenotypePrimer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface MgiCrisprAlleleReportGenotypePrimerRepository extends CrudRepository<GenotypePrimer, Long> {

    @Query("select " +
            "g.id as genotypePrimerId, g.crisprAttempt.id as planId, g.name as name, g.sequence as sequence " +
            "from " +
            "GenotypePrimer g " +
            "where " +
            "g.crisprAttempt.id IN :id")
    List<MgiCrisprAlleleReportGenotypePrimerProjection> findSelectedGenotypePrimerProjections(@Param("id") List planIds );

}
