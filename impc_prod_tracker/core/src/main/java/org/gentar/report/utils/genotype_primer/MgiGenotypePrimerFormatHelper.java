package org.gentar.report.utils.genotype_primer;

import org.gentar.report.collection.mgi_crispr_allele.genotype_primer.MgiCrisprAlleleReportGenotypePrimerProjection;

import java.util.Set;

public interface MgiGenotypePrimerFormatHelper {
    /**
     *
     * @return a String containing data on a set of mutagenesis donors used in a Crispr Attempt.
     */
    String formatGenotypePrimerData (Set<MgiCrisprAlleleReportGenotypePrimerProjection> genotypePrimerProjections);
}
