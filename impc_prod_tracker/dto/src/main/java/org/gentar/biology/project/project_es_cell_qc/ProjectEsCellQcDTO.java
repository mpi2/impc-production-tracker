package org.gentar.biology.project.project_es_cell_qc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.gentar.biology.plan.attempt.es_cell.EsCellAttemptDTO;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class ProjectEsCellQcDTO {

    private String completionComment;

    private String completionNote;

    private LocalDate esCellsReceivedOn;

    private Integer numberOfEsCellsPassingQc;

    private Integer numberOfEsCellsReceived;

    private Integer numberOfEsCellsStartingQc;

    private Integer numberPassingQc;

    private Integer numberStartingQc;

    private String esCellQcComment;

}
