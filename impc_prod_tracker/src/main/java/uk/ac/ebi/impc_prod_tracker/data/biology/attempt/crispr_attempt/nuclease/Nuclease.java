package uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.nuclease;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.CrisprAttempt;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class Nuclease extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "nucleaseSeq", sequenceName = "NUCLEASE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nucleaseSeq")
    private Long id;

    @ManyToOne
    private CrisprAttempt crisprAttempt;

    private Integer concentration;

    @ManyToOne
    private NucleaseType nucleaseType;
}
