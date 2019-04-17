package uk.ac.ebi.impc_prod_tracker.data.biology.breeding_outcome;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.breeding_attempt.BreedingAttempt;
import uk.ac.ebi.impc_prod_tracker.data.biology.tracked_mouse_allele.TrackedMouseAllele;
import uk.ac.ebi.impc_prod_tracker.data.experiment.colony.Colony;
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
public class BreedingOutcome extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "breedingOutcomeSeq", sequenceName = "BREEDING_OUTCOME_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "breedingOutcomeSeq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "breeding_attemmpt_id")
    private BreedingAttempt breedingAttempt;

    @ManyToOne
    private Colony colony;

    @ManyToOne
    private Status status;

    @OneToOne
    @JoinColumn(unique = true)
    private TrackedMouseAllele allele;
}
