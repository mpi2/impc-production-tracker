package org.gentar.report.collection.mgi_crispr_allele.sequence;

import java.util.List;

public interface MgiCrisprAlleleReportMutationSequenceService {

    /**
     *
     * @return a list of MgiCrisprAlleleReportMutationSequenceProjection Spring database projections
     * (each includes a mutation Id)
     */
    List<MgiCrisprAlleleReportMutationSequenceProjection> getSelectedMutationSequenceProjections(List mutationIds);
}
