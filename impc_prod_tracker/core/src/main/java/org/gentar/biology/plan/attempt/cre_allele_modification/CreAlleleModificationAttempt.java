package org.gentar.biology.plan.attempt.cre_allele_modification;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.strain.Strain;

import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class CreAlleleModificationAttempt {

    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne
    @MapsId
    private Plan plan;

    private Long imitsMouseAlleleMod;

    /*
    2021-05-26
    This field that stores an id relating to the Targ Rep Allele table was in the iMits database table,
    but was not obviously used by any current iMits code, such as the Controller, and was not present
    in the iMits web interface. There was also no obvious reference to iMits allele modification data
    in the current Targ Rep web interface.
    The field probably represents a historical artifact, but the links to the Targ Rep Allele
    table when specified appeared correct. We have migrated the data in case it is required later,
    but do intend to expose the field publicly.
     */
    private Long targRepAllele;

    @Column(name = "number_of_cre_matings_successful")
    private Integer numberOfCreMatingsSuccessful;

    private String modificationExternalRef;

    @Column(name = "tat_cre", columnDefinition = "boolean default false")
    private Boolean tatCre;

    @ManyToOne
    private Strain deleterStrain;

}
