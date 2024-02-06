package org.gentar.report.utils.nuclease;

import org.gentar.report.collection.mgi_crispr_allele.nuclease.MgiCrisprAlleleReportNucleaseProjection;

import java.util.Set;

public interface MgiNucleaseFormatHelper {
    /**
     *
     * @return a String containing data on a set of nucleases used in a Crispr Attempt.
     */
    String formatNucleaseData (Set<MgiCrisprAlleleReportNucleaseProjection> nucleaseProjections);

}
