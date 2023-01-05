package org.gentar.biology.specimen;

import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.audit.diff.IgnoreForAuditingChanges;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.specimen.property.SpecimenProperty;
import org.gentar.biology.specimen.status_stamp.SpecimenStatusStamp;
import org.gentar.biology.specimen.type.SpecimenType;
import org.gentar.biology.strain.Strain;
import org.gentar.biology.status.Status;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;

import jakarta.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class Specimen extends BaseEntity implements ProcessData
{
    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne
    @MapsId
    private Outcome outcome;

    @NotNull
    @ManyToOne(cascade=CascadeType.ALL)
    private Strain strain;

    @Column(columnDefinition = "TEXT")
    private String specimenExternalRef;

    @ManyToOne
    private SpecimenType specimenType;

    @NotNull
    @ManyToOne(targetEntity= Status.class)
    private Status status;

    @IgnoreForAuditingChanges
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade= CascadeType.ALL, mappedBy = "specimen")
    private Set<SpecimenStatusStamp> specimenStatusStamps;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade= CascadeType.ALL, mappedBy = "specimen")
    private Set<SpecimenProperty> specimenProperties;

    private transient ProcessEvent event;

    @Override
    public ProcessEvent getProcessDataEvent() {
        return this.event;
    }

    @Override
    public void setProcessDataEvent(ProcessEvent processEvent) {
        this.event=processEvent;
    }

    @Override
    public Status getProcessDataStatus() {
        return this.status;
    }

    @Override
    public void setProcessDataStatus(Status status) {
        this.status=status;
    }
}
