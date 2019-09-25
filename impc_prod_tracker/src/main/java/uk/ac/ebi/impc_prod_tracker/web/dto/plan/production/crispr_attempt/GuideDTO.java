package uk.ac.ebi.impc_prod_tracker.web.dto.plan.production.crispr_attempt;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GuideDTO
{
    private Long id;
    private String chr;
    private Integer start;
    private Integer stop;
    private Double grnaConcentration;
    private String sequence;
    private Boolean truncatedGuide;
    private String strand;
    private String genomeBuild;
    private String pam3;
    private String pam5;
    private String protospacerSequence;
}
