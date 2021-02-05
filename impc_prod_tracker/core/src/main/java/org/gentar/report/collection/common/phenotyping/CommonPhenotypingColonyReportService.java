package org.gentar.report.collection.common.phenotyping;

import org.gentar.biology.gene.Gene;
import org.gentar.report.collection.common.phenotyping.outcome.CommonPhenotypingColonyReportOutcomeMutationProjection;
import org.gentar.report.collection.common.phenotyping.phenotyping_attempt.CommonPhenotypingColonyReportPhenotypingAttemptProjection;

import java.util.List;
import java.util.Map;

public interface CommonPhenotypingColonyReportService {
    /**
     *
     * @return Map of OutcomeId to CommonPhenotypingColonyReportOutcomeMutationProjection
     *         where an outcome maps to a single Mutation
     */
    Map<Long, CommonPhenotypingColonyReportOutcomeMutationProjection> getMutationMap();

    /**
     *
     * @return Map of MutationId to CommonPhenotypingColonyReportMutationGeneProjection
     *         where a mutationId maps to a single Gene.
     */
    Map<Long, Gene> getGeneMap();

    /**
     *
     * @return a list of CommonPhenotypingColonyReportPhenotypingAttemptProjection Spring database projections
     *         (This includes the associated outcome id found in the plan starting point of the phenotyping plan).
     */
    List<CommonPhenotypingColonyReportPhenotypingAttemptProjection> getPhenotypingColonyReportPhenotypingAttemptProjections();

}
