package org.gentar.report.collection.common.phenotyping.phenotyping_attempt;

import java.util.List;

public interface CommonPhenotypingColonyReportPhenotypingAttemptService {

    /**
     *
     * @return a list of CommonPhenotypingColonyReportPhenotypingAttemptProjection Spring database projections
     *
     */
    List<CommonPhenotypingColonyReportPhenotypingAttemptProjection> getPhenotypingAttemptProjections();
}
