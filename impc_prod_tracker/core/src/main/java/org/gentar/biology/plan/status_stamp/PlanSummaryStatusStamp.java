package org.gentar.biology.plan.status_stamp;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.status.Status;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class PlanSummaryStatusStamp extends BaseEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "planSummaryStatusStampSeq", sequenceName = "PLAN_SUMMARY_STATUS_STAMP_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "planSummaryStatusStampSeq")
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
