package uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.nuclease;

import lombok.*;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.CrisprAttempt;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.nuclease.nuclease_type.NucleaseType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class Nuclease extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "nucleaseSeq", sequenceName = "NUCLEASE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nucleaseSeq")
    private Long id;

    @ManyToOne(targetEntity = CrisprAttempt.class)
    @JoinColumn(name = "attempt_id")
    private CrisprAttempt crisprAttempt;

    private Double concentration;

    @ManyToOne(targetEntity = NucleaseType.class)
    private NucleaseType nucleaseType;
}
