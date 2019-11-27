package org.gentar.biology.plan.plan_starting_point;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
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
public class PlanStartingPoint extends BaseEntity implements Serializable {

    @Id
    @SequenceGenerator(name = "planStartingPointSeq", sequenceName = "PLAN_STARTING_POINT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "planStartingPointSeq")
    private Long id;

    @ManyToOne(targetEntity= Plan.class)
    private Plan plan;

    @ManyToOne(targetEntity= Outcome.class)
    private Outcome outcome;

    @ManyToOne(targetEntity= Colony.class)
    private Colony colony;

    @ManyToOne(targetEntity= Specimen.class)
    private Specimen specimen;

}
