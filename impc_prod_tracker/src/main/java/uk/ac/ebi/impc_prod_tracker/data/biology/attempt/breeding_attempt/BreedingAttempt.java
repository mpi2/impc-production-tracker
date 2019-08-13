package uk.ac.ebi.impc_prod_tracker.data.biology.attempt.breeding_attempt;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.Attempt;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.breeding_attempt.breeding_type.BreedingType;
import uk.ac.ebi.impc_prod_tracker.data.biology.strain.Strain;

import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class BreedingAttempt extends BaseEntity
{
    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne
    @MapsId
    private Attempt attempt;

    private Long imitsMouseAlleleModId;

    @Column(name = "number_of_cre_matings_started")
    private Integer numberOfCreMatingsStarted;

    @Column(name = "number_of_cre_matings_successfull")
    private Integer numberOfCareMatingsSuccessful;

    @Column(name = "cre_excesion")
    private Boolean creExcesion;

    @Column(name = "tat_cre")
    private Boolean tatCre;

    @ManyToOne
    private Strain deleterStrain;

    @ManyToOne
    private BreedingType type;
}
