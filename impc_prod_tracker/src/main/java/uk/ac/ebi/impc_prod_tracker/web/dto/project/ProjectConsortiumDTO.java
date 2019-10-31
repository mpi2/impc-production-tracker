package uk.ac.ebi.impc_prod_tracker.web.dto.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_consortium.ProjectConsortium;

import java.util.List;

@Data
@RequiredArgsConstructor
public class ProjectConsortiumDTO {
    private String consortiumName;

    @JsonProperty("projectConsortiumInstitutes")
    private List<ProjectConsortiumInstituteDTO> projectConsortiumInstituteDTOS;
}
