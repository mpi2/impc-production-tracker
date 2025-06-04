package org.gentar.biology.insertion_sequence;

import jakarta.persistence.*;
import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.biology.mutation.Mutation;

import javax.validation.constraints.NotNull;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@AllArgsConstructor
@Data
@Entity
public class InsertionSequence extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "insertionSequenceSeq", sequenceName = "INSERTION_SEQUENCE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "insertionSequenceSeq")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String sequence;

    private String location;

    @NotNull
    @ToString.Exclude
    @ManyToOne(targetEntity= Mutation.class)
    private Mutation mutation;


    // Copy Constructor
    public InsertionSequence(InsertionSequence insertionSequence)
    {
        this.id = insertionSequence.getId();
        this.mutation = insertionSequence.getMutation();
        this.sequence = insertionSequence.getSequence();
        this.location = insertionSequence.getLocation();
    }
}
