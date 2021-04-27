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
    @SequenceGenerator(name = "targRepEsCellMutationTypeSeq", sequenceName = "TARG_REP_ES_CELL_MUTATION_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "targRepEsCellMutationTypeSeq")
    private Long id;

    @NotNull
    private String name;
}
