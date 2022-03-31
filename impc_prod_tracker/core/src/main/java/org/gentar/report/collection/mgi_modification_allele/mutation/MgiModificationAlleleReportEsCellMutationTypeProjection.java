package org.gentar.report.collection.mgi_modification_allele.mutation;

import org.springframework.beans.factory.annotation.Value;

public interface MgiModificationAlleleReportEsCellMutationTypeProjection {

    @Value("#{target.mutationId}")
    Long getMutationId();

    @Value("#{target.mutationIdentificationNumber}")
    String getMutationIdentificationNumber();

    @Value("#{target.mutationCategorizationName}")
    String getMutationCategorizationName();
}
