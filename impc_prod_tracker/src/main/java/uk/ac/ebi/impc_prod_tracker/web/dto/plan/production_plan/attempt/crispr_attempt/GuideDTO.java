package uk.ac.ebi.impc_prod_tracker.web.dto.plan.production_plan.attempt.crispr_attempt;

import lombok.Data;

@Data
public class GuideDTO
{
    private String sequence;

    private String chromosome;

    private Integer start;

    private Integer stop;

    private String strand;

    private String genomeBuild;

    private Boolean truncatedGuide;

    private Double grnaConcentration;

    private String pam3;

    private String pam5;

    private String protospacerSequence;
}
