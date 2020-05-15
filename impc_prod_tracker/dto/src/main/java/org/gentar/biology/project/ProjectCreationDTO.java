package org.gentar.biology.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.gentar.biology.intention.ProjectIntentionDTO;
import org.gentar.biology.plan.PlanDTO;
import java.util.List;

/**
 * Class used to receive a request from the user in order to create a project in the system.
 * The only additional information for now is the minimum information needed to create a plan
 * linked to the project and the intentions information.
 */
@Data
public class ProjectCreationDTO extends ProjectCommonDataDTO
{
    // Plan with minimum information to be created as the firs plan for rhe project.
    @JsonProperty("planDetails")
    private PlanDTO planDTO;

    // Intentions for the project.
    @JsonProperty("projectIntentions")
    private List<ProjectIntentionDTO> projectIntentionDTOS;
}
