package org.gentar.biology.targ_rep.strain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class TargRepStrain extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "targRepStrainSeq", sequenceName = "TARG_REP_STRAIN_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "targRepStrainSeq")
    private Long id;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String name;

    private String mgiStrainAccId;
}
