package org.gentar.biology.targ_rep.allele.allele_sequence_annotation.mutation_type;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;


/**
 * TargRepAlleleSequenceAnnotationMutationType.
 */
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Data
@Entity
public class TargRepAlleleSequenceAnnotationMutationType extends BaseEntity {
    @Id
    @SequenceGenerator(name = "targRepAlleleSequenceAnnotationMutationTypeSeq",
        sequenceName = "TARG_REP_ALLELE_SEQUENCE_ANNOTATION_MUTATION_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "targRepAlleleSequenceAnnotationMutationTypeSeq")
    private Long id;

    @NotNull
    private String name;
}
