package org.gentar.biology.mutation.sequence;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.gentar.BaseEntity;
import org.gentar.biology.mutation.Mutation;
import org.gentar.biology.sequence.Sequence;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class MutationSequence extends BaseEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "mutationSequenceSeq", sequenceName = "MUTATION_SEQUENCE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mutationSequenceSeq")
    private Long id;

    @NotNull
    @ToString.Exclude
    @ManyToOne(targetEntity= Mutation.class)
    private Mutation mutation;

    @NotNull
    @ManyToOne(cascade=CascadeType.ALL)
    private Sequence sequence;

    @NotNull
    private Integer index;

    // Copy Constructor
    public MutationSequence(MutationSequence mutationSequence)
    {
        this.id = mutationSequence.getId();
        this.mutation = mutationSequence.getMutation();
        this.sequence = new Sequence(mutationSequence.getSequence());
        this.index = mutationSequence.getIndex();
    }
}
