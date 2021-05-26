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

    @Column(name = "number_of_cre_matings_started")
    private Integer numberOfCreMatingsStarted;

    @Column(name = "number_of_cre_matings_successful")
    private Integer numberOfCreMatingsSuccessful;

    @Column(name = "cre_excesion", columnDefinition = "boolean default false")
    private Boolean creExcesion;

    @Column(name = "tat_cre", columnDefinition = "boolean default false")
    private Boolean tatCre;

    @ManyToOne
    private Strain deleterStrain;

}
