package org.gentar.biology.mutation.genome_browser;

import org.springframework.beans.factory.annotation.Value;

public interface TargetedExonsProjection {

    @Value("#{target.centre}")
    String getCentre();

    @Value("#{target.min}")
    String getMin();

    @Value("#{target.allele_symbol}")
    String getAlleleSymbol();

    @Value("#{target.gene_symbol}")
    String getGeneSymbol();

    @Value("#{target.exons}")
    String getExons();

    @Value("#{target.fasta}")
    String getFasta();

    @Value("#{target.url}")
    String getUrl();
}
