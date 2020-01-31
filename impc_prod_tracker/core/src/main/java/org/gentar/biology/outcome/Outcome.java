package org.gentar.biology.outcome;

import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.biology.mutation.Mutation;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.status.Status;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class Outcome extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "outcomeSeq", sequenceName = "OUTCOME_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "outcomeSeq")
    private Long id;

    @NotNull
    private String tpo;

    @ManyToOne
    private Plan plan;

    @NotNull
    @ManyToOne(targetEntity= Status.class)
    private Status status;

    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "outcomes")
    private Set<Mutation> mutations;
}
