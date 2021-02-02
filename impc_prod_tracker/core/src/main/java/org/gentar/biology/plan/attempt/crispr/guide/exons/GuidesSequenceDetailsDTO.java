package org.gentar.biology.plan.attempt.crispr.guide.exons;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GuidesSequenceDetailsDTO
{
    private Long id;

    @JsonProperty("pam_right")
    private Integer pamRight;

    @JsonProperty("chr")
    private String chrName;

    @JsonProperty("start")
    private Integer chrStart;

    @JsonProperty("end")
    private Integer chrEnd;

    @JsonProperty("sequence")
    private String seq;
}
