package uk.ac.ebi.impc_prod_tracker.web.dto.plan.production.crispr_attempt;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("grna_concentration")
    private Double grnaConcentration;

    private String sequence;

    @JsonProperty("truncated_guide")
    private Boolean truncatedGuide;

    private String strand;

    @JsonProperty("genome_build")
    private String genomeBuild;

    private String pam3;
    private String pam5;

    @JsonProperty("protospacer_sequence")
    private String protospacerSequence;
}
