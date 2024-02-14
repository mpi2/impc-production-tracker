package org.gentar.biology.project;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.biology.project.project_es_cell_qc.ProjectEsCellQcDTO;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ProjectCommonDataDTO
{
    @JsonProperty("privacyName")
    private String privacyName;

    // TODO To be validated...
    private Boolean recovery;

    // TODO To be validated...
    private Boolean esCellQcOnly;

    // Any comment about this project.
    private String comment;

    // Date in which the project as changed to an active assignment status.
    private LocalDateTime reactivationDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("esCellDetails")
    private ProjectEsCellQcDTO projectEsCellQcDTO;
}
