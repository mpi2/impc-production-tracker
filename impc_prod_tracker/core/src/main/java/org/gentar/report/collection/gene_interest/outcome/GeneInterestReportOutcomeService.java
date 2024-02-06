package org.gentar.report.collection.gene_interest.outcome;

import java.util.List;

public interface GeneInterestReportOutcomeService {

    /**
     *
     * @return a list of GeneInterestReportOutcomeMutationProjection Spring database projections containing
     *         an Outcome Id and the associated Mutation Id and Mutation Symbol
     */
    List<GeneInterestReportOutcomeMutationProjection> getSelectedOutcomeMutationProjections(List<Long> outcomeIds );
}
