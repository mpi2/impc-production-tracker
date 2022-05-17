package org.gentar.biology.targ_rep.es_cell.mutation_subtype;

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
 * TargRepEsCellMutationSubtype.
 */
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Data
@Entity
public class TargRepEsCellMutationSubtype extends BaseEntity {
    @Id
    @SequenceGenerator(name = "targRepEsCellMutationSubtypeSeq",
        sequenceName = "TARG_REP_ES_CELL_MUTATION_SUBTYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "targRepEsCellMutationSubtypeSeq")
    private Long id;

    @NotNull
    private String name;
}
