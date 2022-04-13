package org.gentar.biology.targ_rep.ikmc_project.status.type;

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
 * TargRepIkmcProjectStatusType.
 */
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Data
@Entity
public class TargRepIkmcProjectStatusType extends BaseEntity {
    @Id
    @SequenceGenerator(name = "targRepIkmcProjectStatusTypeSeq",
        sequenceName = "TARG_REP_IKMC_PROJECT_STATUS_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "targRepIkmcProjectStatusTypeSeq")
    private Long id;

    @NotNull
    private String name;
}
