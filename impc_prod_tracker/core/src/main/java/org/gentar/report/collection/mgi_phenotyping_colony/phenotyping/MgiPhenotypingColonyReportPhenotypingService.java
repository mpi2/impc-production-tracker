package org.gentar.report.collection.mgi_phenotyping_colony.phenotyping;

import org.gentar.biology.gene.Gene;
import org.gentar.report.collection.mgi_phenotyping_colony.phenotyping.outcome.MgiPhenotypingColonyReportOutcomeMutationProjection;
import org.gentar.report.collection.mgi_phenotyping_colony.phenotyping.phenotyping_attempt.MgiPhenotypingColonyReportPhenotypingAttemptProjection;

import java.util.List;
import java.util.Map;

public interface MgiPhenotypingColonyReportPhenotypingService {
    /**
     *
     * @return Map of OutcomeId to MgiPhenotypingColonyReportOutcomeMutationProjection
     *         where an outcome maps to a single Mutation
     */
    Map<Long, MgiPhenotypingColonyReportOutcomeMutationProjection> getMutationMap();

    /**
     *
     * @return Map of MutationId to Gene
     *         where a mutationId maps to a single Gene.
     */
    Map<Long, Gene> getGeneMap();

    /**
     *
     * @return a list of MgiPhenotypingColonyReportPhenotypingAttemptProjection Spring database projections
     *         (This includes the associated outcome id found in the plan starting point of the phenotyping plan).
     */
    List<MgiPhenotypingColonyReportPhenotypingAttemptProjection> getPhenotypingColonyReportPhenotypingAttemptProjections();

}
