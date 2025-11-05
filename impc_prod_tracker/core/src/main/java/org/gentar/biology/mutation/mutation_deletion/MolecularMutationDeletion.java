package org.gentar.biology.mutation.mutation_deletion;

import lombok.*;
import org.gentar.BaseEntity;

import jakarta.persistence.*;
import org.gentar.biology.mutation.Mutation;
import org.gentar.biology.plan.attempt.crispr.canonical_targeted_exon.CanonicalTargetedExon;
import org.gentar.biology.plan.attempt.crispr.targeted_exon.TargetedExon;

import javax.validation.constraints.NotNull;
import java.util.Set;


@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class MolecularMutationDeletion extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "molecularMutationDeletionSeq", sequenceName = "MOLECULAR_MUTATION_DELETION_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "molecularMutationDeletionSeq")
    private Long id;

    private String chr;

    private Integer start;

    private Integer stop;

    private Long size;

    @NotNull
    @ToString.Exclude
    @ManyToOne(targetEntity= Mutation.class)
    private Mutation mutation;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "molecularMutationDeletion", cascade= CascadeType.ALL, orphanRemoval=true)
    private Set<CanonicalTargetedExon> canonicalTargetedExons;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "molecularMutationDeletion", cascade= CascadeType.ALL, orphanRemoval=true)
    private Set<TargetedExon> targetedExons;

}
