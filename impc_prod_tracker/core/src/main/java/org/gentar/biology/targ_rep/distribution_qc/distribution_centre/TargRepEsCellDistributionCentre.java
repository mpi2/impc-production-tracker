package org.gentar.biology.targ_rep.distribution_qc.distribution_centre;

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
 * TargRepEsCellDistributionCentre.
 */
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Data
@Entity
public class TargRepEsCellDistributionCentre extends BaseEntity {
    @Id
    @SequenceGenerator(name = "targRepEsCellDistributionCentreSeq",
        sequenceName = "TARG_REP_ES_CELL_DISTRIBUTION_CENTRE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "targRepEsCellDistributionCentreSeq")
    private Long id;

    @NotNull
    private String name;
}
