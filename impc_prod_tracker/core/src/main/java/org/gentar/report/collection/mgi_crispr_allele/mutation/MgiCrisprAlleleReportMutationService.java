package org.gentar.report.collection.mgi_crispr_allele.mutation;

import java.util.List;

public interface MgiCrisprAlleleReportMutationService {

    /**
     *
     * @return a list of MgiCrisprAlleleReportMutationGeneProjection Spring database projections
     *         that correspond to the list of mutationIds provided.
      */
    List<MgiCrisprAlleleReportMutationGeneProjection> getSelectedMutationGeneProjections(List mutationIds);
}
