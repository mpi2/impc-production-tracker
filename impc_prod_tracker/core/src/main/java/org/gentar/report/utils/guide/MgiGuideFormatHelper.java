package org.gentar.report.utils.guide;

import org.gentar.report.collection.mgi_crispr_allele.guide.MgiCrisprAlleleReportGuideProjection;

import java.util.Set;

public interface MgiGuideFormatHelper {

    /**
     *
     * @return a String containing data on a set of guides used in a Crispr Attempt.
     */
    String formatGuideData (Set<MgiCrisprAlleleReportGuideProjection> guideProjections);

}
