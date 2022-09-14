package org.gentar.report.collection.phenotyping_colony.phenotyping.phenotyping_attempt;

import java.util.List;

public interface PhenotypingColonyReportPhenotypingAttemptService {

    /**
     *
     * @return a list of PhenotypingColonyReportPhenotypingAttemptProjection Spring database projections
     *
     */
    List<PhenotypingColonyReportPhenotypingAttemptProjection> getPhenotypingAttemptProjections();
}
