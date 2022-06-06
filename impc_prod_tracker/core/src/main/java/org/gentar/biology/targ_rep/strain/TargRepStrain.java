package org.gentar.biology.targ_rep.strain;

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
 * TargRepStrain.
 */
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Data
@Entity
public class TargRepStrain extends BaseEntity {
    @Id
    @SequenceGenerator(name = "targRepStrainSeq", sequenceName = "TARG_REP_STRAIN_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "targRepStrainSeq")
    private Long id;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String name;

    private String mgiStrainAccId;
}
