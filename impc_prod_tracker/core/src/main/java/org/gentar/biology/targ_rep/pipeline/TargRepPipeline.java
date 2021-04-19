package org.gentar.biology.targ_rep.pipeline;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class TargRepPipeline extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "targRepPipelineSeq", sequenceName = "TARG_REP_PIPELINE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "targRepPipelineSeq")
    private Long id;

    @NotNull
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "boolean default true")
    private Boolean reportToPublic;

    @Column(columnDefinition = "boolean default false")
    private Boolean geneTrap;
}
