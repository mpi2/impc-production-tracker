package org.gentar.report.collection.mgi_crispr_allele.nuclease;

import java.util.List;

public interface MgiCrisprAlleleReportNucleaseService {

    /**
     *
     * @return a list of MgiCrisprAlleleReportNucleaseProjection Spring database projections
     * (this includes a production plan Id)
     */
    List<MgiCrisprAlleleReportNucleaseProjection> getSelectedNucleaseProjections(List planIds);

}
