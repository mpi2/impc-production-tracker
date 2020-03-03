package org.gentar.biology.specimen.property;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;
import org.gentar.biology.specimen.property.type.PropertyType;
import org.gentar.biology.specimen.Specimen;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
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
