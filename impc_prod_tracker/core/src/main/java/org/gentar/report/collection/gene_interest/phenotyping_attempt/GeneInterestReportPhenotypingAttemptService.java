package org.gentar.report.collection.gene_interest.phenotyping_attempt;

import java.util.List;

public interface GeneInterestReportPhenotypingAttemptService {

    /**
     *
     * @return a list of GeneInterestReportPhenotypingAttemptProjection Spring database projections
     *
     */
    List<GeneInterestReportPhenotypingAttemptProjection> getPhenotypingAttemptProjections();
}
