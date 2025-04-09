package org.gentar.biology.mutation.mutation_deletion;

import lombok.*;
import org.gentar.BaseEntity;

import jakarta.persistence.*;
import org.gentar.biology.mutation.Mutation;

import javax.validation.constraints.NotNull;


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


}
