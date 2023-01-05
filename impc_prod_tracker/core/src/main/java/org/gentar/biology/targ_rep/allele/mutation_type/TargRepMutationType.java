package org.gentar.biology.targ_rep.allele.mutation_type;

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
 * TargRepMutationType.
 */
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Data
@Entity
public class TargRepMutationType extends BaseEntity {
    @Id
    @SequenceGenerator(name = "targRepMutationTypeSeq", sequenceName = "TARG_REP_MUTATION_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "targRepMutationTypeSeq")
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String code;

    private String alleleCode;
}
