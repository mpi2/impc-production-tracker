package org.gentar.biology.insertion_sequence;

import jakarta.persistence.*;
import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.biology.mutation.Mutation;
import org.gentar.biology.plan.attempt.crispr.canonical_targeted_exon.InsertionCanonicalTargetedExon;
import org.gentar.biology.plan.attempt.crispr.targeted_exon.InsertionTargetedExon;

import javax.validation.constraints.NotNull;
import java.util.Set;

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

    private String ins;

    @Column(columnDefinition = "TEXT")
    private String sequence;

    private String location;

    @NotNull
    @ToString.Exclude
    @ManyToOne(targetEntity= Mutation.class)
    private Mutation mutation;


    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "insertionSequence", cascade= CascadeType.ALL, orphanRemoval=true)
    private Set<InsertionCanonicalTargetedExon> insertionCanonicalTargetedExons;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "insertionSequence", cascade= CascadeType.ALL, orphanRemoval=true)
    private Set<InsertionTargetedExon> insertionTargetedExons;



    // Copy Constructor
    public InsertionSequence(InsertionSequence insertionSequence)
    {
        this.id = insertionSequence.getId();
        this.ins = insertionSequence.getIns();
        this.mutation = insertionSequence.getMutation();
        this.sequence = insertionSequence.getSequence();
        this.location = insertionSequence.getLocation();
        this.insertionCanonicalTargetedExons = insertionSequence.getInsertionCanonicalTargetedExons();
        this.insertionTargetedExons = insertionSequence.getInsertionTargetedExons();
    }
}
