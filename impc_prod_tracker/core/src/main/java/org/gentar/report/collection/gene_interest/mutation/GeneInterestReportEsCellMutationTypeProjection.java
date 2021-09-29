package org.gentar.report.collection.gene_interest.mutation;

import org.springframework.beans.factory.annotation.Value;

public interface GeneInterestReportEsCellMutationTypeProjection {

    @Value("#{target.mutationId}")
    Long getMutationId();

    @Value("#{target.mutationIdentificationNumber}")
    String getMutationIdentificationNumber();

    @Value("#{target.mutationCategorizationName}")
    String getMutationCategorizationName();
}
