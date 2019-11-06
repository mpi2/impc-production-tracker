package uk.ac.ebi.impc_prod_tracker.data.biology.allele_sequence;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.allele.Allele;
import uk.ac.ebi.impc_prod_tracker.data.biology.sequence.Sequence;
import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class AlleleSequence extends BaseEntity implements Serializable {
    @Id
    @Column(name = "id")
    private Long id;

    @ManyToOne(targetEntity= Allele.class)
    private Allele allele;

    @ManyToOne(targetEntity= Sequence.class)
    private Sequence sequence;

    private Integer index;
}
