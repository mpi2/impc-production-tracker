package org.gentar.report.utils.mutagenesis_donor;

import org.gentar.report.collection.mgi_crispr_allele.mutagenesis_donor.MgiCrisprAlleleReportMutagenesisDonorProjection;

import java.util.Set;

public interface MgiMutagenesisDonorFormatHelper {
    /**
     *
     * @param mutagenesisDonorProjections
     * @return a String containing data on a set of mutagenesis donors used in a Crispr Attempt.
     */
    String formatMutagenesisDonorData (Set<MgiCrisprAlleleReportMutagenesisDonorProjection> mutagenesisDonorProjections);
}
