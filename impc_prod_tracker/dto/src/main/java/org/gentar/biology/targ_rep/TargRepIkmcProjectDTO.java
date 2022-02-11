package org.gentar.biology.targ_rep;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TargRepIkmcProjectDTO {

    public Long id;

    public String name;

    @JsonProperty("pipeline_id")
    public Long pipelineId;

    public TargRepIkmcProjectStatusDTO status;
}
