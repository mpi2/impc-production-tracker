package org.gentar.report.dto.crispr_product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Guide {
    private String sequence;
    private String guideSequence;
    private String pam;
    private String chr;
    private Integer start;
    private Integer stop;
    private String strand;
    private String genomeBuild;
    private Integer grnaConcentration;
    private Boolean truncatedGuide;
    private Boolean reversed;
    private Boolean sangerService;
    private String guideFormat;
    private String guideSource;
}
