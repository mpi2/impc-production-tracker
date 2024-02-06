package org.gentar.report.utils.sequence;

import org.gentar.report.collection.mgi_crispr_allele.sequence.MgiCrisprAlleleReportMutationSequenceProjection;

import java.util.Set;

public interface MgiMutationSequenceFormatHelper {

    /**
     *
     * @return a String containing data on a set of sequences associated with a mutation.
     */
    String formatMutationSeqeunceData (Set<MgiCrisprAlleleReportMutationSequenceProjection> mutationSequenceProjections);

}
