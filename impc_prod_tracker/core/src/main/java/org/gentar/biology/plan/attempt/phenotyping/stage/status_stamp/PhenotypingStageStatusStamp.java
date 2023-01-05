package org.gentar.biology.plan.attempt.phenotyping.stage.status_stamp;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;
import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStage;
import org.gentar.biology.status.Status;

import jakarta.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class PhenotypingStageStatusStamp extends BaseEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "phenotypingStageStatusStampSeq", sequenceName = "PHENOTYPING_STAGE_STATUS_STAMP_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phenotypingStageStatusStampSeq")
    private Long id;

    @NotNull
    @ManyToOne(targetEntity = PhenotypingStage.class)
    private PhenotypingStage phenotypingStage;

    @NotNull
    @ManyToOne(targetEntity = Status.class)
    private Status status;

    @NotNull
    private LocalDateTime date;
}
