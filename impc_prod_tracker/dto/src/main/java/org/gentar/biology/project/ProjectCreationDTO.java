package org.gentar.biology.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import org.gentar.biology.intention.ProjectIntentionDTO;
import org.gentar.biology.plan.PlanMinimumCreationDTO;
import java.util.List;

/**
 * Class used to receive a request from the user in order to create a project in the system.
 * The only additional information for now is the minimum information needed to create a plan
 * linked to the project and the intentions information.
 */
@Data
public class ProjectCreationDTO
{
    @JsonUnwrapped
    private ProjectCommonDataDTO projectCommonDataDTO;

    // Plan with minimum information to be created as the firs plan for the project.
    @JsonProperty("planDetails")
    private PlanMinimumCreationDTO planMinimumCreationDTO;

    // Intentions for the project.
    @JsonProperty("projectIntentions")
    private List<ProjectIntentionDTO> projectIntentionDTOS;

    @JsonProperty("speciesNames")
    private List<String> speciesNames;

    // Consortia information.
    @JsonProperty("consortia")
    private List<ProjectConsortiumDTO> projectConsortiumDTOS;
}
