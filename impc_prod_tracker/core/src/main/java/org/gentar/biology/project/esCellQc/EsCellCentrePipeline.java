package org.gentar.biology.project.esCellQc;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class EsCellCentrePipeline extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "esCellCentrePipelineSeq", sequenceName = "ES_CELL_CENTRE_PIPELINE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "esCellCentrePipelineSeq")
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String centres;
}
