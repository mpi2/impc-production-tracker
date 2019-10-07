package uk.ac.ebi.impc_prod_tracker.data.biology.sequence;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.allele.Allele;
import uk.ac.ebi.impc_prod_tracker.data.biology.sequence.category.SequenceCategory;
import uk.ac.ebi.impc_prod_tracker.data.biology.sequence.type.SequenceType;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class Sequence extends BaseEntity {

    @Id
    @SequenceGenerator(name = "sequenceSeq", sequenceName = "SEQUENCE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceSeq")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String sequence;

    @ManyToOne(targetEntity= SequenceType.class)
    private SequenceType sequenceType;

    @ManyToOne(targetEntity= SequenceCategory.class)
    private SequenceCategory sequenceCategory;

    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "sequences")
    private Set<Allele> alleles;
}
