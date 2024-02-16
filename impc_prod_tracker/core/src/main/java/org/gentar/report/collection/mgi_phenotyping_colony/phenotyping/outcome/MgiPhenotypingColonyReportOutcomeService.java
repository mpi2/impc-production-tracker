package org.gentar.report.collection.mgi_phenotyping_colony.phenotyping.outcome;

import java.util.List;

public interface MgiPhenotypingColonyReportOutcomeService {

    /**
     *
     * @return a list of MgiPhenotypingColonyReportOutcomeMutationProjection Spring database projections containing
     *         an Outcome Id and the associated Mutation Id and Mutation Symbol
     */
    List<MgiPhenotypingColonyReportOutcomeMutationProjection> getSelectedOutcomeMutationProjections(List<Long> outcomeIds );
}
