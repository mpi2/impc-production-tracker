package org.gentar.report.collection.mgi_crispr_allele.mutation_characterization;

import java.util.List;

public interface MgiCrisprAlleleReportMutationCategorizationService {

    /**
     *
     * @return a list of MgiCrisprAlleleReportMutationCategorizationProjection Spring database projections
     * (each includes a mutation Id)
     */
    List<MgiCrisprAlleleReportMutationCategorizationProjection> getSelectedMutationCategorizationProjections(List<Long> mutationIds);

}
