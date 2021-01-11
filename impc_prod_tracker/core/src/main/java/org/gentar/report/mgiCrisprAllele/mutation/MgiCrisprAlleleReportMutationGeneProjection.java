package org.gentar.report.mgiCrisprAllele.mutation;

import org.gentar.biology.gene.Gene;
import org.springframework.beans.factory.annotation.Value;

public interface MgiCrisprAlleleReportMutationGeneProjection {

    @Value("#{target.mutationId}")
    Long getMutationId();

    @Value("#{target.geneId}")
    Long getGeneId();

    @Value("#{target.gene}")
    Gene getGene();
}
