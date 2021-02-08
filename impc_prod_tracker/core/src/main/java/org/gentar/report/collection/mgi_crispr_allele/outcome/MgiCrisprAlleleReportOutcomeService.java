package org.gentar.report.collection.mgi_crispr_allele.outcome;

import java.util.List;

public interface MgiCrisprAlleleReportOutcomeService {

    /**
     *
     * @param outcomeIds
     * @return a list of MgiCrisprAlleleReportOutcomeMutationProjection Spring database projections
     *         that correspond to the list of outcomeIds provided.
     */
    List<MgiCrisprAlleleReportOutcomeMutationProjection> getSelectedOutcomeMutationCrisprReportProjections(List<Long> outcomeIds );
}
