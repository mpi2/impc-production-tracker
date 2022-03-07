package org.gentar.report.collection.mgi_crispr_allele.mutation_characterization;

import org.gentar.report.collection.mgi_crispr_allele.guide.MgiCrisprAlleleReportGuideProjection;

import java.util.List;

public interface MgiCrisprAlleleReportMutationCategorizationService {

    /**
     *
     * @param mutationIds
     * @return a list of MgiCrisprAlleleReportMutationCategorizationProjection Spring database projections
     * (each includes a mutation Id)
     */
    List<MgiCrisprAlleleReportMutationCategorizationProjection> getSelectedMutationCategorizationProjections(List<Long> mutationIds);

}
