package org.gentar.biology.specimen;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.specimen.type.SpecimenType;
import org.gentar.biology.strain.Strain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class Specimen extends BaseEntity {

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


}
