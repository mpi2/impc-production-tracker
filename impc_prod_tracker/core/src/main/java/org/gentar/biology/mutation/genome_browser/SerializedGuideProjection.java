package org.gentar.biology.mutation.genome_browser;

import org.springframework.beans.factory.annotation.Value;

public interface SerializedGuideProjection {


    @Value("#{target.pin}")
    String getPin();

    @Value("#{target.chrom}")
    String getChrom();

    @Value("#{target.chrom_start}")
    String getChromStart();

    @Value("#{target.chrom_end}")
    String getChromEnd();

    @Value("#{target.guide_name}")
    String getGuideName();

    @Value("#{target.score}")
    String getScore();

    @Value("#{target.strand}")
    String getStrand();

    @Value("#{target.thick_start}")
    String getThickStart();

    @Value("#{target.thick_end}")
    String getThickEnd();

    @Value("#{target.item_rgb}")
    String getItemRgb();

    @Value("#{target.min}")
    String getMin();
}
