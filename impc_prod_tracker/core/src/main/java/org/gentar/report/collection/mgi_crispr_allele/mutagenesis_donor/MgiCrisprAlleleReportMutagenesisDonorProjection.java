package org.gentar.report.collection.mgi_crispr_allele.mutagenesis_donor;

import org.springframework.beans.factory.annotation.Value;

public interface MgiCrisprAlleleReportMutagenesisDonorProjection {

    @Value("#{target.mutagenesisDonorId}")
    Long getMutagenesisDonorId();

    @Value("#{target.planId}")
    Long getPlanId();

    @Value("#{target.sequence}")
    String getSequence();

    @Value("#{target.vector}")
    String getVector();

    @Value("#{target.preparationType}")
    String getPreparationType();
}
