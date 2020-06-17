package org.gentar.biology.specimen.status_stamp;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;
import org.gentar.biology.specimen.Specimen;
import org.gentar.biology.status.Status;
import org.gentar.biology.status.StatusStamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class SpecimenStatusStamp extends BaseEntity implements Serializable, StatusStamp
{
    @Id
    @SequenceGenerator(name = "specimenStatusStampSeq", sequenceName = "SPECIMEN_STATUS_STAMP_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "specimenStatusStampSeq")
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "specimen_id")
    private Specimen specimen;

    @NotNull
    @ManyToOne(targetEntity = Status.class)
    private Status status;

    @NotNull
    private LocalDateTime date;

    @Override
    public String getStatusName()
    {
        return status.getName();
    }
}