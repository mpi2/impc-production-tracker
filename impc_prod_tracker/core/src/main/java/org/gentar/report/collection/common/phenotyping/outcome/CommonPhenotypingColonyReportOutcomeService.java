package org.gentar.report.collection.common.phenotyping.outcome;

import java.util.List;

public interface CommonPhenotypingColonyReportOutcomeService {

    /**
     *
     * @param outcomeIds
     * @return a list of CommonPhenotypingColonyReportOutcomeMutationProjection Spring database projections containing
     *         an Outcome Id and the associated Mutation Id and Mutation Symbol
     */
    List<CommonPhenotypingColonyReportOutcomeMutationProjection> getSelectedOutcomeMutationProjections(List<Long> outcomeIds );
}
