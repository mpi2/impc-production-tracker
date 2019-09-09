package uk.ac.ebi.impc_prod_tracker.data.biology.plan_outcome;

import lombok.*;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.colony.Colony;
import uk.ac.ebi.impc_prod_tracker.data.biology.outcome.Outcome;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.biology.specimen.Specimen;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class PlanOutcome extends BaseEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "planOutcomeSeq", sequenceName = "PLAN_OUTCOME_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "planOutcomeSeq")
    private Long id;

    @ManyToOne
    private Plan plan;

    @ManyToOne
    private Outcome outcome;

    @ManyToOne
    private Colony colony;

    @ManyToOne
    private Specimen specimen;
}
