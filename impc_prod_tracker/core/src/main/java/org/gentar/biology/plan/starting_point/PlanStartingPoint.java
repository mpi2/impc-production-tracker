package org.gentar.biology.plan.starting_point;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.plan.Plan;
import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class PlanStartingPoint extends BaseEntity implements Serializable
{

    @Id
    @SequenceGenerator(name = "planStartingPointSeq", sequenceName = "PLAN_STARTING_POINT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "planStartingPointSeq")
    private Long id;

    @ManyToOne(targetEntity= Plan.class)
    private Plan plan;

    @ManyToOne(targetEntity= Outcome.class)
    private Outcome outcome;

}
