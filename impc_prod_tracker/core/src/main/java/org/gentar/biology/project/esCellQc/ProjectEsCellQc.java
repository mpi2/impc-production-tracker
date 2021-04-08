package org.gentar.biology.project.esCellQc;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;
import org.gentar.biology.project.Project;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class ProjectEsCellQc  extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "projectEsCellQcSeq", sequenceName = "PROJECT_ES_CELL_QC_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projectEsCellQcSeq")
    private Long id;

    @NotNull
    @OneToOne
    private Project project;

    @ManyToOne(targetEntity = EsCellQcComment.class)
    private EsCellQcComment esCellQcComment;

    private Integer number_of_es_cells_passing_qc;

    private String completion_note;

    private Integer number_of_es_cells_received;

    private LocalDate es_cells_received_on;

    @ManyToOne(targetEntity = EsCellCentrePipeline.class)
    private EsCellCentrePipeline es_cells_received_from_id;

    @Column(columnDefinition = "TEXT")
    private String completionComment;

    @Column(columnDefinition = "boolean default false")
    private Boolean es_cell_qc_only;

    private Integer number_of_es_cells_starting_qc;

    private Integer number_starting_qc;

    private Integer number_passing_qc;
}
