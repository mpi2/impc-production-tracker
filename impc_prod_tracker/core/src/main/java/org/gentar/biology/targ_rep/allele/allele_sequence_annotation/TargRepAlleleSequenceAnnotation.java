package org.gentar.biology.targ_rep.allele.allele_sequence_annotation;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.gentar.BaseEntity;
import org.gentar.biology.targ_rep.allele.TargRepAllele;
import org.gentar.biology.targ_rep.allele.allele_sequence_annotation.mutation_type.TargRepAlleleSequenceAnnotationMutationType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class TargRepAlleleSequenceAnnotation extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "targRepAlleleSequenceAnnotationSeq",
            sequenceName = "TARG_REP_ALLELE_SEQUENCE_ANNOTATION_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "targRepAlleleSequenceAnnotationSeq")
    private Long id;

    @ManyToOne(targetEntity= TargRepAlleleSequenceAnnotationMutationType.class)
    private TargRepAlleleSequenceAnnotationMutationType mutationType;

    private String expected;

    private String actual;

    @Column(columnDefinition = "TEXT")
    private String comment;

    private Integer oligosStartCoordinate;

    private Integer oligosEndCoordinate;

    private Integer mutationLength;

    private Integer genomicStartCoordinate;

    private Integer genomicEndCoordinate;

    @ToString.Exclude
    @NotNull
    @ManyToOne(targetEntity= TargRepAllele.class)
    private TargRepAllele allele;
}
