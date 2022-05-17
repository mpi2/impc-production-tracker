package org.gentar.biology.targ_rep.allele.mutation_method;

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
 * TargRepMutationMethod.
 */
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Data
@Entity
public class TargRepMutationMethod extends BaseEntity {
    @Id
    @SequenceGenerator(name = "targRepMutationMethodSeq",
        sequenceName = "TARG_REP_MUTATION_METHOD_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "targRepMutationMethodSeq")
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String code;

    @NotNull
    private String allelePrefix;
}
