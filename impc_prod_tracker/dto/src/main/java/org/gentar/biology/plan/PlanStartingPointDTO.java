package org.gentar.biology.plan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlanStartingPointDTO {
    @JsonIgnore
    private Long id;

    @JsonProperty("outcomeTpo")
    private String tpo;
}
