package org.gentar.biology.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.gentar.biology.plan.PlanDTO;

@Data
public class ProjectCreationDTO extends ProjectDTO
{
    @JsonProperty("planDetails")
    private PlanDTO planDTO;
}
