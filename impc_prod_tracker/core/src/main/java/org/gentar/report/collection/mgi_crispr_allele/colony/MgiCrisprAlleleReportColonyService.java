package org.gentar.report.collection.mgi_crispr_allele.colony;

import org.gentar.biology.gene.Gene;
import org.gentar.report.collection.mgi_crispr_allele.guide.MgiCrisprAlleleReportGuideProjection;
import org.gentar.report.collection.mgi_crispr_allele.nuclease.MgiCrisprAlleleReportNucleaseProjection;
import org.gentar.report.collection.mgi_crispr_allele.outcome.MgiCrisprAlleleReportOutcomeMutationProjection;
import org.gentar.report.collection.mgi_crispr_allele.sequence.MgiCrisprAlleleReportMutationSequenceProjection;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
     * @return Map of Production PlanId to a set of MgiCrisprAlleleReportGuideProjection
     *         the planId will often map to more than one Guide.
     */
    Map<Long, Set<MgiCrisprAlleleReportGuideProjection>> getGuideMap();

    /**
     *
     * @return Map of Production PlanId to a set of MgiCrisprAlleleReportGuideProjection
     *         the planId will often map to more than one Guide.
     */
    Map<Long, Set<MgiCrisprAlleleReportNucleaseProjection>> getNucleaseMap();

    /**
     *
     * @return Map of MutationId to Sequence data.
     */
    Map<Long, Set<MgiCrisprAlleleReportMutationSequenceProjection>> getMutationSequenceMap();

    /**
     *
     * @return Map of MutationId to Gene
     *         where a mutationId maps to a single Gene.
     */
    Map<Long, Gene> getGeneMap();
}
