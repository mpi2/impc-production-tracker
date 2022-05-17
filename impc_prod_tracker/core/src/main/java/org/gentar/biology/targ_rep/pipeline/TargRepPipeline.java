package org.gentar.biology.targ_rep.pipeline;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;




/**
 * TargRepPipeline.
 */
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Data
@Entity
public class TargRepPipeline extends BaseEntity {
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
