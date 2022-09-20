package org.gentar.report.collection.phenotyping_colony.phenotyping.outcome;

import java.util.List;

public interface PhenotypingColonyReportOutcomeService {

    /**
     *
     * @param outcomeIds
     * @return a list of PhenotypingColonyReportOutcomeMutationProjection Spring database projections containing
     *         an Outcome Id and the associated Mutation Id and Mutation Symbol
     */
    List<PhenotypingColonyReportOutcomeMutationProjection> getSelectedOutcomeMutationProjections(List<Long> outcomeIds );
}
