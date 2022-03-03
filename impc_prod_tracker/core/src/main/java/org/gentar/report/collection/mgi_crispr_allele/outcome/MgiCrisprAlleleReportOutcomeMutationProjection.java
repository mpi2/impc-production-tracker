package org.gentar.report.collection.mgi_crispr_allele.outcome;

import org.springframework.beans.factory.annotation.Value;

public interface MgiCrisprAlleleReportOutcomeMutationProjection {

    @Value("#{target.outcomeId}")
    Long getOutcomeId();

    @Value("#{target.mutationId}")
    Long getMutationId();

    // Note: retrieving the entire mutation as part of the projection is slow
    @Value("#{target.symbol}")
    String getSymbol();

    @Value("#{target.description}")
    String getDescription();

    @Value("#{target.mgiAlleleAccId}")
    String getMgiAlleleAccId();

    @Value("#{target.alleleConfirmed}")
    Boolean getAlleleConfirmed();

    @Value("#{target.mutationType}")
    String getMutationType();

    @Value("#{target.mutationCategory}")
    String getMutationCategory();

}
