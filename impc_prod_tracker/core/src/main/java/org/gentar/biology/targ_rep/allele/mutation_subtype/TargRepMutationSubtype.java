package org.gentar.biology.targ_rep.allele.mutation_subtype;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class TargRepMutationSubtype extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "targRepMutationSubtypeSeq", sequenceName = "TARG_REP_MUTATION_SUBTYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "targRepMutationSubtypeSeq")
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String code;
}
