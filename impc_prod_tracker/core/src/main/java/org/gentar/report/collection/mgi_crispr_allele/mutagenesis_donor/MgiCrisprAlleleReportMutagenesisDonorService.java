package org.gentar.report.collection.mgi_crispr_allele.mutagenesis_donor;

import java.util.List;

public interface MgiCrisprAlleleReportMutagenesisDonorService {

    /**
     *
     * @return a list of MgiCrisprAlleleReportMutagenesisDonorProjection Spring database projections
     *         corresponding to the list of production plan ids provided.
     */
    List<MgiCrisprAlleleReportMutagenesisDonorProjection> getSelectedMutagenesisDonorProjections(List planIds);

}
