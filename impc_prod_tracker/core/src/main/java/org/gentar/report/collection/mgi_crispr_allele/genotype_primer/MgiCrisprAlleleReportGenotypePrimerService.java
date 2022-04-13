package org.gentar.report.collection.mgi_crispr_allele.genotype_primer;

import org.gentar.report.collection.mgi_crispr_allele.guide.MgiCrisprAlleleReportGuideProjection;

import java.util.List;

public interface MgiCrisprAlleleReportGenotypePrimerService {

    /**
     *
     * @param planIds
     * @return a list of MgiCrisprAlleleReportGenotypePrimerProjection Spring database projections
     * corresponding to the list of production plan ids provided.
     */
    List<MgiCrisprAlleleReportGenotypePrimerProjection> getSelectedGenotypePrimerProjections(List<Long> planIds);
}
