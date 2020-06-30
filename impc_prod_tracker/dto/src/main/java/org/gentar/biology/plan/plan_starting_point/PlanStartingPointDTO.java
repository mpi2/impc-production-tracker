package org.gentar.biology.plan.plan_starting_point;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
public class PlanStartingPointDTO extends RepresentationModel<PlanStartingPointDTO>
{
    @JsonIgnore
    private Long id;

    @JsonProperty("outcomeTpo")
    private String tpo;
}
