package org.gentar.report.collection.mgi_crispr_allele.mutation_characterization;

import org.springframework.beans.factory.annotation.Value;

public interface MgiCrisprAlleleReportMutationCategorizationProjection {

    @Value("#{target.mutationId}")
    Long getMutationId();

    @Value("#{target.mutationCategorization}")
    String getMutationCategorization();

    @Value("#{target.mutationCategorizationType}")
    String getMutationCategorizationType();

}
