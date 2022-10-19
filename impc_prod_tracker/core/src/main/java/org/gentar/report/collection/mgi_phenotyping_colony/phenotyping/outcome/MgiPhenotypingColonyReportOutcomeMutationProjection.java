package org.gentar.report.collection.mgi_phenotyping_colony.phenotyping.outcome;

import org.springframework.beans.factory.annotation.Value;

public interface MgiPhenotypingColonyReportOutcomeMutationProjection {

    @Value("#{target.outcomeId}")
    Long getOutcomeId();

    @Value("#{target.mutationId}")
    Long getMutationId();

    @Value("#{target.mutationIdentificationNumber}")
    String getMutationIdentificationNumber();

    // Note: retrieving the entire mutation as part of the projection is slow
    @Value("#{target.symbol}")
    String getSymbol();
}
