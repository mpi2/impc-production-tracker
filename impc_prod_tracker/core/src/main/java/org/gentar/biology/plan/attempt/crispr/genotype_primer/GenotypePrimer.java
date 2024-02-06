package org.gentar.biology.plan.attempt.crispr.genotype_primer;

import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.biology.plan.attempt.crispr.CrisprAttempt;

import jakarta.persistence.*;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class GenotypePrimer extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "genotypePrimerSeq", sequenceName = "GENOTYPE_PRIMER_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genotypePrimerSeq")
    private Long id;

    private String name;

    private String sequence;

    @Column()
    private Integer genomicStartCoordinate;

    @Column()
    private Integer genomicEndCoordinate;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(targetEntity = CrisprAttempt.class)
    @JoinColumn(name = "attempt_id")
    private CrisprAttempt crisprAttempt;
}
