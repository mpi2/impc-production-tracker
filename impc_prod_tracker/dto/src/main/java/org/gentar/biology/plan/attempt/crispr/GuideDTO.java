package org.gentar.biology.plan.attempt.crispr;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GuideDTO
{
    private Long id;
    private String sequence;
    private String guideSequence;
    private String pam;
    private String chr;
    private Integer start;
    private Integer stop;
    private String strand;
    private String genomeBuild;
    private Double grnaConcentration;
    private Boolean truncatedGuide;
    private Boolean reversed;
    private Boolean sangerService;
    private String ensemblExonId;
}
