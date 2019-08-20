package uk.ac.ebi.impc_prod_tracker.web.dto.plan.attempt.production.crispr_attempt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GuideDTO
{
    @JsonIgnore
    private Long id;
    @JsonIgnore
    private Long crispr_attempt_plan_id;
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
