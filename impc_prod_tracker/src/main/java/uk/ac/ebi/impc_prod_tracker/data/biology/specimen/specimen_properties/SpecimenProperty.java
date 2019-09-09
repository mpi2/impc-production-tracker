package uk.ac.ebi.impc_prod_tracker.data.biology.specimen.specimen_properties;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.location.Location;
import uk.ac.ebi.impc_prod_tracker.data.biology.molecular_mutation_type.MolecularMutationType;
import uk.ac.ebi.impc_prod_tracker.data.biology.specimen.Specimen;
import uk.ac.ebi.impc_prod_tracker.data.biology.specimen.type.PropertyType;

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
