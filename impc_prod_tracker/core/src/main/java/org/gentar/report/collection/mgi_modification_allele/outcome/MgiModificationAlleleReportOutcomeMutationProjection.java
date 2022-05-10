package org.gentar.report.collection.mgi_modification_allele.outcome;

import org.springframework.beans.factory.annotation.Value;

public interface MgiModificationAlleleReportOutcomeMutationProjection {

    @Value("#{target.outcomeId}")
    Long getOutcomeId();

    @Value("#{target.mutationId}")
    Long getMutationId();

    // Note: retrieving the entire mutation as part of the projection is slow
    @Value("#{target.symbol}")
    String getSymbol();

    @Value("#{target.mgiAlleleAccId}")
    String getMgiAlleleAccId();
}
