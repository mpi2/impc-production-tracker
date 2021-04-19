package org.gentar.biology.targ_rep.allele.mutation_type;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class TargRepMutationType extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "targRepMutationTypeSeq", sequenceName = "TARG_REP_MUTATION_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "targRepMutationTypeSeq")
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String code;

    @NotNull
    private String alleleCode;
}
