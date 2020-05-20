package org.gentar.biology.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
/**
 * This class has the information that is common to every Project*DTO.
 * Class is package protected because it should be used only internally.
 */
public class ProjectCommonDataDTO
{
    @JsonProperty("privacyName")
    private String privacyName;

    // Reference for the project in the internal systems of the client.
    @JsonProperty("externalReference")
    private String projectExternalRef;

    // Date in which the project as changed to an active assignment status.
    private LocalDateTime reactivationDate;

    // To be validated...
    private Boolean recovery;

    // Any comment about this project.
    private String comment;

    @JsonProperty("speciesNames")
    private List<String> speciesNames;

    // Consortia information.
    @JsonProperty("consortia")
    private List<ProjectConsortiumDTO> projectConsortiumDTOS;
}
