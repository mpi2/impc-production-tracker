package org.gentar.report.collection.mgi_crispr_allele.guide;

import org.gentar.biology.plan.attempt.crispr.guide.Guide;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface MgiCrisprAlleleReportGuideRepository extends CrudRepository<Guide, Long> {

    @Query("select " +
            "g.id as guideId, g.crisprAttempt.id as planId, g.sequence as sequence, g.guideSequence as guideSequence, g.pam as pam, g.chr as chr, g.start as start, g.stop as stop, g.strand as strand, g.genomeBuild as genomeBuild " +
            "from " +
            "Guide g " +
            "where " +
            "g.crisprAttempt.id IN :id")
    List<MgiCrisprAlleleReportGuideProjection> findSelectedGuideProjections(@Param("id") List planIds );

}
