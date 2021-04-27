package org.gentar.biology.targ_rep.mutation.type;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class TargRepEsCellMutationType extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "targRepMutationTypeSeq", sequenceName = "TARG_REP_MUTATION_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "targRepMutationTypeSeq")
    private Long id;

    @NotNull
    private String name;
}
