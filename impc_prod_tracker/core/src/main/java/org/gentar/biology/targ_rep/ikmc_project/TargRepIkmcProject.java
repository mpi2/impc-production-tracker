package org.gentar.biology.targ_rep.ikmc_project;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;
import org.gentar.biology.targ_rep.ikmc_project.status.TargRepIkmcProjectStatus;
import org.gentar.biology.targ_rep.pipeline.TargRepPipeline;

/**
 * TargRepIkmcProject.
 */
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Data
@Entity
public class TargRepIkmcProject extends BaseEntity {
    @Id
    @SequenceGenerator(name = "targRepIkmcProjectSeq", sequenceName = "TARG_REP_IKMC_PROJECT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "targRepIkmcProjectSeq")
    private Long id;

    @NotNull
    private String name;

    @ManyToOne(targetEntity = TargRepIkmcProjectStatus.class)
    private TargRepIkmcProjectStatus status;

    @NotNull
    @ManyToOne(targetEntity = TargRepPipeline.class)
    private TargRepPipeline pipeline;
}
