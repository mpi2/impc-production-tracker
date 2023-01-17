package org.gentar.biology.targ_rep.allele.mutation_method;

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
