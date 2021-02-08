package org.gentar.report.collection.mgi_crispr_allele.colony;

import org.gentar.biology.gene.Gene;
import org.gentar.report.collection.mgi_crispr_allele.outcome.MgiCrisprAlleleReportOutcomeMutationProjection;

import java.util.List;
import java.util.Map;

public interface MgiCrisprAlleleReportColonyService {

    /**
     *
     * @return a list of MgiCrisprAlleleReportColonyProjection Spring database projections
     *  (this includes an outcomeId)
     */
    List<MgiCrisprAlleleReportColonyProjection> getAllColonyReportProjections();

    /**
     *
     * @return Map of OutcomeId to MgiCrisprAlleleReportOutcomeMutationProjection
     *         where an outcomeId maps to a single Mutation.
     */
    Map<Long, MgiCrisprAlleleReportOutcomeMutationProjection> getMutationMap();

    /**
     *
     * @return Map of MutationId to Gene
     *         where a mutationId maps to a single Gene.
     */
    Map<Long, Gene> getGeneMap();
}
