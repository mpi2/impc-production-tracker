package org.gentar.biology.outcome_status_stamp;

import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.status.Status;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class OutcomeStatusStamp extends BaseEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "outcomeStatusStampSeq", sequenceName = "OUTCOME_STATUS_STAMP_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "outcomeStatusStampSeq")
    private Long id;

    @NotNull
    @ManyToOne(targetEntity = Outcome.class)
    private Outcome outcome;

    @NotNull
    @ManyToOne(targetEntity = Status.class)
    private Status status;

    @NotNull
    private LocalDateTime date;
}
