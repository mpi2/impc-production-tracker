package org.gentar.biology.colony.status_stamp;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;
import org.gentar.biology.colony.Colony;
import org.gentar.biology.status.Status;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class ColonyStatusStamp extends BaseEntity implements Serializable {
    @Id
    @SequenceGenerator(name = "colonyStatusStampSeq", sequenceName = "COLONY_STATUS_STAMP_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "colonyStatusStampSeq")
    private Long id;

    @NotNull
    @ManyToOne(targetEntity = Colony.class)
    private Colony colony;

    @NotNull
    @ManyToOne(targetEntity = Status.class)
    private Status status;

    @NotNull
    private LocalDateTime date;
}
