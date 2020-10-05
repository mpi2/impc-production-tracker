package org.gentar.biology.project;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import org.gentar.biology.intention.ProjectIntentionDTO;
import org.gentar.biology.intention.ProjectIntentionResponseDTO;
import org.gentar.biology.status_stamps.StatusStampsDTO;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Class to represent the response to return in a endpoint requesting information about a project.
 * The fields added in this class correspond to read only information.
 */
@Relation(collectionRelation = "projects")
@Data
public class ProjectResponseDTO extends RepresentationModel<ProjectResponseDTO>
{
    // Public identifier of the project. No editable.
    private String tpn;

    // Id of the project in the iMits system where the equivalent for a project is a plan.
    // No editable.
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("imitsMiPlanId")
    private Long imitsMiPlan;

    @JsonUnwrapped
    private ProjectCommonDataDTO projectCommonDataDTO;

    // Name of the assignment status for the project.
    private String assignmentStatusName;

    // Name of the summary status name for the project. The summary status summarises the statuses
    // of all the plans that belong to the project.
    private String summaryStatusName;

    // Stamps for the changes of assignment status in the project.
    @JsonProperty("assignmentStatusStamps")
    private List<StatusStampsDTO> statusStampsDTOS;

    // Intentions for the project.
    @JsonProperty("projectIntentions")
    private List<ProjectIntentionResponseDTO> projectIntentionDTOS;

    // Indicates if this project is a restricted view of the data because of the permissions for the
    // user.
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isObjectRestricted;

    // A list of work units that are related with this plan through its plans.
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<String> relatedWorkUnitNames;

    // A list of work groups that are related with this plan through its plans.
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<String> relatedWorkGroupNames;

    @JsonProperty("speciesNames")
    private List<String> speciesNames;

    // Consortia information.
    @JsonProperty("consortia")
    private List<ProjectConsortiumDTO> projectConsortiumDTOS;
}
