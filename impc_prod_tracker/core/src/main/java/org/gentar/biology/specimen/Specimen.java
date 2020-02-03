package org.gentar.biology.specimen;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.status.Status;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class Specimen extends BaseEntity {

    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne
    @MapsId
    private Outcome outcome;


    @Column(columnDefinition = "TEXT")
    private String specimenExternalRef;

    @ManyToOne
    private SpecimenType specimenType;

    @NotNull
    @ManyToOne(targetEntity= Status.class)
    private Status status;
}
