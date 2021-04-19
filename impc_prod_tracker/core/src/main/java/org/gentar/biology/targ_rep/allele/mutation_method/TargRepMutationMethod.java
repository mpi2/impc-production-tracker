package org.gentar.biology.targ_rep.allele.mutation_method;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class TargRepMutationMethod  extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "targRepMutationMethodSeq", sequenceName = "TARG_REP_MUTATION_METHOD_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "targRepMutationMethodSeq")
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String code;

    @NotNull
    private String allelePrefix;
}
