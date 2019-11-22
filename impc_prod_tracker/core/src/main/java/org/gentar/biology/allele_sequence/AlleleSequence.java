package org.gentar.biology.allele_sequence;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;
import org.gentar.biology.allele.Allele;
import org.gentar.biology.sequence.Sequence;
import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class AlleleSequence extends BaseEntity implements Serializable {
    @Id
    @SequenceGenerator(name = "alleleSequenceSeq", sequenceName = "ALLELE_SEQUENCE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "alleleSequenceSeq")
    private Long id;

    @ManyToOne(targetEntity= Allele.class)
    private Allele allele;

    @ManyToOne(targetEntity= Sequence.class)
    private Sequence sequence;

    private Integer index;
}
