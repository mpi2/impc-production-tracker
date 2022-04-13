package org.gentar.report.collection.mgi_crispr_allele.guide;

import org.springframework.beans.factory.annotation.Value;

public interface MgiCrisprAlleleReportGuideProjection {

    @Value("#{target.guideId}")
    Long getGuideId();

    @Value("#{target.planId}")
    Long getPlanId();

    @Value("#{target.sequence}")
    String getSequence();

    @Value("#{target.guideSequence}")
    String getGuideSequence();

    @Value("#{target.pam}")
    String getPam();

    @Value("#{target.chr}")
    String getChr();

    @Value("#{target.start}")
    Integer getStart();

    @Value("#{target.stop}")
    Integer getStop();

    @Value("#{target.strand}")
    String getStrand();

    @Value("#{target.genomeBuild}")
    String getGenomeBuild();

}
