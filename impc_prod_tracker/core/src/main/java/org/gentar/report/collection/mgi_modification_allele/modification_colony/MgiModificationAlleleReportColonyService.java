package org.gentar.report.collection.mgi_modification_allele.modification_colony;

import org.gentar.report.collection.mgi_crispr_allele.colony.MgiCrisprAlleleReportColonyProjection;

import java.util.List;

public interface MgiModificationAlleleReportColonyService {

    /**
     *
     * @return a list of MgiModificationAlleleReportColonyProjection Spring database projections
     */
    List<MgiModificationAlleleReportColonyProjection> getAllMgiModificationAlleleReportColonyProjections();

}
