package org.gentar.report.collection.mgi_phenotyping_colony.phenotyping.phenotyping_attempt;

import java.util.List;

public interface MgiPhenotypingColonyReportPhenotypingAttemptService {

    /**
     *
     * @return a list of MgiPhenotypingColonyReportPhenotypingAttemptProjection Spring database projections
     *
     */
    List<MgiPhenotypingColonyReportPhenotypingAttemptProjection> getPhenotypingAttemptProjections();
}
