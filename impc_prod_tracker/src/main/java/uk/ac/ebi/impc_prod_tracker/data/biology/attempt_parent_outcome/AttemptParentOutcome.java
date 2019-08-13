package uk.ac.ebi.impc_prod_tracker.data.biology.attempt_parent_outcome;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.Attempt;
import uk.ac.ebi.impc_prod_tracker.data.biology.colony.Colony;
import uk.ac.ebi.impc_prod_tracker.data.biology.outcome.Outcome;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class AttemptParentOutcome extends BaseEntity implements Serializable {
    @Id
    @SequenceGenerator(name = "projectLocationSeq", sequenceName = "PROJECT_LOCATION_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projectLocationSeq")
    private Long id;

    @ManyToOne
    private Attempt attempt;

    @ManyToOne
    private Outcome parentOutcome;

    @ManyToOne
    private Colony parentColony;
}
