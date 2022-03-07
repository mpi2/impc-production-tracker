package org.gentar.report.collection.mgi_crispr_allele.mutagenesis_donor;

import org.gentar.biology.plan.attempt.crispr.mutagenesis_donor.MutagenesisDonor;
import org.gentar.report.collection.mgi_crispr_allele.guide.MgiCrisprAlleleReportGuideProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface MgiCrisprAlleleReportMutagenesisDonorRepository extends CrudRepository<MutagenesisDonor, Long> {

    @Query("select " +
            "m.id as mutagenesisDonorId, m.crisprAttempt.id as planId, m.oligoSequenceFasta as sequence, m.vectorName as vector, pt.name as preparationType " +
            "from " +
            "MutagenesisDonor m " +
            "INNER JOIN PreparationType pt ON m.preparationType = pt " +
            "where " +
            "m.crisprAttempt.id IN :id")
    List<MgiCrisprAlleleReportMutagenesisDonorProjection> findSelectedMutagenesisDonorProjections(@Param("id") List planIds );

}
