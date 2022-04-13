package org.gentar.report.collection.mgi_modification_allele.modification_colony;

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
     * @return Map of OutcomeId to MgiModificationAlleleReportOutcomeMutationProjection
     *         where an outcomeId maps to a single Mutation.
     */
    Map<Long, MgiModificationAlleleReportOutcomeMutationProjection> getMutationMap();
}
