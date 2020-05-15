package org.gentar.biology.project;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
/**
 * This class has the information that is common to every Project*DTO
 */
public class ProjectCommonDataDTO
{
    // Public identifier of the project. No editable.
    private String tpn;

    // Id of the project in the iMits system where the equivalent for a project is a plan.
    // No editable.
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("imitsMiPlanId")
    private Long imitsMiPlan;

    // Reference for the project in the internal systems of the client.
    @JsonProperty("externalReference")
    private String projectExternalRef;

    // Date in which the project as changed to an active assignment status.
    private LocalDateTime reactivationDate;

    // To be validated...
    private Boolean recovery;

    // Any comment about this project.
    private String comment;

    @JsonProperty("privacyName")
    private String privacyName;

    @JsonProperty("speciesNames")
    private List<String> speciesNames;

    // Consortia information.
    @JsonProperty("consortia")
    private List<ProjectConsortiumDTO> projectConsortiumDTOS;
}
