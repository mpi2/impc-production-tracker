package org.gentar.biology.targ_rep.ikmc_project.status;

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
