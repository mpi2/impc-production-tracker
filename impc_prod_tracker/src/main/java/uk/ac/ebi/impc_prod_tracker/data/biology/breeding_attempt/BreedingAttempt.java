package uk.ac.ebi.impc_prod_tracker.data.biology.breeding_attempt;

import lombok.*;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.breeding_attempt.breeding_type.BreedingType;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.biology.strain.Strain;

import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class BreedingAttempt extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "breedingAttemptSeq", sequenceName = "BREEDING_ATTEMPT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "breedingAttemptSeq")
    private Long id;

    @OneToOne
    @MapsId
    private Plan plan;

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
