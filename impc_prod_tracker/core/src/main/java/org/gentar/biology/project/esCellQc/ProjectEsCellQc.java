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

    private Integer numberOfEsCellsPassingQc;

    private String completionNote;

    private Integer numberOfEsCellsReceived;

    private LocalDate esCellsReceivedOn;

    @ManyToOne(targetEntity = EsCellCentrePipeline.class)
    private EsCellCentrePipeline esCellsReceivedFrom;

    @Column(columnDefinition = "TEXT")
    private String completionComment;

    @Column(columnDefinition = "boolean default false")
    private Boolean esCellQcOnly;

    private Integer numberOfEsCellsStartingQc;

    private Integer numberStartingQc;

    private Integer numberPassingQc;
}
