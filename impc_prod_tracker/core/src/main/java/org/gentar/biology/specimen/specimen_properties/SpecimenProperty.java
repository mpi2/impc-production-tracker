package org.gentar.biology.specimen.specimen_properties;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;
import org.gentar.biology.specimen.Specimen;
import org.gentar.biology.specimen.type.PropertyType;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class SpecimenProperty  extends BaseEntity implements Serializable {

    @Id
    @SequenceGenerator(name = "specimenPropertySeq", sequenceName = "SPECIMEN_PROPERTY_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "specimenPropertySeq")
    private Long id;


    @ManyToOne
    private Specimen specimen;

    @ManyToOne(targetEntity= PropertyType.class)
    private PropertyType propertyType;

    @Column(columnDefinition = "TEXT")
    private String value;
}
