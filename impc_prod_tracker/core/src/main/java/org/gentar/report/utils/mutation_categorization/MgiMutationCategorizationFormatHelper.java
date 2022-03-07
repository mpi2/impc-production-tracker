package org.gentar.report.utils.mutation_categorization;

import org.gentar.report.collection.mgi_crispr_allele.mutation_characterization.MgiCrisprAlleleReportMutationCategorizationProjection;

import java.util.Set;

public interface MgiMutationCategorizationFormatHelper {

    /**
     *
     * @param mutationCategorizationProjections
     * @return a String containing the repair mechanism associated with the mutation.
     */
    String formatRepairMechanism (Set<MgiCrisprAlleleReportMutationCategorizationProjection> mutationCategorizationProjections);

    /**
     *
     * @param mutationCategorizationProjections
     * @return a String containing the allele categories associated with the mutation.
     */
    String formatAlleleCategory (Set<MgiCrisprAlleleReportMutationCategorizationProjection> mutationCategorizationProjections);
}
