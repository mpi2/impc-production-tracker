package uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.genotype_primer;

import lombok.*;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.CrisprAttempt;

import javax.persistence.*;

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

    @Column(nullable = true)
    private int genomicStartCoordinate;

    @Column(nullable = true)
    private int genomicEndCoordinate;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(targetEntity = CrisprAttempt.class)
    @JoinColumn(name = "attempt_id")
    private CrisprAttempt crisprAttempt;
}
