package org.gentar.report.collection.mgi_modification_allele.modification_colony;

import org.gentar.biology.gene.Gene;
import org.gentar.report.collection.mgi_modification_allele.outcome.MgiModificationAlleleReportOutcomeMutationProjection;

import java.util.List;
import java.util.Map;

public interface MgiModificationAlleleReportColonyService {

    /**
     *
     * @return a list of MgiModificationAlleleReportColonyProjection Spring database projections
     */
    List<MgiModificationAlleleReportColonyProjection> getAllMgiModificationAlleleReportColonyProjections();


    /**
     *
     * @return Map of Modification OutcomeId to MgiModificationAlleleReportOutcomeMutationProjection
     *         where an outcomeId maps to a single Mutation.
     */
    Map<Long, MgiModificationAlleleReportOutcomeMutationProjection> getMutationMap();


    /**
     *
     * @return Map of Production OutcomeId to MgiModificationAlleleReportOutcomeMutationProjection
     *         where an outcomeId maps to a single Mutation.
     */
    Map<Long, MgiModificationAlleleReportOutcomeMutationProjection> getProductionMutationMap();


    /**
     *
     * @return Map of MutationId to Gene
     *         where a mutationId maps to a single Gene.
     */
    Map<Long, Gene> getGeneMap();



    /**
     *
     * @return Map of MutationId to Allele Category
     *         where a mutationId maps to a single Category.
     */
    Map<Long, String> getAlleleCategoryMap();
}
