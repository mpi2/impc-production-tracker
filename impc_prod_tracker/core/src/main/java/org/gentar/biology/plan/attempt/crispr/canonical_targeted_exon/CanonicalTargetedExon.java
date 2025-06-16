package org.gentar.biology.plan.attempt.crispr.canonical_targeted_exon;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.gentar.BaseEntity;
import org.gentar.biology.mutation.Mutation;

import javax.validation.constraints.NotNull;


@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class CanonicalTargetedExon extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "canonicalExonDeletionSeq", sequenceName = "CANONICAL_EXON_DELETION_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "canonicalExonDeletionSeq")
    private Long id;

    private String exonId;

    @Column(columnDefinition = "TEXT")
    private String transcript;

    @NotNull
    @ToString.Exclude
    @ManyToOne(targetEntity= Mutation.class)
    private Mutation mutation;


}
