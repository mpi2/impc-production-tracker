package org.gentar.report.collection.mgi_crispr_allele.genotype_primer;

import org.springframework.beans.factory.annotation.Value;

public interface MgiCrisprAlleleReportGenotypePrimerProjection {

    @Value("#{target.genotypePrimerId}")
    Long getGenotypePrimerId();

    @Value("#{target.planId}")
    Long getPlanId();

    @Value("#{target.name}")
    String getName();

    @Value("#{target.sequence}")
    String getSequence();

}
