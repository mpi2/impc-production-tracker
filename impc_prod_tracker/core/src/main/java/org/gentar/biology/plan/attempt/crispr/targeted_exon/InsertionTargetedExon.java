package org.gentar.biology.plan.attempt.crispr.targeted_exon;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.gentar.BaseEntity;
import org.gentar.biology.insertion_sequence.InsertionSequence;

import javax.validation.constraints.NotNull;


@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class InsertionTargetedExon extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "insertionExonSeq", sequenceName = "INSERTION_EXON_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "insertionExonSeq")
    private Long id;

    private String exonId;

    @Column(columnDefinition = "TEXT")
    private String transcript;

    @NotNull
    @ToString.Exclude
    @ManyToOne(targetEntity= InsertionSequence.class)
    private InsertionSequence insertionSequence;

}
