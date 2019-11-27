package org.gentar.biology.plan.outcome;

import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.biology.colony.Colony;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.specimen.Specimen;

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
