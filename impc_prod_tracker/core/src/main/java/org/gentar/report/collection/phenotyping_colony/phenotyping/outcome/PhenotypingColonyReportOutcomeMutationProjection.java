package org.gentar.report.collection.phenotyping_colony.phenotyping.outcome;

import org.springframework.beans.factory.annotation.Value;

public interface PhenotypingColonyReportOutcomeMutationProjection {

    @Value("#{target.outcomeId}")
    Long getOutcomeId();

    @Value("#{target.mutationId}")
    Long getMutationId();

    // Note: retrieving the entire mutation as part of the projection is slow
    @Value("#{target.symbol}")
    String getSymbol();

}
