package org.gentar.report.collection.mgi_crispr_allele.sequence;

import org.springframework.beans.factory.annotation.Value;

public interface MgiCrisprAlleleReportMutationSequenceProjection {

    @Value("#{target.mutationId}")
    Long getMutationId();

    @Value("#{target.index}")
    Long getIndex();

    @Value("#{target.sequenceId}")
    Long getSequenceId();

    @Value("#{target.sequence}")
    String getSequence();

    @Value("#{target.sequenceType}")
    String getSequenceType();

    @Value("#{target.sequenceCategory}")
    String getSequenceCategory();

}
