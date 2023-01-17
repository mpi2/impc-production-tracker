package org.gentar.biology.targ_rep.ikmc_project.status.type;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
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
