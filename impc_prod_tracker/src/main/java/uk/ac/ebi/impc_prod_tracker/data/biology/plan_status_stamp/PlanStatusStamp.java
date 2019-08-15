package uk.ac.ebi.impc_prod_tracker.data.biology.plan_status_stamp;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.biology.status.Status;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class PlanStatusStamp extends BaseEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "statusStampSeq", sequenceName = "STATUS_STAMP_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "statusStampSeq")
    private Long id;

    @NotNull
    @ManyToOne(targetEntity = Plan.class)
    private Plan plan;

    @NotNull
    @ManyToOne(targetEntity = Status.class)
    private Status status;

    @NotNull
    private LocalDateTime date;
}
