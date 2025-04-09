package org.gentar.biology.plan.attempt.crispr.targeted_exon;

import jakarta.persistence.*;
import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.biology.mutation.Mutation;

import javax.validation.constraints.NotNull;


@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class TargetedExon extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "exonDeletionSeq", sequenceName = "EXON_DELETION_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exonDeletionSeq")
    private Long id;

    private String exonId;

    @NotNull
    @ToString.Exclude
    @ManyToOne(targetEntity= Mutation.class)
    private Mutation mutation;


}
