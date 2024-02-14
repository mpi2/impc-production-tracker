package org.gentar.report.collection.mgi_crispr_allele.genotype_primer;

import java.util.List;

public interface MgiCrisprAlleleReportGenotypePrimerService {

    /**
     *
     * @return a list of MgiCrisprAlleleReportGenotypePrimerProjection Spring database projections
     * corresponding to the list of production plan ids provided.
     */
    List<MgiCrisprAlleleReportGenotypePrimerProjection> getSelectedGenotypePrimerProjections(List<Long> planIds);
}
