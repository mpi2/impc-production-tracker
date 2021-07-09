package org.gentar.biology.project.project_es_cell_qc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class ProjectEsCellQcDTO
{
    private Long id;
    private Integer numberOfEsCellsReceived;
    private String esCellsReceivedFromName;
    private LocalDate esCellsReceivedOn;
    private Integer numberOfEsCellsStartingQc;
    private Integer numberOfEsCellsPassingQc;
    private String esCellQcComment;
}
