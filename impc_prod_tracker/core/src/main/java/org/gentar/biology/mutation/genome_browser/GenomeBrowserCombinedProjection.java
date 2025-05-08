package org.gentar.biology.mutation.genome_browser;

import org.springframework.beans.factory.annotation.Value;

public interface GenomeBrowserCombinedProjection {

    @Value("#{target.centre}")
    String getCentre();

    @Value("#{target.min}")
    String getMin();

    @Value("#{target.allele_symbol}")
    String getAlleleSymbol();

    @Value("#{target.gene_symbol}")
    String getGeneSymbol();

    @Value("#{target.deletion_coordinates}")
    String getDeletionCoordinates();

    @Value("#{target.targeted_exons}")
    String getTargetedExons();

    @Value("#{target.canonical_targeted_exons}")
    String getCanonicalTargetedExons();

    @Value("#{target.fasta}")
    String getFasta();

    @Value("#{target.url}")
    String getUrl();


}
