package org.gentar.report.mgiPhenotypingColony.mutation;

import org.gentar.biology.gene.Gene;
import org.springframework.beans.factory.annotation.Value;

public interface MgiPhenotypingColonyReportMutationGeneProjection {

    @Value("#{target.mutationId}")
    Long getMutationId();

    @Value("#{target.geneId}")
    Long getGeneId();

    @Value("#{target.gene}")
    Gene getGene();
}
