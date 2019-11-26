package org.gentar.biology.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class ProjectConsortiumDTO {
    private String consortiumName;

    @JsonProperty("institutes")
    private List<ProjectConsortiumInstituteDTO> projectConsortiumInstituteDTOS;
}
