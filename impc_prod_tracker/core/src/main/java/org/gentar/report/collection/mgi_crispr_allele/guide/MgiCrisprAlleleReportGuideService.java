package org.gentar.report.collection.mgi_crispr_allele.guide;

import java.util.List;

public interface MgiCrisprAlleleReportGuideService {

    /**
     *
     * @return a list of MgiCrisprAlleleReportGuideProjection Spring database projections
     * (this includes a production plan Id)
     */
    List<MgiCrisprAlleleReportGuideProjection> getSelectedGuideProjections(List<Long> planIds);
}
