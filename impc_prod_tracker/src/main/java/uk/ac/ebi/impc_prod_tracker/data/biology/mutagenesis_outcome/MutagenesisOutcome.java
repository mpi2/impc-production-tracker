package uk.ac.ebi.impc_prod_tracker.data.biology.mutagenesis_outcome;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.mouse_allele.MouseAllele;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.mutagenesis_attempt.MutagenesisAttempt;
import uk.ac.ebi.impc_prod_tracker.data.experiment.colony.Colony;
import uk.ac.ebi.impc_prod_tracker.data.experiment.privacy.Privacy;
import uk.ac.ebi.impc_prod_tracker.data.experiment.status.Status;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class MutagenesisOutcome extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "mutagenesisOutcomeSeq", sequenceName = "MUTAGENESIS_OUTCOME_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mutagenesisOutcomeSeq")
    private Long id;

    @ManyToOne
    private MutagenesisAttempt mutagenesisAttempt;

    @ManyToOne
    private Colony colony;

    @ManyToOne
    private Privacy privacy;

    @ManyToOne
    private Status status;

    @OneToOne
    @JoinColumn(unique = true)
    private MouseAllele allele;
}
