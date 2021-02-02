package org.gentar.biology.plan.attempt.crispr.guide;

import org.gentar.biology.plan.attempt.crispr.guide.exons.ExonDTO;
import org.gentar.biology.plan.attempt.crispr.guide.exons.GuidesSequenceDetailsDTO;

import java.util.List;

public interface GuideService
{
    /**
     * Get list of exons by gene.
     * @param marker_symbol
     * @return List of exons.
     */
    List<ExonDTO> getExonsByMarkerSymbol(String marker_symbol);

    /**
     * Get list of guide sequences by exon.
     * @param ensembl_id
     * @return List of guide sequences.
     */
    List<GuidesSequenceDetailsDTO> getGuideSeqsByEnsemblId(String ensembl_id);
}
