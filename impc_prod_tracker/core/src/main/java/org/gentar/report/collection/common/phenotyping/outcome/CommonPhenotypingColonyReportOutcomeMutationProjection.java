package org.gentar.report.collection.common.phenotyping.outcome;

import org.springframework.beans.factory.annotation.Value;

public interface CommonPhenotypingColonyReportOutcomeMutationProjection {

    @Value("#{target.outcomeId}")
    Long getOutcomeId();

    @Value("#{target.mutationId}")
    Long getMutationId();

    // Note: retrieving the entire mutation as part of the projection is slow
    @Value("#{target.symbol}")
    String getSymbol();

}