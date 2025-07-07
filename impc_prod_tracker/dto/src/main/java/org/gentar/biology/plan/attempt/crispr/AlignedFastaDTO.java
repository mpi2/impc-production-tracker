package org.gentar.biology.plan.attempt.crispr;

import lombok.Data;

@Data
public class AlignedFastaDTO {
    private Long id;
    private String chrom;
    private Integer chromStart;
    private Integer chromEnd;
    private String name;
    private Integer score;
    private String strand;
    private Integer thickStart;
    private Integer thickEnd;
    private Integer itemRgb;
    private Integer blockCount;
    private String blockSizes;
    private String blockStarts;
}
