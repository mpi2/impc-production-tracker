package org.gentar.biology.targ_rep.ikmc_project.status;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;
import org.gentar.biology.targ_rep.ikmc_project.status.type.TargRepIkmcProjectStatusType;



/**
 * TargRepIkmcProjectStatus.
 */
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Data
@Entity
public class TargRepIkmcProjectStatus extends BaseEntity {
    @Id
    @SequenceGenerator(name = "targRepIkmcProjectStatusSeq",
        sequenceName = "TARG_REP_IKMC_PROJECT_STATUS_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "targRepIkmcProjectStatusSeq")
    private Long id;

    @NotNull
    private String name;

    @ManyToOne(targetEntity = TargRepIkmcProjectStatusType.class)
    private TargRepIkmcProjectStatusType productStatusType;

    @NotNull
    private Integer orderBy;
}
