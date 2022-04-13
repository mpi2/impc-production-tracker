package org.gentar.report.collection.mgi_crispr_allele.nuclease;

import org.springframework.beans.factory.annotation.Value;

public interface MgiCrisprAlleleReportNucleaseProjection {

    @Value("#{target.nucleaseId}")
    Long getNucleaseId();

    @Value("#{target.planId}")
    Long getPlanId();

    @Value("#{target.nucleaseType}")
    String getNucleaseType();

    @Value("#{target.nucleaseClass}")
    String getNucleaseClass();

}
