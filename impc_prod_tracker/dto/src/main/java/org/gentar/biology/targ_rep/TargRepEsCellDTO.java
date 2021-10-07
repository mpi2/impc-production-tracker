package org.gentar.biology.targ_rep;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TargRepEsCellDTO
{
    @JsonIgnore
    private Long id;

    private String name;

    @JsonProperty("pipelineName")
    private String pipelineName;
}
