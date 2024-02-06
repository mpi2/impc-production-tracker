package org.gentar.biology.project.esCellQc;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.esCellQc.centre_pipeline.EsCellCentrePipeline;
import org.gentar.biology.project.esCellQc.comment.EsCellQcComment;

import java.time.LocalDate;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class ProjectEsCellQc  extends BaseEntity
{
    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne
    @MapsId
    private Project project;

    private Integer numberOfEsCellsReceived;

    @ManyToOne(targetEntity = EsCellCentrePipeline.class)
    private EsCellCentrePipeline esCellsReceivedFrom;

    private LocalDate esCellsReceivedOn;

    private Integer numberOfEsCellsStartingQc;

    private Integer numberOfEsCellsPassingQc;

    @ManyToOne(targetEntity = EsCellQcComment.class)
    private EsCellQcComment esCellQcComment;
}
