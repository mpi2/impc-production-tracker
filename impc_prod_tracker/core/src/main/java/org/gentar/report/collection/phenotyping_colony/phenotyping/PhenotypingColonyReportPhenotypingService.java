package org.gentar.report.collection.phenotyping_colony.phenotyping;

import org.gentar.biology.gene.Gene;
import org.gentar.report.collection.phenotyping_colony.phenotyping.outcome.PhenotypingColonyReportOutcomeMutationProjection;
import org.gentar.report.collection.phenotyping_colony.phenotyping.phenotyping_attempt.PhenotypingColonyReportPhenotypingAttemptProjection;

import java.util.List;
import java.util.Map;

public interface PhenotypingColonyReportPhenotypingService {
    /**
     *
     * @return Map of OutcomeId to PhenotypingColonyReportOutcomeMutationProjection
     *         where an outcome maps to a single Mutation
     */
    Map<Long, PhenotypingColonyReportOutcomeMutationProjection> getMutationMap();

    /**
     *
     * @return Map of MutationId to Gene
     *         where a mutationId maps to a single Gene.
     */
    Map<Long, Gene> getGeneMap();

    /**
     *
     * @return a list of PhenotypingColonyReportPhenotypingAttemptProjection Spring database projections
     *         (This includes the associated outcome id found in the plan starting point of the phenotyping plan).
     */
    List<PhenotypingColonyReportPhenotypingAttemptProjection> getPhenotypingColonyReportPhenotypingAttemptProjections();

}
