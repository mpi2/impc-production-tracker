package org.gentar.report.collection.mgi_modification_allele.outcome;

import java.util.List;

public interface MgiModificationAlleleReportOutcomeService {
    /**
     *
     * @return a list of MgiModificationAlleleReportOutcomeMutationProjection Spring database projections containing
     *         an Outcome Id and the associated Mutation Id and Mutation Symbol
     */
    List<MgiModificationAlleleReportOutcomeMutationProjection> getSelectedOutcomeMutationProjections(List<Long> outcomeIds );
}
