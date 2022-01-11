package org.gentar.biology.targ_rep;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TargRepPipelineDTO {

    private Long id;

    private String name;

    private String description;

    @JsonProperty("geneTrap")
    private Boolean geneTrap;

    @JsonProperty("reportToPublic")
    private Boolean reportToPublic;

}
